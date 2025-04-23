package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.bean.BeanUtil;
import  com.ruoyi.battle.bayonet.context.BayonetBattleContext;
import  com.ruoyi.battle.bayonet.context.BayonetBattleMqttContext;
import  com.ruoyi.battle.bayonet.context.BayonetBattleSerialPortContext;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.serialport.service.SerialPortService;
import com.ruoyi.common.battle.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hongjiasen
 */
@Service
public class BayonetService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BayonetBattleService bayonetBattleService;
    @Autowired
    private BayonetCompetitionConfigService bayonetCompetitionConfigService;
    @Autowired
    private BayonetCompetitionGroupService bayonetCompetitionGroupService;
    @Autowired
    private BayonetScorePlanService bayonetScorePlanService;
    @Autowired
    private BayonetHitScoreService bayonetHitScoreService;
    @Autowired
    private BayonetHitAreaConfigService bayonetHitAreaConfigService;
    @Autowired
    private BayonetBattleAchievementService bayonetBattleAchievementService;
    @Autowired
    private SerialPortService serialPortService;

    public static BayonetBattleContext context;

    @PreDestroy
    public void destroy() throws Exception {
        if (Objects.nonNull(context)) {
            log.error("[演习管理]：异常关闭程序，终止正在进行的刺杀演习");
            this.stop(true);
        }
    }

    public BayonetBattleEntity start(Long id, int mode) throws Exception {
        log.error("[演习管理]：开始演习");
        BayonetBattleEntity battleEntity = new BayonetBattleEntity();
        battleEntity.setConfigId(id);
        battleEntity.setStartTime(LocalDateTime.now());
        bayonetBattleService.save(battleEntity);
        BayonetCompetitionConfigEntity config = bayonetCompetitionConfigService.getEntityById(id);
        List<BayonetCompetitionGroupVO> groups = new ArrayList<>(config.getGroups().size());
        config.getGroups().forEach(group -> {
            BayonetCompetitionGroupVO groupVO = new BayonetCompetitionGroupVO(group.getId(), config.getLimitedTime()
                    , config.getTotalHitNum()
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId1())
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId2()));
            groups.add(groupVO);
        });
        BayonetScorePlanEntity scorePlan = bayonetScorePlanService.getById(config.getScorePlanId());
        List<BayonetHitScoreEntity> hitScoreList = bayonetHitScoreService.queryByPlanId(scorePlan.getId());
        List<Long> ids = hitScoreList.stream().map(BayonetHitScoreEntity::getHitAreaId).collect(Collectors.toList());
        List<BayonetHitAreaConfigEntity> hitAreaList = bayonetHitAreaConfigService.listByIds(ids);

        if (Objects.nonNull(context)) {
            log.error("[演习管理]：当前存在正在进行的演习，进行关闭");
            this.stop(true);
        }

        if (Constant.BATTLE_SERIALPORT_MODE == mode) {
            context = new BayonetBattleSerialPortContext(battleEntity, serialPortService.getSerialPort()
                    , config, groups, scorePlan, hitScoreList, hitAreaList);
        } else {
            context = new BayonetBattleMqttContext(battleEntity, config, groups, scorePlan, hitScoreList, hitAreaList);
        }

        context.init();
        context.start();
        return battleEntity;
    }

    public void stop(boolean exist) throws Exception {
        if (Objects.nonNull(context)) {
            log.info("[演习管理]：结束刺杀演习");
            BayonetBattleEntity battleEntity = context.getBayonetBattle();
            battleEntity.setEndTime(LocalDateTime.now());
            bayonetBattleService.update(battleEntity);
            context.stop(true);
            context = null;
        } else {
            log.info("[演习管理]：当前无正在进行的演习");
        }
    }

    public List<BayonetResultVO> result(Long id) {
        // 演习信息
        BayonetBattleEntity battleEntity = bayonetBattleService.getById(id);
        // 演习配置信息
        BayonetCompetitionConfigEntity config = bayonetCompetitionConfigService.getEntityById(battleEntity.getConfigId());
        // 演习对抗小组信息
        List<BayonetCompetitionGroupVO> groups = new ArrayList<>(config.getGroups().size());
        config.getGroups().forEach(group -> {
            BayonetCompetitionGroupVO groupVO = new BayonetCompetitionGroupVO(group.getId(), config.getLimitedTime()
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId1())
                    , bayonetCompetitionGroupService.getByMemberId(group.getMemberId2()));
            groups.add(groupVO);
        });
        // 演习得分方案信息
        BayonetScorePlanEntity scorePlan = bayonetScorePlanService.getById(config.getScorePlanId());
        // 具体击中部位得分信息
        List<BayonetHitScoreEntity> hitScoreList = bayonetHitScoreService.queryByPlanId(scorePlan.getId());
        Map<Long, Integer> scoreMap = new HashMap<>(hitScoreList.size());
        hitScoreList.forEach(score -> scoreMap.put(score.getHitAreaId(), score.getScore()));
        // 演习击中信息
        List<BayonetBattleAchievementVO> achievementList = bayonetBattleAchievementService.queryByBattleId(id);
        // 不同人员击中信息
        Map<Integer, List<BayonetBattleAchievementVO>> achievementMap = achievementList.stream()
                .collect(Collectors.groupingBy(BayonetBattleAchievementVO::getNum));

        List<BayonetResultVO> resultVOList = new ArrayList<>(groups.size());
        for (BayonetCompetitionGroupVO group : groups) {
            BayonetPersonnelVO member1 = group.getMember1();
            BayonetPersonnelVO member2 = group.getMember2();
            BayonetPersonnelResultVO bpr1 = new BayonetPersonnelResultVO();
            BayonetPersonnelResultVO bpr2 = new BayonetPersonnelResultVO();
            BeanUtil.copyProperties(member1, bpr1);
            BeanUtil.copyProperties(member2, bpr2);
            int member1Score = 0;
            if (null != achievementMap.get(bpr1.getNum())) {
                for (BayonetBattleAchievementVO achievementVO : achievementMap.get(bpr1.getNum())) {
                    member1Score += scoreMap.get(achievementVO.getHitAreaId());
                }
                bpr1.setAchievement(achievementMap.get(bpr1.getNum()));
            } else {
                bpr1.setAchievement(Collections.emptyList());
            }

            bpr1.setScore(member1Score);
            int member2Score = 0;
            if (null != achievementMap.get(bpr2.getNum())) {
                for (BayonetBattleAchievementVO achievementVO : achievementMap.get(bpr2.getNum())) {
                    member2Score += scoreMap.get(achievementVO.getHitAreaId());
                }
                bpr2.setAchievement(achievementMap.get(bpr2.getNum()));
            } else {
                bpr2.setAchievement(Collections.emptyList());
            }
            bpr2.setScore(member2Score);

            bpr1.setWin(member1Score > member2Score);
            bpr2.setWin(member2Score > member1Score);
            BayonetResultVO resultVO = new BayonetResultVO(bpr1, bpr2);
            resultVOList.add(resultVO);
        }
        return resultVOList;
    }
}
