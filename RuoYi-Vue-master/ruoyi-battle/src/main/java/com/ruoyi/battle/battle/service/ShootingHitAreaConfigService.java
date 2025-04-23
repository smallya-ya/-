package com.ruoyi.battle.battle.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.battle.battle.domain.ShootingHitAreaConfigEntity;
import com.ruoyi.battle.battle.domain.ShootingHitAreaConfigQueryDO;
import  com.ruoyi.battle.battle.mapper.ShootingHitAreaConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class ShootingHitAreaConfigService {

    @Autowired
    private ShootingHitAreaConfigMapper shootingHitAreaConfigMapper;

    @Transactional(readOnly = true)
    public List<ShootingHitAreaConfigEntity> list() {
        return shootingHitAreaConfigMapper.selectList(null);
    }

    @Transactional(readOnly = true)
    public List<ShootingHitAreaConfigEntity> listByIds(List<Long> ids) {
        LambdaQueryWrapper<ShootingHitAreaConfigEntity> query = Wrappers.lambdaQuery();
        query.in(ShootingHitAreaConfigEntity::getId, ids);
        return shootingHitAreaConfigMapper.selectList(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(ShootingHitAreaConfigEntity entity) {
        shootingHitAreaConfigMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ShootingHitAreaConfigEntity entity) {
        shootingHitAreaConfigMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        shootingHitAreaConfigMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        shootingHitAreaConfigMapper.deleteBatchIds(ids);
    }

    @Transactional(readOnly = true)
    public ShootingHitAreaConfigEntity getById(long id) {
        return shootingHitAreaConfigMapper.selectById(id);
    }

    @Transactional(readOnly = true)
    public PageInfo<ShootingHitAreaConfigEntity> listByQuery(ShootingHitAreaConfigQueryDO queryDO) {
        LambdaQueryWrapper<ShootingHitAreaConfigEntity> query = Wrappers.lambdaQuery();
        query.orderByAsc(ShootingHitAreaConfigEntity::getHitCode);
        query.eq(null != queryDO.getHitCode(), ShootingHitAreaConfigEntity::getHitCode, queryDO.getHitCode());
        query.eq(StrUtil.isNotBlank(queryDO.getHitAreaName()), ShootingHitAreaConfigEntity::getHitAreaName, queryDO.getHitAreaName());
        query.like(StrUtil.isNotBlank(queryDO.getRemark()), ShootingHitAreaConfigEntity::getRemark, queryDO.getRemark());
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(shootingHitAreaConfigMapper.selectList(query));
    }
}
