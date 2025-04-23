package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleAchievementMapper;
import  com.ruoyi.battle.bayonet.mapper.BayonetCompetitionConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class BayonetCompetitionConfigService {

    @Autowired
    private BayonetCompetitionConfigMapper bayonetCompetitionConfigMapper;
    @Autowired
    private BayonetCompetitionGroupService bayonetCompetitionGroupService;
    @Autowired
    private BayonetBattleAchievementMapper bayonetBattleAchievementMapper;
    @Autowired
    private BayonetBattleService bayonetBattleService;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetCompetitionConfigBO bo) {
        BayonetCompetitionConfigEntity entity = new BayonetCompetitionConfigEntity();
        BeanUtil.copyProperties(bo, entity, "groups");

        bayonetCompetitionConfigMapper.insert(entity);

        for (BayonetCompetitionGroupBO groupBO : bo.getGroups()) {
            BayonetCompetitionGroupEntity group = new BayonetCompetitionGroupEntity(entity.getId()
                    , groupBO.getMember1().getPersonId(), groupBO.getMember2().getPersonId());
            bayonetCompetitionGroupService.save(group);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetCompetitionConfigBO bo) {
        BayonetCompetitionConfigEntity entity = new BayonetCompetitionConfigEntity();
        BeanUtil.copyProperties(bo, entity, "groups");

        bayonetCompetitionConfigMapper.updateById(entity);

        bayonetCompetitionGroupService.deleteByConfigId(entity.getId());
        for (BayonetCompetitionGroupBO groupBO : bo.getGroups()) {
            BayonetCompetitionGroupEntity group = new BayonetCompetitionGroupEntity(entity.getId()
                    , groupBO.getMember1().getPersonId(), groupBO.getMember2().getPersonId());
            bayonetCompetitionGroupService.save(group);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (bayonetBattleAchievementMapper.countByConfigId(id) > 0) {
            List<Long> ids = bayonetBattleService.queryByConfigId(id);
            LambdaQueryWrapper<BayonetBattleAchievementEntity> wrapper = Wrappers.lambdaQuery();
            wrapper.in(BayonetBattleAchievementEntity::getBattleId, ids);
            bayonetBattleAchievementMapper.delete(wrapper);
        }
        bayonetBattleService.deleteByConfigId(id);
        bayonetCompetitionGroupService.deleteByConfigId(id);
        bayonetCompetitionConfigMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByGroupId(Long groupId) {
        return bayonetCompetitionGroupService.deleteByGroupId(groupId);
    }

    @Transactional(readOnly = true)
    public BayonetCompetitionConfigEntity getEntityById(Long id) {
        BayonetCompetitionConfigEntity entity = bayonetCompetitionConfigMapper.selectById(id);
        List<BayonetCompetitionGroupEntity> groups = bayonetCompetitionGroupService.queryByConfigId(id);
        entity.setGroups(groups);
        return entity;
    }

    @Transactional(readOnly = true)
    public BayonetCompetitionConfigVO getById(Long id) {
        BayonetCompetitionConfigVO vo = bayonetCompetitionConfigMapper.getById(id);
        List<BayonetCompetitionGroupEntity> groupEntityList = bayonetCompetitionGroupService.queryByConfigId(vo.getId());
        List<BayonetCompetitionGroupVO> groups = new ArrayList<>(groupEntityList.size());
        groupEntityList.forEach(group -> {
            BayonetCompetitionGroupVO groupVO = new BayonetCompetitionGroupVO(group.getId(), vo.getLimitedTime()
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId1())
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId2()));
            groups.add(groupVO);
        });
        vo.setGroups(groups);
        return vo;
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetCompetitionConfigVO> listByQuery(BayonetCompetitionConfigQueryDO queryDO) {
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        PageInfo<BayonetCompetitionConfigVO> page = new PageInfo<>(bayonetCompetitionConfigMapper.queryList(queryDO));
        for (BayonetCompetitionConfigVO config : page.getList()) {
            List<BayonetCompetitionGroupEntity> groupEntityList = bayonetCompetitionGroupService.queryByConfigId(config.getId());
            List<BayonetCompetitionGroupVO> groups = new ArrayList<>(groupEntityList.size());
            groupEntityList.forEach(group -> {
                BayonetCompetitionGroupVO groupVO = new BayonetCompetitionGroupVO(group.getId(), config.getLimitedTime()
                        , bayonetCompetitionGroupService.getByMemberId(group.getMemberId1())
                        , bayonetCompetitionGroupService.getByMemberId(group.getMemberId2()));
                groups.add(groupVO);
            });
            config.setGroups(groups);
        }
        return page;
    }

    @Transactional(readOnly = true)
    public List<BayonetCompetitionGroupVO> listGroupById(Long id) {
        BayonetCompetitionConfigVO vo = bayonetCompetitionConfigMapper.getById(id);
        List<BayonetCompetitionGroupEntity> groupEntityList = bayonetCompetitionGroupService.queryByConfigId(id);
        List<BayonetCompetitionGroupVO> groups = new ArrayList<>(groupEntityList.size());
        groupEntityList.forEach(group -> {
            BayonetCompetitionGroupVO groupVO = new BayonetCompetitionGroupVO(group.getId(), vo.getLimitedTime()
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId1())
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId2()));
            groups.add(groupVO);
        });
        return groups;
    }
}
