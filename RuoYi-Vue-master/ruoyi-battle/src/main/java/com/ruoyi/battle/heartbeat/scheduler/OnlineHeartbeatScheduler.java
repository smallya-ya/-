package com.ruoyi.battle.heartbeat.scheduler;

import  com.ruoyi.battle.heartbeat.service.OnlineHeartbeatService;
import com.ruoyi.common.battle.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author hongjiasen
 */
@Component
public class OnlineHeartbeatScheduler {

    @Value("${singleMode}")
    private int singleMode;
    private OnlineHeartbeatService onlineHeartbeatService;

    public OnlineHeartbeatScheduler(OnlineHeartbeatService onlineHeartbeatService) {
        this.onlineHeartbeatService = onlineHeartbeatService;
    }

    @Scheduled(fixedRate = 10000)
    public void doTask() {
        if (Constant.ONLINE_MODE == singleMode) {
            onlineHeartbeatService.sendHeatbeat();
        }
    }

}
