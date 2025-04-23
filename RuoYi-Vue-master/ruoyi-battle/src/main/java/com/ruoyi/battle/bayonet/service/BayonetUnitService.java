package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.domain.BayonetUnitEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetUnitQueryDO;
import  com.ruoyi.battle.bayonet.mapper.BayonetUnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetUnitService {

    @Autowired
    private BayonetUnitMapper bayonetUnitMapper;
    @Autowired
    private BayonetPersonnelService bayonetPersonnelService;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetUnitEntity entity) {
        bayonetUnitMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetUnitEntity entity) {
        bayonetUnitMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bayonetPersonnelService.deleteByUnitId(id);
        bayonetUnitMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        bayonetUnitMapper.deleteBatchIds(ids);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetUnitEntity> listByQuery(BayonetUnitQueryDO queryDO) {
        LambdaQueryWrapper<BayonetUnitEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(BayonetUnitEntity::getCreateTime);
        query.like(StrUtil.isNotBlank(queryDO.getUnitName()), BayonetUnitEntity::getUnitName, queryDO.getUnitName());
        query.like(StrUtil.isNotBlank(queryDO.getLeader()), BayonetUnitEntity::getLeader, queryDO.getLeader());
        query.eq(null != queryDO.getQuantity(), BayonetUnitEntity::getQuantity, queryDO.getQuantity());
        query.like(StrUtil.isNotBlank(queryDO.getRemark()), BayonetUnitEntity::getRemark, queryDO.getRemark());
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetUnitMapper.selectList(query));
    }

    @Transactional(readOnly = true)
    public BayonetUnitEntity getById(long id) {
        return bayonetUnitMapper.selectById(id);
    }
}
