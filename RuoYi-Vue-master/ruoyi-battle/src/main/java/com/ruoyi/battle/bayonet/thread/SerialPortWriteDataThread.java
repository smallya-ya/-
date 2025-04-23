package com.ruoyi.battle.bayonet.thread;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.bayonet.context.BayonetBattleContext;
import  com.ruoyi.battle.bayonet.service.BayonetService;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author hongjiasen
 */
public class SerialPortWriteDataThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private BayonetBattleContext context;
    private SerialPort serialPort;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public SerialPortWriteDataThread(BayonetBattleContext context, SerialPort serialPort, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
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
                log.info("[串口任务控制线程]：发送{}", HexUtil.encodeHexStr(sendDataMissionModel.getData()));
                serialPort.writeBytes(sendDataMissionModel.getData());
                if (sendDataMissionModel.isWait()) {
                    long pollingTimeOut = NumberUtil.parseLong(SpringUtil.getProperty("pollingTimeOut"), 100L);
                    TimeUnit.MILLISECONDS.sleep(pollingTimeOut);
                } else {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                log.error("[串口任务控制线程]：任务结束");
            } catch (SerialPortException e) {
                if (SerialPortException.TYPE_PORT_NOT_FOUND.equals(e.getExceptionType())) {
                    log.error("[串口任务控制线程]：串口异常，无法正常连接，结束演习");
                    BayonetService bayonetService = SpringUtil.getBean(BayonetService.class);
                    try {
                        bayonetService.stop(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                } else {
                    log.error("[串口任务控制线程]：发送数据时出现异常", e);
                }
            } catch (Exception e) {
                log.error("[串口任务控制线程]：发送数据时出现异常", e);
            }
        }
        log.info("[串口任务控制线程]：结束");
    }
}
