package com.ruoyi.battle.bayonet.context;

import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.thread.InstructionProcessThread;
import  com.ruoyi.battle.bayonet.thread.MqttWriteDataThread;
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

/**
 * @author hongjiasen
 */
public class BayonetBattleMqttContext extends AbstractBayonetBattleContext {

    private Logger log = LoggerFactory.getLogger(getClass());

    // 需要内部自己初始化的属性

    private boolean isStop;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private ScheduledExecutorService scheduledExecutorService;
    private MqttProperty mqttProperty;
    private MemoryPersistence persistence;
    private MqttClient mqttClient;
    private ExecutorService executor;
    private MqttWriteDataThread mqttWriteDataThread;
    private InstructionProcessThread instructionProcessThread;
//    private int countdownPeriod;
//    private CountdownThread countdownThread;

    public BayonetBattleMqttContext(BayonetBattleEntity battleEntity, BayonetCompetitionConfigEntity config
            , List<BayonetCompetitionGroupVO> groupList, BayonetScorePlanEntity scorePlan
            , List<BayonetHitScoreEntity> hitScoreList, List<BayonetHitAreaConfigEntity> hitAreaList) {
        this.battleEntity = battleEntity;
        this.config = config;
        this.groupList = groupList;
        this.scorePlan = scorePlan;
        this.hitScoreList = hitScoreList;
        this.hitAreaList = hitAreaList;
    }

    @Override
    public void init() throws Exception {
        super.init();
        // 初始化任务队列
        rollingMissionBlockingQueue = new PriorityBlockingQueue<>(256
                , Comparator.comparingInt(SendDataMissionModel::getPriority).thenComparing(SendDataMissionModel::getDateTime));

        // 初始化线程池
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        executor =  Executors.newFixedThreadPool(2);

        // 初始化倒计时线程 2022/09/02 由前端控制倒计时
//        countdownPeriod = Integer.parseInt(SpringUtil.getProperty("countdownPeriod"));
//        countdownThread = new CountdownThread(config.getLimitedTime() * 60, countdownPeriod);

        // 初始化数据处理线程
        instructionProcessThread = new InstructionProcessThread(this, new LinkedBlockingDeque<>(1024), this.hitAreaMap, this.hitAreaScoreMap);

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
        log.info("[指令处理]：MQTT连接成功，Host:{}，指令下发topic:{}，数据上报topic:{}", mqttProperty.getHost()
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
                .data(DataFrameUtils.createBayonetBattleStartData())
                .dateTime(LocalDateTime.now())
                .build();
        rollingMissionBlockingQueue.put(data);

        // 启动倒计时线程
//        scheduledExecutorService.scheduleAtFixedRate(countdownThread, countdownPeriod, countdownPeriod, TimeUnit.SECONDS);

        // 启动数据处理线程
        executor.execute(mqttWriteDataThread);
        executor.execute(instructionProcessThread);
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
                    .data(DataFrameUtils.createBayonetBattleEndData())
                    .dateTime(LocalDateTime.now())
                    .build();
            for (int i = 0; i < Constant.STOP_BATTLE_RESPON_TIME; i++){
                rollingMissionBlockingQueue.put(data);
            }

            TimeUnit.SECONDS.sleep(2);
        }

        isStop = true;
        if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
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
}
