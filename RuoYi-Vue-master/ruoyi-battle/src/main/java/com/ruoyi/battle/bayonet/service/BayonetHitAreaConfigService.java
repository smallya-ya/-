package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.domain.BayonetHitAreaConfigEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetHitAreaConfigQueryDO;
import  com.ruoyi.battle.bayonet.mapper.BayonetHitAreaConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetHitAreaConfigService {

    @Autowired
    private BayonetHitAreaConfigMapper bayonetHitAreaConfigMapper;

    @Transactional(readOnly = true)
    public List<BayonetHitAreaConfigEntity> list() {
        return bayonetHitAreaConfigMapper.selectList(null);
    }

    @Transactional(readOnly = true)
    public List<BayonetHitAreaConfigEntity> listByIds(List<Long> ids) {
        LambdaQueryWrapper<BayonetHitAreaConfigEntity> query = Wrappers.lambdaQuery();
        query.in(BayonetHitAreaConfigEntity::getId, ids);
        return bayonetHitAreaConfigMapper.selectList(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetHitAreaConfigEntity entity) {
        bayonetHitAreaConfigMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetHitAreaConfigEntity entity) {
        bayonetHitAreaConfigMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bayonetHitAreaConfigMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        bayonetHitAreaConfigMapper.deleteBatchIds(ids);
    }

    @Transactional(readOnly = true)
    public BayonetHitAreaConfigEntity getById(long id) {
        return bayonetHitAreaConfigMapper.selectById(id);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetHitAreaConfigEntity> listByQuery(BayonetHitAreaConfigQueryDO queryDO) {
        LambdaQueryWrapper<BayonetHitAreaConfigEntity> query = Wrappers.lambdaQuery();
        query.orderByAsc(BayonetHitAreaConfigEntity::getHitCode);
        query.eq(null != queryDO.getHitCode(), BayonetHitAreaConfigEntity::getHitCode, queryDO.getHitCode());
        query.eq(StrUtil.isNotBlank(queryDO.getHitAreaName()), BayonetHitAreaConfigEntity::getHitAreaName, queryDO.getHitAreaName());
        query.like(StrUtil.isNotBlank(queryDO.getRemark()), BayonetHitAreaConfigEntity::getRemark, queryDO.getRemark());
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetHitAreaConfigMapper.selectList(query));
    }
}
