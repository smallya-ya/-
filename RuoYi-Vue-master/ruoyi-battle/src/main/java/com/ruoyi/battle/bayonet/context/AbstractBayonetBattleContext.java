package com.ruoyi.battle.bayonet.context;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.bayonet.Constant;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleGroupRecordMapper;
import  com.ruoyi.battle.bayonet.mapper.BayonetPersonnelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongjiasen
 */
public class AbstractBayonetBattleContext implements BayonetBattleContext {

    /**
     * 刺杀配置方案
     */
    protected BayonetCompetitionConfigEntity config;

    /**
     * 刺杀对抗小组列表
     */
    protected List<BayonetCompetitionGroupVO> groupList;

    /**
     * 刺杀分值方案
     */
    protected BayonetScorePlanEntity scorePlan;

    /**
     * 刺杀方案击中部位分值列表
     */
    protected List<BayonetHitScoreEntity> hitScoreList;

    /**
     * 击中部位配置列表
     */
    protected List<BayonetHitAreaConfigEntity> hitAreaList;

    /**
     * 演习信息
     */
    protected BayonetBattleEntity battleEntity;

    protected Map<Integer, BayonetVestModel> vestEntityMap;
    protected Map<Integer, BayonetHitAreaConfigEntity> hitAreaMap;
    protected Map<Long, BayonetHitScoreEntity> hitAreaScoreMap = new HashMap<>();

    @Override
    public void init() throws Exception {
        // 初始化刺杀护具信息列表
        vestEntityMap = new ConcurrentHashMap<>(groupList.size() * 2);
        for (BayonetCompetitionGroupVO group : groupList) {
            BayonetBattleGroupRecordEntity record = new BayonetBattleGroupRecordEntity();
            record.setBattleId(battleEntity.getId());
            record.setMemberId1(group.getMember1().getId());
            record.setMemberId2(group.getMember2().getId());
            SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).insert(record);

            BayonetVestModel m1 = BayonetVestModel.BayonetVestModelBuilder
                    .aBayonetVestModel()
                    .id(group.getMember1().getId())
                    .num(group.getMember1().getNum())
                    .name(group.getMember1().getName())
                    .team(group.getMember1().getUnitName())
                    .battleGroupRecordId(record.getId())
                    .opponentNum(group.getMember2().getNum())
                    .build();
            hitAreaList.forEach(hitArea -> m1.getHitAreaLog().add(new HitAreaLogDetail(hitArea.getHitAreaName(), new AtomicInteger(0), new AtomicInteger(0))));
            vestEntityMap.put(group.getMember1().getNum(), m1);
            BayonetVestModel m2 = BayonetVestModel.BayonetVestModelBuilder
                    .aBayonetVestModel()
                    .id(group.getMember2().getId())
                    .num(group.getMember2().getNum())
                    .name(group.getMember2().getName())
                    .team(group.getMember2().getUnitName())
                    .battleGroupRecordId(record.getId())
                    .opponentNum(group.getMember1().getNum())
                    .build();
            hitAreaList.forEach(hitArea -> m2.getHitAreaLog().add(new HitAreaLogDetail(hitArea.getHitAreaName(), new AtomicInteger(0), new AtomicInteger(0))));
            vestEntityMap.put(group.getMember2().getNum(), m2);
        }
        hitAreaMap = new ConcurrentHashMap<>(hitAreaList.size());
        for (BayonetHitAreaConfigEntity hitArea : hitAreaList) {
            hitAreaMap.put(hitArea.getHitCode(), hitArea);
        }
        for (BayonetHitScoreEntity score : hitScoreList) {
            hitAreaScoreMap.put(score.getHitAreaId(), score);
        }
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop(boolean exist) throws Exception {
        if (vestEntityMap.isEmpty()) {
            return;
        }
        LambdaQueryWrapper<BayonetBattleGroupRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetBattleGroupRecordEntity::getBattleId, battleEntity.getId());
        List<BayonetBattleGroupRecordEntity> records = SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).selectList(wrapper);
        for (BayonetBattleGroupRecordEntity record : records) {
            if (null != record.getWinner()) {
                continue;
            }
            BayonetPersonnelVO m1 = SpringUtil.getBean(BayonetPersonnelMapper.class).queryById(record.getMemberId1());
            BayonetPersonnelVO m2 = SpringUtil.getBean(BayonetPersonnelMapper.class).queryById(record.getMemberId2());
            int m1Score = this.vestEntityMap.get(m1.getNum()).getTotalScore();
            int m2Score = this.vestEntityMap.get(m2.getNum()).getTotalScore();
            int m1HitNum = this.vestEntityMap.get(m1.getNum()).getNumOfTotalHits();
            int m2HitNum = this.vestEntityMap.get(m2.getNum()).getNumOfTotalHits();
            switch (config.getType()) {
                case Constant.SHENGFEN_MODE:
                    record.setWinner(m1Score > m2Score ? record.getMemberId1() : record.getMemberId2());
                    SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                    break;
                case Constant.SHENGQIANG_MODE:
                    record.setWinner(m1HitNum > m2HitNum ? record.getMemberId1() : record.getMemberId2());
                    SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                    break;
                case Constant.KUAIQIANG_MODE:
                    record.setWinner(m1HitNum == config.getTotalHitNum() ? record.getMemberId1() : record.getMemberId2());
                    SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                    break;
                case Constant.WUQIANGSANSHENG_MODE:
                    if (m1HitNum == config.getTotalHitNum()) {
                        record.setWinner(record.getMemberId1());
                    } else if (m2HitNum == config.getTotalHitNum()) {
                        record.setWinner(record.getMemberId2());
                    } else {
                        record.setWinner(m1HitNum > m2HitNum ? record.getMemberId1() : record.getMemberId2());
                    }
                    SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean isStop() {
        return false;
    }

    @Override
    public Map<Integer, BayonetVestModel> getVestEntityMap() {
        return this.vestEntityMap;
    }

    @Override
    public BayonetBattleEntity getBayonetBattle() {
        return this.battleEntity;
    }

    @Override
    public List<BayonetCompetitionGroupVO> getGroup() {
        return this.groupList;
    }

    @Override
    public BayonetCompetitionConfigEntity getConfig() {
        return this.config;
    }
}
