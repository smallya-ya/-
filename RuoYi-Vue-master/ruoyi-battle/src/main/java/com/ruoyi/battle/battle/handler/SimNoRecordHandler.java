package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;

import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class SimNoRecordHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(ff0a)\\w{28}(affa)$");

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
        String simNo = str.substring(10, 30);
        vestEntity.setSimNo(simNo);
        log.info("[指令处理]：收到{}号上报 SIM为{}", vestNum, simNo);
    }
}
