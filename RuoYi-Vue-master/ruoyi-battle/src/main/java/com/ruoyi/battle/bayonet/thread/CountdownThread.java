package com.ruoyi.battle.bayonet.thread;

import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.bayonet.service.BayonetService;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongjiasen
 */
public class CountdownThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private int countdownTime;
    private int countdownPeriod;

    public CountdownThread(int countdownTime, int countdownPeriod) {
        this.countdownTime = countdownTime;
        this.countdownPeriod = countdownPeriod;
    }

    @Override
    public void run() {
        this.countdownTime = this.countdownTime - countdownPeriod;
        log.info("[倒计时线程]：当前刺杀演习剩余{}秒", this.countdownTime);
        if (countdownTime <= 0) {
            log.info("[倒计时线程]：倒计时结束，结束刺杀演习");
            try {
                BayonetService bayonetService = SpringUtil.getBean(BayonetService.class);
                bayonetService.stop(false);
            } catch (InterruptedException e) {
                log.error("[倒计时线程]：任务结束");
            } catch (SerialPortException e) {
                log.error("[倒计时线程]：任务结束", e);
            } catch (Exception e) {
                log.error("[倒计时线程]：任务结束", e);
            }
        }
    }
}
