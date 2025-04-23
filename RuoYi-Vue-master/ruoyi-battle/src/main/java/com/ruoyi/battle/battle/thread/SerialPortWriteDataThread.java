package com.ruoyi.battle.battle.thread;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.battle.context.BattleContext;
import  com.ruoyi.battle.battle.service.BattleService;
import  com.ruoyi.battle.serialport.service.SerialPortService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * @author hongjiasen
 */
public class SerialPortWriteDataThread extends Thread {

    private Logger log = LoggerFactory.getLogger(getClass());

    private BattleContext context;
    private SerialPort serialPort;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private final Object lock = new Object(); // 锁对象

    public SerialPortWriteDataThread(BattleContext context, SerialPort serialPort, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.context = context;
        this.serialPort = serialPort;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[串口任务控制线程]：启动");
        while (!this.context.isStop()) {
            try {
                SendDataMissionModel sendDataMissionModel = rollingMissionBlockingQueue.take();
                ShootingVestModel vestModel = BattleService.context.getVestEntityMap().get(sendDataMissionModel.getVestNum());
                if(vestModel!=null && vestModel.getHp()==0) {
                    if(sendDataMissionModel.getPriority()== Constant.LOW_PRIORITY){//已阵亡的心跳轮询，跳过
                        log.info("[串口任务控制线程]：当前{}号已阵亡，跳过轮询发送", vestModel.getNum());
                        continue;
                    }
                    log.info("[串口任务控制线程]：这里是判死、迫击炮攻击判死命令");
                }
                log.info("[串口任务控制线程]：发送{}", HexUtil.encodeHexStr(sendDataMissionModel.getData()));
                serialPort.writeBytes(sendDataMissionModel.getData());

                synchronized (lock) { // 使用锁对象进行同步
                    if (sendDataMissionModel.isWait()) {
                        long pollingTimeOut = NumberUtil.parseLong(SpringUtil.getProperty("pollingTimeOut"), 100L);
                        lock.wait(pollingTimeOut); // 等待唤醒
                    } else {
                        lock.wait(100); // 等待一定时间
                    }
                }
            } catch (InterruptedException e) {
                log.error("[串口任务控制线程]：任务结束");
                Thread.currentThread().interrupt(); // 恢复中断状态
            } catch (SerialPortException e) {
                handleSerialPortException(e);
                break;
            } catch (Exception e) {
                log.error("[串口任务控制线程]：发送数据时出现异常", e);
            }
        }
        log.info("[串口任务控制线程]：结束");
    }

    public void wakeUp() {
        synchronized (lock) {
            lock.notify(); // 唤醒等待的线程
        }
    }

    private void handleSerialPortException(SerialPortException e) {
        if (SerialPortException.TYPE_PORT_NOT_FOUND.equals(e.getExceptionType())) {
            log.error("[串口任务控制线程]：串口异常，无法正常连接，结束演习");
            BattleService battleService = SpringUtil.getBean(BattleService.class);
            SerialPortService serialPortService = SpringUtil.getBean(SerialPortService.class);
            try {
                battleService.endBattle(false);
                serialPortService.closePort("串口异常，无法正常连接，关闭串口");
            } catch (Exception ex) {
                log.error("关闭串口异常：",e);
            }
        } else {
            log.error("[串口任务控制线程]：发送数据时出现异常", e);
        }
    }
}


