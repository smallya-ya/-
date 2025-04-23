package com.ruoyi.battle.battle.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.battle.controller.ShootingBattleWebSocketController;
import com.ruoyi.battle.battle.domain.BattleLogEntity;
import  com.ruoyi.battle.battle.mapper.BattleLogMapper;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hongjiasen
 */
@Service
public class ShootingVestService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BattleLogMapper battleLogMapper;

    public Collection<ShootingVestModel> getAllVest() {
        return BattleService.context.getVestEntityMap().values();
    }

    public Collection<ShootingVestModel> setAllVestNanme(List<ShootingVestModel> nameList) {
        Map<Integer, ShootingVestModel> vestEntityMap = BattleService.context.getVestEntityMap();
        nameList.forEach((vest)->{
            if(vestEntityMap.get(vest.getNum())!=null){
                vestEntityMap.get(vest.getNum()).setName(vest.getName());
            }
        });
        return BattleService.context.getVestEntityMap().values();
    }

    public int random(int num) {
        List<Integer> vestList = BattleService.context.getVestEntityMap()
                .values()
                .stream()
                .map(ShootingVestModel::getNum)
                .collect(Collectors.toList());
        int i = RandomUtil.randomInt(0, vestList.size());
        if (vestList.get(i) != num) {
            return vestList.get(i);
        } else {
            return this.random(num);
        }
    }

    public String randomPart() {
        List<String> list = new ArrayList<>();
        list.add("胸部");
        list.add("左手");
        list.add("右手");
        list.add("腹部");
        list.add("背部");
        list.add("左腿");
        list.add("右腿");
        return list.get(RandomUtil.randomInt(0, list.size()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendDieData(int vestNum, String killer) throws InterruptedException {
        log.info("[演习管理]：收到{}号判死请求", vestNum);

        // hp:0
        BattleService.context.getVestEntityMap().get(vestNum).setHp(0);
        BattleService.context.getVestEntityMap().get(vestNum).setStatus(Constant.DIE_STATUS);
        BattleService.context.getVestEntityMap().get(vestNum).setManualLiveFlag(0);

        String logg = Constant.TRUE.equals(SpringUtil.getProperty(Constant.FORCE_UP))
                ? vestNum + "号被" + this.random(vestNum) + "号击中头部阵亡"
                : vestNum + "号死亡 击杀者：" + killer;
        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.DIE_LOG_TYPE)
                .log(logg)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createDieData(vestNum))
                .dateTime(LocalDateTime.now())
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
        try {
            ShootingBattleWebSocketController.notice();
        } catch (IOException e) {
            log.error("实兵演习WebSocket通信发生异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendLiveData(int vestNum) throws InterruptedException {
        log.info("[演习管理]：收到{}号判活请求", vestNum);

        // hp:100
        BattleService.context.getVestEntityMap().get(vestNum).setHp(100);
        BattleService.context.getVestEntityMap().get(vestNum).setStatus(Constant.LIVE_STATUS);
        BattleService.context.getVestEntityMap().get(vestNum).setManualLiveFlag(1);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.LIVE_LOG_TYPE)
                .log(vestNum + "号复活")
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createLiveData(vestNum))
                .dateTime(LocalDateTime.now())
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
        try {
            ShootingBattleWebSocketController.notice();
        } catch (IOException e) {
            log.error("实兵演习WebSocket通信发生异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendInjureData(int vestNum) throws InterruptedException {
        log.info("[演习管理]：收到{}号判伤请求", vestNum);

        String logg = Constant.TRUE.equals(SpringUtil.getProperty(Constant.FORCE_UP))
                ? vestNum + "号被" + this.random(vestNum) + "号击中" + this.randomPart()
                : vestNum + "号判伤";

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.INJURE_LOG_TYPE)
                .log(logg)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createInjureData(vestNum))
                .dateTime(LocalDateTime.now())
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
        try {
            ShootingBattleWebSocketController.notice();
        } catch (IOException e) {
            log.error("实兵演习WebSocket通信发生异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void reloadAllVest(int ammo, int weapon) throws InterruptedException {
        log.info("[演习管理]：收到全体{}装弹{}请求", weapon == (byte) 0x02 ? "主武器" : "副武器", ammo);

        if (weapon == Constant.PRIMARY_WEAPON) {
            BattleService.context.getBattleEntity().setIsLoadAmmo(1);
            BattleService.context.getBattleEntity().setAmmo(ammo);
        } else if (weapon == Constant.SECONDARY_WEAPON) {
            BattleService.context.getBattleEntity().setIsLoadAmmo2(1);
            BattleService.context.getBattleEntity().setAmmo2(ammo);
        }

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.RELOAD_LOG_TPYE)
                .log("全体" + (weapon == (byte) 0x02 ? "主武器" : "副武器") + "装弹" + ammo)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createAllLoadingData(ammo, weapon))
                .dateTime(LocalDateTime.now())
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
        try {
            ShootingBattleWebSocketController.notice();
        } catch (IOException e) {
            log.error("实兵演习WebSocket通信发生异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void reloadVestAmmo(int vestNum, int ammo, int weapon) throws InterruptedException {
        log.info("[演习管理]：收到{}号的{}装弹{}请求", vestNum, weapon == (byte) 0x02 ? "主武器" : "副武器", ammo);

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.RELOAD_LOG_TPYE)
                .log(vestNum + "号" + (weapon == (byte) 0x02 ? "主武器" : "副武器") + "装弹" + ammo)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);

        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                .aSendDataMissionModel()
                .vestNum(0)
                .isWait(false)
                .priority(Constant.HIGH_PRIORITY)
                .data(DataFrameUtils.createSingleLoadingData(vestNum, ammo, weapon))
                .dateTime(LocalDateTime.now())
                .build();
        BattleService.context.getRollingMissionBlockingQueue().put(data);
        try {
            ShootingBattleWebSocketController.notice();
        } catch (IOException e) {
            log.error("实兵演习WebSocket通信发生异常", e);
        }
    }
}
