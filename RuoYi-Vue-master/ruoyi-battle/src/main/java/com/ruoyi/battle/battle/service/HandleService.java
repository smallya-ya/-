package com.ruoyi.battle.battle.service;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.controller.ShootingBattleWebSocketController;
import  com.ruoyi.battle.battle.handler.DataHandlerContext;
import  com.ruoyi.battle.battle.handler.HeartbeatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author hongjiasen
 */
@Component
public class HandleService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public void handle(byte[] data) {
        String dataStr = HexUtil.encodeHexStr(data);
        int startFrame = 0;
        int endFrame;
        while (true) {
            if (startFrame >= dataStr.length()) {
                break;
            }
            startFrame = dataStr.indexOf("ff0", startFrame);
            if (startFrame < 0) {
                log.error("数据帧{}存在错误，跳过处理", dataStr);
                break;
            }
            endFrame = dataStr.indexOf("affa", startFrame);
            byte[] targetData = Arrays.copyOfRange(data, startFrame / 2, endFrame / 2 + 2);
            try {
                DataHandlerContext.getHandler(targetData[1]).handle(targetData);
                log.info("[HandleService]:notice");
                if(DataHandlerContext.getHandler(targetData[1]) instanceof HeartbeatHandler
                && !HeartbeatHandler.updateFlage){
                    log.info("正常心跳数据，不发送给前端更新");
                }else{
                    ShootingBattleWebSocketController.notice();
                }

            } catch (Exception e) {
                log.error("发生异常，继续下轮命令处理",e);
            }
            startFrame = endFrame + 4;
        }
    }
}
