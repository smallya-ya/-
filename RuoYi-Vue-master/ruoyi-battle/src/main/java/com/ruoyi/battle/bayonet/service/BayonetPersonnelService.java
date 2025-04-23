package com.ruoyi.battle.bayonet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelQueryDO;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelVO;
import  com.ruoyi.battle.bayonet.mapper.BayonetPersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetPersonnelService {

    @Autowired
    private BayonetPersonnelMapper bayonetPersonnelMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetPersonnelEntity entity) {
        bayonetPersonnelMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetPersonnelEntity entity) {
        bayonetPersonnelMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bayonetPersonnelMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        bayonetPersonnelMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUnitId(Long id) {
        LambdaQueryWrapper<BayonetPersonnelEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BayonetPersonnelEntity::getUnitId, id);
        bayonetPersonnelMapper.delete(wrapper);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetPersonnelVO> listByQuery(BayonetPersonnelQueryDO queryDO) {
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetPersonnelMapper.queryList(queryDO));
    }

    @Transactional(readOnly = true)
    public BayonetPersonnelVO getById(long id) {
        return bayonetPersonnelMapper.queryById(id);
    }
}
