package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class IndoorLocationHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(ff06)\\w{24}(affa)$");

    @Override
    protected boolean dataTypeVerification(String data) {
        return pattern.matcher(data).matches();
    }

    @Override
    public void handle(byte[] data) {
        super.handle(data);

        String str = HexUtil.encodeHexStr(data);
        int vestNum = Integer.parseInt(str.substring(6, 10), 16);
        ShootingVestModel vestEntity = BattleService.context.getVestEntityMap().get(vestNum);
        int width = Integer.parseInt(str.substring(10, 18), 16);
        int height = Integer.parseInt(str.substring(18, 26), 16);
        log.info("[指令处理]：收到{}号上报 室内坐标系坐标{},{}", vestNum, width, height);
        vestEntity.setLastReportTime(LocalDateTime.now());
        vestEntity.setLng(BigDecimal.valueOf(width));
        vestEntity.setLat(BigDecimal.valueOf(height));
    }
}
