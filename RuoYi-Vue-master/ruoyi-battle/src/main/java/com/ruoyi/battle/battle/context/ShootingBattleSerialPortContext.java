package com.ruoyi.battle.battle.context;

import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleBaseConfigEntity;
import com.ruoyi.battle.battle.domain.BattleEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity2;
import  com.ruoyi.battle.battle.service.HandleService;
import com.ruoyi.battle.battle.thread.*;
import com.ruoyi.battle.map.domain.MapEntity;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author hongjiasen
 */
public class ShootingBattleSerialPortContext extends AbstractShootingBattleContext {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // 需要外部传入的属性

    private SerialPort serialPort;
    private SerialPort virtualSerialPort;

    // 需要内部自己初始化的属性

    private boolean isStop;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private HandleService handleService;
    private ExecutorService executor;
    private SerialPortWriteDataThread serialPortWriteDataThread;
    private InstructionProcessThread instructionProcessThread;
    private IndoorLocationThread indoorLocationThread;
    private MapDataThread mapDataThread;
    private BattleRecordThread battleRecordThread;
    private ScheduledExecutorService scheduledExecutorService;
    private BattleRollThread battleRollThread;

    public ShootingBattleSerialPortContext(BattleEntity battleEntity
            , BattleBaseConfigEntity battleBaseConfigEntity
            , List<BattleTeamConfigEntity> battleTeamConfigEntityList, MapEntity mapEntity
            , SerialPort serialPort, SerialPort virtualSerialPort) {
        this.battleEntity = battleEntity;
        this.battleBaseConfigEntity = battleBaseConfigEntity;
        this.battleTeamConfigEntityList = battleTeamConfigEntityList;
        this.mapEntity = mapEntity;
        this.serialPort = serialPort;
        this.virtualSerialPort = virtualSerialPort;
    }

    public ShootingBattleSerialPortContext(BattleEntity battleEntity
            , BattleBaseConfigEntity battleBaseConfigEntity
            , MapEntity mapEntity, SerialPort serialPort, SerialPort virtualSerialPort
            , List<BattleTeamConfigEntity2> battleTeamConfigEntityList2) {
        this.battleEntity = battleEntity;
        this.battleBaseConfigEntity = battleBaseConfigEntity;
        this.battleTeamConfigEntityList2 = battleTeamConfigEntityList2;
        this.mapEntity = mapEntity;
        this.serialPort = serialPort;
        this.virtualSerialPort = virtualSerialPort;
    }

    @Override
    public void init() throws Exception {
        super.init();

        // 初始化任务队列
        rollingMissionBlockingQueue = new PriorityBlockingQueue<>(256
                , Comparator.comparingInt(SendDataMissionModel::getPriority).thenComparing(SendDataMissionModel::getDateTime));

        // 注册串口监听事件
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {}
        serialPort.addEventListener(serialPortEvent -> readData(), 1);
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            try {
                virtualSerialPort.removeEventListener();
            } catch (Exception e) {}
            virtualSerialPort.addEventListener(serialPortEvent -> readIndoorData());
        }

        // 初始化线程池
        executor =  Executors.newFixedThreadPool(Constant.INDOOR_TYPE == mapEntity.getType() ? 5 : 3);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // 初始化轮询线程
        battleRollThread = new BattleRollThread(vestEntityMap.values(), rollingMissionBlockingQueue);

        // 初始化数据处理线程
        handleService = SpringUtil.getBean(HandleService.class);
        serialPortWriteDataThread = new SerialPortWriteDataThread(this, serialPort, rollingMissionBlockingQueue);
        instructionProcessThread = new InstructionProcessThread(this, new LinkedBlockingDeque<>(1024), handleService);
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            mapDataThread = new MapDataThread(this, mapEntity, vestEntityMap.values(), rollingMissionBlockingQueue);
            indoorLocationThread = new IndoorLocationThread(this, new LinkedBlockingDeque<>(1024), handleService);
        }
        battleRecordThread = new BattleRecordThread(this, battleEntity.getId(), vestEntityMap.values());
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

        // 1秒执行一次轮询
        scheduledExecutorService.scheduleAtFixedRate(battleRollThread, 1, 1, TimeUnit.SECONDS);

        // 启动各个数据处理线程
        executor.execute(serialPortWriteDataThread);
        executor.execute(instructionProcessThread);
        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            executor.execute(mapDataThread);
            executor.execute(indoorLocationThread);
        }
        executor.execute(battleRecordThread);
    }

    @Override
    public void stop(boolean exist) throws Exception {
        super.stop(exist);
        vestEntityMap.clear();
        rollingMissionBlockingQueue.clear();
        if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
        }
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

        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            log.error("移除串口监听器出现异常", e);
        }

        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            try {
                virtualSerialPort.removeEventListener();
            } catch (Exception e) {
                log.error("移除虚拟串口监听器出现异常", e);
            }
        }

        log.info("[演习管理]：当前工作线程全部关闭，释放所有资源");
    }

    private void readData() {
        byte[] data = receiveData(serialPort, "串口管理");
        if (Objects.nonNull(data)) {
            try {
                instructionProcessThread.getDataQueue().put(data);
            } catch (InterruptedException e) {
                log.error("[指令处理]：发生异常", e);
            }
        }
    }

    private void readIndoorData() {
        byte[] data = receiveData(virtualSerialPort, "虚拟串口管理");
        if (Objects.nonNull(data)) {
            try {
                indoorLocationThread.getDataQueue().put(data);
            } catch (InterruptedException e) {
                log.error("[指令处理]：发生异常", e);
            }
        }
    }

    private byte[] receiveData(SerialPort serialPort, String serialPortTips) {
        byte[] bytes = null;
        try {
            bytes = serialPort.readBytes();
            if (Objects.nonNull(bytes)) {
                log.info("[{}]：收到的数据长度[{}]", serialPortTips, bytes.length);
                log.info("[{}]：收到的数据[{}]", serialPortTips, HexUtil.encodeHexStr(bytes));
                if (bytes[0] == 1) {
                    log.info("[{}]：设备识别码为0x01的数据,暂停轮询线程", serialPortTips);
                }
            }
        } catch (SerialPortException e) {
            log.error("[{}]：串口通讯发生错误[{}]", serialPortTips, e.getExceptionType(), e);
        } catch (Exception e) {
            log.error("[{}]：串口通讯发生错误[{}]", serialPortTips, e);
        }
        return bytes;
    }

    @Override
    public MapDataThread getMapDataThread() {
        return this.mapDataThread;
    }

    @Override
    public BattleRecordThread getBattleRecordThread() {
        return this.battleRecordThread;
    }

    @Override
    public SerialPortWriteDataThread getSerialPortWriteDataThread() {
        return this.serialPortWriteDataThread;
    }

    @Override
    public BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue() {
        return this.rollingMissionBlockingQueue;
    }

    @Override
    public boolean isStop() {
        return this.isStop;
    }
}
