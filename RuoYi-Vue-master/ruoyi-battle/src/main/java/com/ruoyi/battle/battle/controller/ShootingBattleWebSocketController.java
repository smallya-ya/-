package com.ruoyi.battle.battle.controller;

import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.HeartbeatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongjiasen
 */
@Component
@ServerEndpoint("/socket/ShootingBattle")
public class ShootingBattleWebSocketController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static BattleService battleService;
    private static final String heartbeatTag = "pong";
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    public void setBattleService(BattleService battleService) {
        ShootingBattleWebSocketController.battleService = battleService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionMap.put(session.getId(), session);
        if (null != BattleService.context) {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .data(battleService.getNowBattleDetail(null))
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("实兵演习收到{}心跳信息：{}", session.getId(), message);
        if (null != BattleService.context) {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
//                    .data(battleService.getNowBattleDetail(null))
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        } else {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .msg("当前没有在进行的实兵演习")
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        }
    }

    public static void notice() throws IOException {
        for (Session session : sessionMap.values()) {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .data(battleService.getNowBattleDetail(null))
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        }
    }

}
