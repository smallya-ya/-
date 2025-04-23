package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetData;
import  com.ruoyi.battle.bayonet.service.BayonetService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongjiasen
 */
@Component
@ServerEndpoint("/socket/bayonet")
public class BayonetBattleWebSocketController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionMap.put(session.getId(), session);
        log.info("有新连接加入！当前连接刺杀演习客户端数量为{}", sessionMap.size());
        if (null != BayonetService.context) {
            BayonetData bayonetData = new BayonetData();
            bayonetData.setNum(-1);
            bayonetData.setName("");
            bayonetData.setVestList(BayonetService.context.getVestEntityMap().values());
            session.getBasicRemote().sendText(BaseResponse.BaseResponseBuilder.aBaseResponse()
                    .code(200)
                    .status(Constant.SUCCESS_STATUS)
                    .msg("获取演习日志成功")
                    .data(bayonetData)
                    .build()
                    .toString());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("有一连接关闭！当前连接刺杀演习客户端数量为{}", sessionMap.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket通信发生异常", error);
    }

    public static void notice(BayonetData data) throws IOException {
        for (Session session : sessionMap.values()) {
            session.getBasicRemote().sendText(BaseResponse.BaseResponseBuilder.aBaseResponse()
                    .code(200)
                    .status(Constant.SUCCESS_STATUS)
                    .msg("获取演习日志成功")
                    .data(data)
                    .build()
                    .toString());
        }
    }
}
