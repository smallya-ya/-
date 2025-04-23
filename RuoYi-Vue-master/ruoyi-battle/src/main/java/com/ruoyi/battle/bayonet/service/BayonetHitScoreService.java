package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.bayonet.domain.BayonetHitAreaConfigEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetHitScoreEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetHitScoreVO;
import  com.ruoyi.battle.bayonet.mapper.BayonetHitScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetHitScoreService {

    @Autowired
    private BayonetHitScoreMapper bayonetHitScoreMapper;
    @Autowired
    private BayonetHitAreaConfigService bayonetHitAreaConfigService;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetHitScoreEntity entity) {
        bayonetHitScoreMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetHitScoreEntity entity) {
        bayonetHitScoreMapper.updateById(entity);
    }

    @Transactional(readOnly = true)
    public List<BayonetHitScoreEntity> queryByPlanId(Long id) {
        LambdaQueryWrapper<BayonetHitScoreEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(BayonetHitScoreEntity::getHitAreaId);
        wrapper.eq(BayonetHitScoreEntity::getPlanId, id);
        return bayonetHitScoreMapper.selectList(wrapper);
    }

    @Transactional(readOnly = true)
    public List<BayonetHitScoreVO> queryVoByPlanId(Long id) {
        LambdaQueryWrapper<BayonetHitScoreEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(BayonetHitScoreEntity::getHitAreaId);
        wrapper.eq(BayonetHitScoreEntity::getPlanId, id);
        List<BayonetHitScoreEntity> entityList = bayonetHitScoreMapper.selectList(wrapper);
        List<BayonetHitScoreVO> voList = new ArrayList<>(entityList.size());
        for (BayonetHitScoreEntity entity : entityList) {
            BayonetHitScoreVO vo = new BayonetHitScoreVO();
            BeanUtil.copyProperties(entity, vo);
            BayonetHitAreaConfigEntity hitArea = bayonetHitAreaConfigService.getById(entity.getHitAreaId());
            vo.setHitCode(hitArea.getHitCode());
            vo.setHitAreaName(hitArea.getHitAreaName());
            voList.add(vo);
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByPlanId(Long id) {
        LambdaQueryWrapper<BayonetHitScoreEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetHitScoreEntity::getPlanId, id);
        bayonetHitScoreMapper.delete(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return bayonetHitScoreMapper.deleteById(id);
    }
}
