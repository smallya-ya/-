package com.ruoyi.battle.bayonet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.bayonet.domain.BayonetBattleAchievementEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetBattleAchievementVO;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleAchievementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetBattleAchievementService {

    @Autowired
    private BayonetBattleAchievementMapper bayonetBattleAchievementMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetBattleAchievementEntity entity) {
        bayonetBattleAchievementMapper.insert(entity);
    }

    @Transactional(readOnly = true)
    public List<BayonetBattleAchievementVO> queryByBattleId(Long id) {
        return bayonetBattleAchievementMapper.queryByBattleId(id);
    }

    @Transactional(readOnly = true)
    public List<BayonetBattleAchievementEntity> getByBattleId(Long id) {
        LambdaQueryWrapper<BayonetBattleAchievementEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetBattleAchievementEntity::getBattleId, id);
        return bayonetBattleAchievementMapper.selectList(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByBattleId(Long id) {
        LambdaQueryWrapper<BayonetBattleAchievementEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetBattleAchievementEntity::getBattleId, id);
        bayonetBattleAchievementMapper.delete(wrapper);
    }
}
