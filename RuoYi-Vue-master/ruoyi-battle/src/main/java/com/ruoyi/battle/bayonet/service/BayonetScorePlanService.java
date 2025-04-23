package com.ruoyi.battle.bayonet.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.domain.BayonetHitScoreEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanQueryDO;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanVO;
import  com.ruoyi.battle.bayonet.mapper.BayonetScorePlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetScorePlanService {

    @Autowired
    private BayonetScorePlanMapper bayonetScorePlanMapper;
    @Autowired
    private BayonetHitScoreService bayonetHitScoreService;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetScorePlanEntity entity) {
        bayonetScorePlanMapper.insert(entity);

        for (BayonetHitScoreEntity score : entity.getHitScoreList()) {
            score.setPlanId(entity.getId());
            bayonetHitScoreService.save(score);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetScorePlanEntity entity) {
        bayonetScorePlanMapper.updateById(entity);

        bayonetHitScoreService.deleteByPlanId(entity.getId());
        for (BayonetHitScoreEntity score : entity.getHitScoreList()) {
            score.setPlanId(entity.getId());
            bayonetHitScoreService.save(score);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteItem(Long id) {
        return bayonetHitScoreService.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bayonetHitScoreService.deleteByPlanId(id);
        bayonetScorePlanMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        bayonetScorePlanMapper.deleteBatchIds(ids);
    }

    @Transactional(readOnly = true)
    public BayonetScorePlanEntity getById(long id) {
        return bayonetScorePlanMapper.selectById(id);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetScorePlanVO> listByQuery(BayonetScorePlanQueryDO queryDO) {
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetScorePlanMapper.queryList(queryDO));
    }
}
