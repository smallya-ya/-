package com.ruoyi.battle.battle.thread;

import  com.ruoyi.battle.battle.controller.ShootingBattleWebSocketController;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author hongjiasen
 */
public class ForceOnlineThread implements Runnable {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        if (null != BattleService.context) {
            for (ShootingVestModel vestEntity : BattleService.context.getVestEntityMap().values()) {
                if (Constant.OFF_LINE_STATUS == vestEntity.getStatus()) {
                    log.info("强制{}号上线", vestEntity.getNum());
                    vestEntity.setStatus(Constant.LIVE_STATUS);
                    vestEntity.setHp(100);
                    vestEntity.setAmmo1(100);
                    vestEntity.setAmmo2(100);
                }
            }
            try {
                ShootingBattleWebSocketController.notice();
            } catch (IOException e) {
                log.error("实兵演习WebSocket通信发生异常", e);
            }
        }
    }
}
