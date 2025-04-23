package com.ruoyi.battle.serialport.controller;

import  com.ruoyi.battle.serialport.domain.SerialPortEntity;
import  com.ruoyi.battle.serialport.service.SerialPortService;
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
@ServerEndpoint("/socket/out")
public class SerialPortWebSocketController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String heartbeatTag = "pong";
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static SerialPortService serialPortService;

    @Autowired
    public void setSerialPortService(SerialPortService serialPortService) {
        SerialPortWebSocketController.serialPortService = serialPortService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionMap.put(session.getId(), session);
//        log.info("室外天线有新连接{}加入！当前在线人数为{}", session.getId(), sessionMap.size());
        if (null != serialPortService.getNowComStatus()) {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .data(serialPortService.getNowComStatus())
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
//        log.info("室外天线有一连接{}关闭！当前在线人数为{}", session.getId(), sessionMap.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("室外天线收到{}心跳信息：{}", session.getId(), message);
        if (null != serialPortService.getNowComStatus()) {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .data(serialPortService.getNowComStatus())
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        } else {
            session.getBasicRemote().sendText(HeartbeatResponse.HeartbeatResponseBuilder
                    .aHeartbeatResponse()
                    .code(HttpServletResponse.SC_OK)
                    .status(Constant.SUCCESS_STATUS)
                    .data(SerialPortEntity.SerialPortEntityBuilder
                            .aSerialPortEntity()
                            .status(Constant.UNNORMAL_STATUS)
                            .type(0)
                            .build())
                    .heartTag(heartbeatTag)
                    .build()
                    .toString());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("室外天线WebSocket通信发生异常", error);
    }

    public static void noticeComStatus(SerialPortEntity serialPortEntity) throws IOException {
        String msg = HeartbeatResponse.HeartbeatResponseBuilder
                .aHeartbeatResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialPortEntity)
                .heartTag(heartbeatTag)
                .build()
                .toString();
        for (Session session : sessionMap.values()) {
            session.getBasicRemote().sendText(msg);
        }
    }
}
