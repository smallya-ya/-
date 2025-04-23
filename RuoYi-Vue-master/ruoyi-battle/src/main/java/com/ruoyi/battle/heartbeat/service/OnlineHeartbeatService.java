package com.ruoyi.battle.heartbeat.service;

import cn.hutool.system.oshi.OshiUtil;
import com.ruoyi.common.battle.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author hongjiasen
 */
@Component
public class OnlineHeartbeatService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${serverUrl}")
    private String serverUrl;
    @Value("${singleMode}")
    private int singleMode;
    private RestTemplate restTemplate;

    public OnlineHeartbeatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void sendHeatbeat() {
        if (Constant.ONLINE_MODE == singleMode) {
            try {
                restTemplate.getForEntity(serverUrl + "/client/heartbeat/"
                        + OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID(), String.class);
                log.info("[心跳测试]：服务端在线");
            } catch (Exception e) {
                log.error("[心跳测试]：服务端离线或本机未联网");
            }
        }
    }
}
