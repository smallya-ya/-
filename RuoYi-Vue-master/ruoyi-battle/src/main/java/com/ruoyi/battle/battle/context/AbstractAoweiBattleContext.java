package com.ruoyi.battle.battle.context;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleBaseConfigEntity;
import com.ruoyi.battle.battle.domain.BattleEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity2;
import com.ruoyi.battle.battle.thread.AoweiMapDataThread;
import com.ruoyi.battle.battle.thread.BattleRecordThread;
import com.ruoyi.battle.battle.thread.ForceOnlineThread;
import com.ruoyi.battle.battle.thread.SerialPortWriteDataThread;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author hongjiasen
 */
public class AbstractAoweiBattleContext implements AoweiShootingBattleContext{

    // 需要外部传入的属性

    protected BattleEntity battleEntity;
    protected BattleBaseConfigEntity battleBaseConfigEntity;
    protected List<BattleTeamConfigEntity> battleTeamConfigEntityList;
    protected List<BattleTeamConfigEntity2> battleTeamConfigEntityList2;
    protected BaseMapEntity mapEntity;

    // 需要内部自己初始化的属性

    protected Map<Integer, ShootingVestModel> vestEntityMap;
    protected ScheduledExecutorService ses;
    protected ForceOnlineThread forceOnlineThread;

    @Override
    public void init() throws Exception {
        forceOnlineThread = new ForceOnlineThread();
        ses = Executors.newScheduledThreadPool(1);
        if (Constant.TRUE.equals(SpringUtil.getProperty(Constant.FORCE_UP))) {
            ses.schedule(forceOnlineThread, 2, TimeUnit.MINUTES);
        }
        // 初始化马甲信息列表
        vestEntityMap = new ConcurrentHashMap<>();
        if (CollUtil.isNotEmpty(battleTeamConfigEntityList)) {
            for (BattleTeamConfigEntity battleTeamConfigEntity : battleTeamConfigEntityList) {
                if (Objects.nonNull(battleTeamConfigEntity.getStartNum()) && Objects.nonNull(battleTeamConfigEntity.getEndNum())) {
                    for (int num = battleTeamConfigEntity.getStartNum(); num <= battleTeamConfigEntity.getEndNum(); num++) {
                        ShootingVestModel model = ShootingVestModel.ShootingVestModelBuilder
                                .aShootingVestModel()
                                .name("")
                                .team(battleTeamConfigEntity.getTeam())
                                .num(num)
                                .hp(100)
                                .weapon1(battleTeamConfigEntity.getPrimaryWeapon())
                                .ammo1(0)
                                .weapon2(battleTeamConfigEntity.getSecondaryWeapon())
                                .ammo2(0)
                                .lat(BigDecimal.ZERO)
                                .lng(BigDecimal.ZERO)
                                .mode(battleBaseConfigEntity.getMode())
                                .status(Constant.OFF_LINE_STATUS)
                                .realStatus(Constant.OFF_LINE_STATUS)
                                .build();
                        vestEntityMap.put(num, model);
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(battleTeamConfigEntityList2)) {
            for (BattleTeamConfigEntity2 battleTeamConfigEntity2 : battleTeamConfigEntityList2) {
                if (Objects.nonNull(battleTeamConfigEntity2.getNums())) {
                    String[] numArr = battleTeamConfigEntity2.getNums().split(",");
                    for (String num : numArr) {
                        ShootingVestModel model = ShootingVestModel.ShootingVestModelBuilder
                                .aShootingVestModel()
                                .name("")
                                .team(battleTeamConfigEntity2.getTeam())
                                .num(Integer.parseInt(num))
                                .hp(100)
                                .weapon1(battleTeamConfigEntity2.getPrimaryWeapon())
                                .ammo1(0)
                                .weapon2(battleTeamConfigEntity2.getSecondaryWeapon())
                                .ammo2(0)
                                .lat(BigDecimal.ZERO)
                                .lng(BigDecimal.ZERO)
                                .mode(battleBaseConfigEntity.getMode())
                                .status(Constant.OFF_LINE_STATUS)
                                .realStatus(Constant.OFF_LINE_STATUS)
                                .build();
                        vestEntityMap.put(Integer.parseInt(num), model);
                    }
                }
            }
        }
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop(boolean exist) throws Exception {
        ses.shutdown();
    }

    @Override
    public boolean isStop() {
        return false;
    }

    @Override
    public BattleEntity getBattleEntity() {
        return this.battleEntity;
    }

    @Override
    public Map<Integer, ShootingVestModel> getVestEntityMap() {
        return this.vestEntityMap;
    }

    @Override
    public AoweiMapDataThread getMapDataThread() {
        return null;
    }

    @Override
    public SerialPortWriteDataThread getSerialPortWriteDataThread() {
        return null;
    }

    @Override
    public BattleRecordThread getBattleRecordThread() {
        return null;
    }

    @Override
    public BaseMapEntity getMapEntity() {
        return this.mapEntity;
    }

    @Override
    public BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue() {
        return null;
    }
}
