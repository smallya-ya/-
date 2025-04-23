package com.ruoyi.battle.battle.handler;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleLogEntity;
import com.ruoyi.battle.battle.domain.HitRecordModel;
import  com.ruoyi.battle.battle.mapper.BattleLogMapper;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class HitRecordHandler extends CommonDataHandler {

    private final Pattern pattern = Pattern.compile("^(ff05)\\w{14}(affa)$");
    private final BattleLogMapper battleLogMapper = SpringUtil.getBean(BattleLogMapper.class);

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
        int shotNum = Integer.parseInt(str.substring(10, 14), 16);

        if (null == vestEntity && !this.explore(shotNum)) {
            log.info("[指令处理]：马甲ID{}不在演习方案中，且不是 0XFFFF(手雷) 0XFFFE（爆炸物） 0XFFFD（地雷），舍弃该条命令{}", vestNum, str);
            return;
        }

        ShootingVestModel attack = BattleService.context.getVestEntityMap().get(shotNum);
        if (null == attack && !(shotNum >= 65533 && shotNum <= 65535)) {
            log.info("[指令处理]：马甲ID{}不在演习方案中，且不是 0XFFFF(手雷) 0XFFFE（爆炸物） 0XFFFD（地雷），舍弃该条命令{}", shotNum, str);
            return;
        }

        HitRecordModel hitRecordModel = HitRecordModel.HitRecordModelBuilder
                .aHitRecordModel()
                .hitTime(LocalDateTime.now())
                .shotNum(shotNum)
                .shotPart(Integer.parseInt(str.substring(14, 16), 16))
                .build();
        vestEntity.setHitRecordModel(hitRecordModel);
        vestEntity.setLastReportTime(LocalDateTime.now());
        vestEntity.setManualLiveFlag(-1);
        StringBuilder logStr = new StringBuilder(vestNum + "");
        logStr.append("号被");
        // 提取开枪者编号末位
        if (shotNum > Constant.PEOPLE_OR_OBJECT_TYPE) {
            logStr.append(shotNum)
                    .append("号")
                    .append(this.hitObject(shotNum))
                    .append("击中");
        } else {
            logStr.append(shotNum).append("号击中");
        }
        String shotPart;
//        if (vestNum >= Constant.PEOPLE_OR_OBJECT_TYPE) {
            // 类为装甲车类，击中部位为 ARMORED_CAR_SHOT_PART_MAP
//            shotPart = HitRecordModel.ARMORED_CAR_SHOT_PART_MAP.get(hitRecordModel.getShotPart());
//        } else {
            // 类为士兵类，集中部位为 SOLIDER_SHOT_PART_MAP
            shotPart = HitRecordModel.SOLIDER_SHOT_PART_MAP.get(hitRecordModel.getShotPart());
//        }
        logStr.append(shotPart);
        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.SHOT_LOG_TYPE)
                .log(logStr.toString())
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);
        log.info("[指令处理]：收到{}号上报 射击者{}号 击中部位{}", vestNum, shotNum, shotPart);
    }

    private boolean explore(int shutNum) {
        return ListUtil.of(65535, 65533, 65534).contains(shutNum);
    }

    private String hitObject(int num) {
        if (num >= 1801 && num <= 2000) {
            return "35榴弹";
        }
        if (num >= 2201 && num <= 2400) {
            return "80火";
        }
        if (num >= 2401 && num <= 2600) {
            return "120火";
        }
        if (num >= 3001 && num <= 3200) {
            return "步战车";
        }
        if (num >= 3201 && num <= 3400) {
            return "突击车";
        }
        if (num >= 3801 && num <= 4000) {
            return "装甲输送车";
        }
        if (num >= 4201 && num <= 4400) {
            return "红箭73";
        }
        if (num >= 4401 && num <= 4600) {
            return "迫击炮";
        }
        if (num >= 4601 && num <= 4800) {
            return "火力点";
        }
        if (num == 65535) {
            return "手雷";
        }
        if (num == 65533) {
            return "地雷";
        }
        if (num == 65534) {
            return "爆炸物";
        }

        return "未知杀伤武器";
    }
}
