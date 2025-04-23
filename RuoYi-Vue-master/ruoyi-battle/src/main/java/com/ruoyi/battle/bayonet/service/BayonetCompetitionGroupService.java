package com.ruoyi.battle.bayonet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionGroupEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelVO;
import  com.ruoyi.battle.bayonet.mapper.BayonetCompetitionGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetCompetitionGroupService {

    @Autowired
    private BayonetCompetitionGroupMapper bayonetCompetitionGroupMapper;
    @Autowired
    private BayonetPersonnelService bayonetPersonnelService;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetCompetitionGroupEntity entity) {
        bayonetCompetitionGroupMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByConfigId(Long id) {
        LambdaQueryWrapper<BayonetCompetitionGroupEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetCompetitionGroupEntity::getConfigId, id);
        bayonetCompetitionGroupMapper.delete(wrapper);
    }

    @Transactional(readOnly = true)
    public List<BayonetCompetitionGroupEntity> queryByConfigId(Long id) {
        LambdaQueryWrapper<BayonetCompetitionGroupEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetCompetitionGroupEntity::getConfigId, id);
        return bayonetCompetitionGroupMapper.selectList(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByGroupId(Long id) {
        return bayonetCompetitionGroupMapper.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BayonetPersonnelVO getByMemberId(long id) {
        return bayonetPersonnelService.getById(id);
    }
}
