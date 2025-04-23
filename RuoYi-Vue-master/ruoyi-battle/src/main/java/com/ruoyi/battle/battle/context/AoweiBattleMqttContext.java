package com.ruoyi.battle.battle.context;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleBaseConfigEntity;
import com.ruoyi.battle.battle.domain.BattleEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity2;
import  com.ruoyi.battle.battle.service.HandleService;
import com.ruoyi.battle.battle.thread.AoweiMapDataThread;
import com.ruoyi.battle.battle.thread.BattleRecordThread;
import com.ruoyi.battle.battle.thread.InstructionProcessThread;
import com.ruoyi.battle.battle.thread.MqttWriteDataThread;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import  com.ruoyi.battle.mqtt.callback.ScdsMqttCallbackExtended;
import  com.ruoyi.battle.mqtt.domain.MqttProperty;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import  com.ruoyi.common.utils.battle.AliyunEmqxSSLFactory;
import com.ruoyi.battle.utils.DataFrameUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class AoweiBattleMqttContext extends AbstractAoweiBattleContext {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // 需要内部自己初始化的属性

    private boolean isStop;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private HandleService handleService;
    private MqttProperty mqttProperty;
    private MemoryPersistence persistence;
    private MqttClient mqttClient;
    private ExecutorService executor;
    private AoweiMapDataThread mapDataThread;
    private MqttWriteDataThread mqttWriteDataThread;
    private BattleRecordThread battleRecordThread;
    private InstructionProcessThread instructionProcessThread;

    public AoweiBattleMqttContext(BattleEntity battleEntity, BattleBaseConfigEntity battleBaseConfigEntity
            , List<BattleTeamConfigEntity> battleTeamConfigEntityList, BaseMapEntity mapEntity) {
        this.battleEntity = battleEntity;
        this.battleBaseConfigEntity = battleBaseConfigEntity;
        this.battleTeamConfigEntityList = battleTeamConfigEntityList;
        this.mapEntity = mapEntity;
    }

    public AoweiBattleMqttContext(BattleEntity battleEntity, BattleBaseConfigEntity battleBaseConfigEntity
            , BaseMapEntity mapEntity, List<BattleTeamConfigEntity2> battleTeamConfigEntityList2) {
        this.battleEntity = battleEntity;
        this.battleBaseConfigEntity = battleBaseConfigEntity;
        this.battleTeamConfigEntityList2 = battleTeamConfigEntityList2;
        this.mapEntity = mapEntity;
    }

    @Override
    public void init() throws Exception {
        super.init();

        // 初始化任务队列
        rollingMissionBlockingQueue = new PriorityBlockingQueue<>(256
                , Comparator.comparingInt(SendDataMissionModel::getPriority).thenComparing(SendDataMissionModel::getDateTime));

        // 初始化线程池
        executor =  Executors.newFixedThreadPool(4);

        // 初始化数据处理线程
        handleService = SpringUtil.getBean(HandleService.class);
        instructionProcessThread = new InstructionProcessThread(this, new LinkedBlockingDeque<>(1024), handleService);
        battleRecordThread = new BattleRecordThread(this, battleEntity.getId(), vestEntityMap.values());
        mapDataThread = new AoweiMapDataThread(this, mapEntity, vestEntityMap.values());

        // 初始化MQTT客户端
        persistence = new MemoryPersistence();
        mqttProperty = SpringUtil.getBean(MqttProperty.class);
        mqttClient = new MqttClient(mqttProperty.getHost(), mqttProperty.getClientId(), persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        if (mqttProperty.getSslUse()) {
            SSLSocketFactory sslSocketFactory
                    = AliyunEmqxSSLFactory.getSSLSocktet(mqttProperty.getSslCa()
                    , mqttProperty.getSslClientCa(), mqttProperty.getSslClientKey(), mqttProperty.getSslPassword());
            connOpts.setSocketFactory(sslSocketFactory);
        }
        connOpts.setUserName(mqttProperty.getUsername());
        connOpts.setPassword(mqttProperty.getPassword().toCharArray());
        connOpts.setConnectionTimeout(mqttProperty.getTimeout());
        connOpts.setKeepAliveInterval(mqttProperty.getKeepalive());
        connOpts.setCleanSession(mqttProperty.getCleanSession());
        ScdsMqttCallbackExtended callback = new ScdsMqttCallbackExtended(mqttProperty, connOpts
                , mqttClient, instructionProcessThread);
        mqttClient.setCallback(callback);
        mqttClient.connect(connOpts);
        log.info("[指令处理]：MQTT连接成功，上位机ClientId:{}，指令下发topic:{}，数据上报topic:{}", mqttProperty.getClientId()
                , mqttProperty.getProducerTopic(), mqttProperty.getConsumerTopic());

        mqttWriteDataThread = new MqttWriteDataThread(this, mqttProperty, mqttClient, rollingMissionBlockingQueue);
    }

    @Override
    public void start() throws Exception {
        isStop = false;
        // 下发演习开始指令
        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createStartBattleData(battleBaseConfigEntity.getMode()))
                .dateTime(LocalDateTime.now())
                .build();
        rollingMissionBlockingQueue.put(data);

        // 启动各个数据处理线程
        executor.execute(instructionProcessThread);
        executor.execute(mqttWriteDataThread);
        executor.execute(battleRecordThread);
        executor.execute(mapDataThread);
    }

    @Override
    public void stop(boolean exist) throws Exception {
        super.stop(exist);
        vestEntityMap.clear();
        rollingMissionBlockingQueue.clear();
        if (!exist) {
            SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                    .aSendDataMissionModel()
                    .vestNum(0)
                    .isWait(false)
                    .priority(Constant.HIGH_PRIORITY)
                    .data(DataFrameUtils.createFinishBattleData())
                    .dateTime(LocalDateTime.now())
                    .build();
            for (int i = 0; i < Constant.STOP_BATTLE_RESPON_TIME; i++){
                rollingMissionBlockingQueue.put(data);
            }

            TimeUnit.SECONDS.sleep(2);
        }

        isStop = true;
        if (null != executor && !executor.isShutdown()) {
            executor.shutdownNow();
        }

        log.info("[演习管理]：当前工作线程全部关闭，释放所有资源");

        if (mqttClient.isConnected()) {
            mqttClient.unsubscribe(mqttProperty.getConsumerTopic());
            mqttClient.disconnect();
            mqttClient.close();
            persistence.clear();
            persistence.close();
            log.info("[演习管理]：MQTT资源释放完毕");
        }
    }

    @Override
    public boolean isStop() {
        return this.isStop;
    }

    @Override
    public BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue() {
        return this.rollingMissionBlockingQueue;
    }

    @Override
    public BattleRecordThread getBattleRecordThread() {
        return this.battleRecordThread;
    }

    @Override
    public AoweiMapDataThread getMapDataThread() {
        return this.mapDataThread;
    }
}
