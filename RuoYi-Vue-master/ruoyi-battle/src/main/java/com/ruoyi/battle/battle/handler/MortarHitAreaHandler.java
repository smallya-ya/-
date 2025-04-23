package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleLogEntity;
import  com.ruoyi.battle.battle.mapper.BattleLogMapper;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import  com.ruoyi.common.utils.battle.GpsCoordinateUtils;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class MortarHitAreaHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(ff07)\\w{16}(affa)$");
    private BattleLogMapper battleLogMapper = SpringUtil.getBean(BattleLogMapper.class);

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
        int angle = Integer.parseInt(str.substring(10, 14), 16);
        int distance = Integer.parseInt(str.substring(14, 18), 16);

        if(vestEntity.getWgs84Lng().equals(BigDecimal.ZERO)||vestEntity.getWgs84Lat().equals(BigDecimal.ZERO)){
            log.info("[指令处理]：{}号发射迫击炮，但是经纬度坐标为零，不处理", vestNum);
            return;
        }
        // 炮击跑击中坐标
        double[] hitArea = GpsCoordinateUtils.calLocationByDistanceAndDegree(vestEntity.getWgs84Lng().doubleValue(), vestEntity.getWgs84Lat().doubleValue(), angle, distance);
        GlobalCoordinates hitCoordinates = new GlobalCoordinates(hitArea[0], hitArea[1]);

        BattleService.context.getVestEntityMap().forEach((num,vest) -> {
            if (vest.getStatus() == Constant.OFF_LINE_STATUS || vest.getHp() == 0 || vest.getNum() == vestNum) {
                // 生命值为0和自己本身不处理
                log.info("[指令处理]：{}号发射迫击炮 {}号不处理", vestNum, num);
            } else if (vest.getLat().equals(BigDecimal.ZERO)||vest.getLng().equals(BigDecimal.ZERO)){
                log.info("[指令处理]：{}号发射迫击炮 {}号坐标为零，不处理", vestNum, num);
            }else {
                GlobalCoordinates vestCoordinates = new GlobalCoordinates(vest.getWgs84Lat().doubleValue(), vest.getWgs84Lng().doubleValue());
                double dis = GpsCoordinateUtils.getDistanceMeter(hitCoordinates, vestCoordinates);
                if (dis - Constant.MORTAR_DEAD_DISTANCE < 0) {
                    vest.setHp(0);
                    log.info("[指令处理]：{}号发射迫击炮 击杀{}号", vestNum, num);
                    BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                            .aBattleLogEntity()
                            .battleId(BattleService.context.getBattleEntity().getId())
                            .time(LocalDateTime.now())
                            .type(Constant.DIE_LOG_TYPE)
                            .log(num + "号被" + vestNum + "号发射迫击炮击中阵亡")
                            .isShow(0)
                            .build();
                    battleLogMapper.insert(battleLogEntity);
                    SendDataMissionModel sendData = SendDataMissionModel.SendDataMissionModelBuilder
                            .aSendDataMissionModel()
                            .vestNum(vest.getNum())
                            .isWait(false)
                            .priority(Constant.HIGH_PRIORITY)
                            .data(DataFrameUtils.createDieData(vest.getNum()))
                            .dateTime(LocalDateTime.now())
                            .build();
                    try {
                        log.info("[指令处理]：炮击炮击杀，发送判死命令");
                        BattleService.context.getRollingMissionBlockingQueue().put(sendData);
                    } catch (InterruptedException e) {
                        log.error("[指令处理异常]：炮击炮发送判死命令异常",e);
                    }
                }
            }
        });
    }
}
