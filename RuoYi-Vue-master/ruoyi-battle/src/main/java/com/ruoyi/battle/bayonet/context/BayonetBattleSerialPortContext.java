package com.ruoyi.battle.bayonet.context;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.thread.BattleRollThread;
import  com.ruoyi.battle.bayonet.thread.InstructionProcessThread;
import  com.ruoyi.battle.bayonet.thread.SerialPortWriteDataThread;
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
public class BayonetBattleSerialPortContext extends AbstractBayonetBattleContext {

    private Logger log = LoggerFactory.getLogger(getClass());

    // 需要外部传入的属性

    private SerialPort serialPort;

    // 需要内部自己初始化的属性

    private boolean isStop;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private SerialPortWriteDataThread serialPortWriteDataThread;
    private ScheduledExecutorService scheduledExecutorService;
    private BattleRollThread battleRollThread;
    private ExecutorService executor;
    private InstructionProcessThread instructionProcessThread;
//    private int countdownPeriod;
//    private CountdownThread countdownThread;

    public BayonetBattleSerialPortContext(BayonetBattleEntity battleEntity, SerialPort serialPort, BayonetCompetitionConfigEntity config
            , List<BayonetCompetitionGroupVO> groupList, BayonetScorePlanEntity scorePlan
            , List<BayonetHitScoreEntity> hitScoreList, List<BayonetHitAreaConfigEntity> hitAreaList) {
        this.battleEntity = battleEntity;
        this.serialPort = serialPort;
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

        // 注册串口监听事件
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {}
        serialPort.addEventListener(serialPortEvent -> readData(), 1);

        // 初始化线程池
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        executor =  Executors.newFixedThreadPool(2);

        // 初始化轮询线程
        battleRollThread = new BattleRollThread(vestEntityMap.values(), rollingMissionBlockingQueue);
//        countdownPeriod = Integer.parseInt(SpringUtil.getProperty("countdownPeriod"));
//        countdownThread = new CountdownThread(config.getLimitedTime() * 60, countdownPeriod);

        // 初始化数据处理线程
        serialPortWriteDataThread = new SerialPortWriteDataThread(this, serialPort, rollingMissionBlockingQueue);
        instructionProcessThread = new InstructionProcessThread(this, new LinkedBlockingDeque<>(1024), this.hitAreaMap, this.hitAreaScoreMap);
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

        // 1秒执行一次轮询
        scheduledExecutorService.scheduleAtFixedRate(battleRollThread, 1, 1, TimeUnit.SECONDS);
//        scheduledExecutorService.scheduleAtFixedRate(countdownThread, countdownPeriod, countdownPeriod, TimeUnit.SECONDS);

        // 启动数据处理线程
        executor.execute(serialPortWriteDataThread);
        executor.execute(instructionProcessThread);
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
                    .data(DataFrameUtils.createBayonetBattleEndData())
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

        log.info("[演习管理]：当前工作线程全部关闭，释放所有资源");
    }

    @Override
    public boolean isStop() {
        return this.isStop;
    }

    private void readData() {
        try {
            byte[] data = serialPort.readBytes();
            if (Objects.nonNull(data)) {
                log.info("[串口管理]：收到的数据长度[{}]", data.length);
                log.info("[串口管理]：收到的数据[{}]", HexUtil.encodeHexStr(data));
                instructionProcessThread.getDataQueue().put(data);
            }
        } catch (SerialPortException e) {
            log.error("[串口管理]：串口通讯发生错误[{}]", e.getExceptionType(), e);
        } catch (InterruptedException e) {
            log.error("[指令处理]：发生异常", e);
        } catch (Exception e) {
            log.error("[串口管理]：串口通讯发生错误[{}]", e);
        }
    }
}
