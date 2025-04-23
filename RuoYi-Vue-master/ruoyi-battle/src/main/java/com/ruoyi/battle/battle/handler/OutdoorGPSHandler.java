package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.Point;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class OutdoorGPSHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(ff04)\\w{24}(affa)$");

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
        double lng = Integer.parseInt(str.substring(10, 18), 16) * 1.0 / 1000000;
        double lat = Integer.parseInt(str.substring(18, 26), 16) * 1.0 / 1000000;
        log.info("[指令处理]：收到[{}]号上报 WGS-84坐标系坐标经度{} 纬度{}", vestNum, lng, lat);
        vestEntity.setLastReportTime(LocalDateTime.now());
        Point point = new Point(lng, lat);
        vestEntity.setLng(BigDecimal.valueOf(point.getBdLng()).setScale(6, RoundingMode.HALF_UP));
        vestEntity.setLat(BigDecimal.valueOf(point.getBdLat()).setScale(6, RoundingMode.HALF_UP));
        vestEntity.setWgs84Lng(BigDecimal.valueOf(lng).setScale(6, RoundingMode.HALF_UP));
        vestEntity.setWgs84Lat(BigDecimal.valueOf(lat).setScale(6, RoundingMode.HALF_UP));
    }
}
