package com.ruoyi.battle.battle.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.battle.battle.domain.BattleLogEntity;
import com.ruoyi.battle.battle.domain.BattleVestInfo;
import  com.ruoyi.battle.battle.mapper.BattleLogMapper;
import  com.ruoyi.battle.im.domain.RongCloudImInfoEntity;
import  com.ruoyi.battle.im.mapper.RongCloudImInfoMapper;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongjiasen
 */
@Service
public class ShootingAppInfoService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final Map<Integer, BlockingQueue<BattleLogEntity>> logMap = new ConcurrentHashMap<>();

    @Autowired
    private RongCloudImInfoMapper rongCloudImInfoMapper;
    @Autowired
    private BattleLogMapper battleLogMapper;

//    public void init(List<BattleTeamConfigEntity> battleTeamConfigEntityList) {
//        for (BattleTeamConfigEntity battleTeamConfigEntity : battleTeamConfigEntityList) {
//            if (Objects.nonNull(battleTeamConfigEntity.getStartNum()) && Objects.nonNull(battleTeamConfigEntity.getEndNum())) {
//                for (int num = battleTeamConfigEntity.getStartNum(); num <= battleTeamConfigEntity.getEndNum(); num++) {
//                    logMap.put(num, new LinkedBlockingQueue<>(100));
//                }
//            }
//        }
//    }

//    public void clear() {
//        logMap.forEach((num, queue) -> queue.clear());
//        logMap.clear();
//    }

//    public void log(BattleLogEntity log) {
//        logger.info("接收到新的演习日志");
//        long start = System.currentTimeMillis();
//        logMap.forEach((num, queue) -> queue.offer(log));
//        long stop = System.currentTimeMillis();
//        logger.info("保存演习日志处理时间{}", stop - start);
//    }

    public List<BattleLogEntity> getVestLog(int num) {
        if (null == BattleService.context) {
            return null;
        }
        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, BattleService.context.getBattleEntity().getId());
        logQuery.orderByAsc(BattleLogEntity::getTime);
        return battleLogMapper.selectList(logQuery);
    }

    @Transactional(readOnly = true)
    public BattleVestInfo getVestInfo(int num) {
        if (null == BattleService.context) {
            return null;
        }
        ShootingVestModel vest = BattleService.context.getVestEntityMap().get(num);
        LambdaQueryWrapper<RongCloudImInfoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RongCloudImInfoEntity::getNum, num);
        RongCloudImInfoEntity imInfo = rongCloudImInfoMapper.selectOne(wrapper);
        if (null != vest && null != imInfo) {
            BattleVestInfo result = BeanUtil.copyProperties(vest, BattleVestInfo.class);
            result.setToken(imInfo.getToken());
            result.setUserId(imInfo.getUserId());
            return result;
        }
        return null;
    }

    public BaseMapEntity getMapInfo() {
        if (null == BattleService.context) {
            return null;
        }
        BaseMapEntity map = BattleService.context.getMapEntity();
        map.setZoom(String.valueOf(Integer.parseInt(map.getZoom()) - 7));
        return map;
    }

    @Transactional(readOnly = true)
    public List<BattleVestInfo> getVestList(int num) {
        List<BattleVestInfo> resultList = new ArrayList<>();
        if (null == BattleService.context) {
            return resultList;
        }
        ShootingVestModel vest = BattleService.context.getVestEntityMap().get(num);
        if (null != vest) {
            for (ShootingVestModel vestModel : BattleService.context.getVestEntityMap().values()) {
                if (vest.getTeam().equals(vestModel.getTeam())) {
                    LambdaQueryWrapper<RongCloudImInfoEntity> wrapper = Wrappers.lambdaQuery();
                    wrapper.eq(RongCloudImInfoEntity::getNum, vestModel.getNum());
                    RongCloudImInfoEntity imInfo = rongCloudImInfoMapper.selectOne(wrapper);
                    if (null != imInfo) {
                        BattleVestInfo result = BeanUtil.copyProperties(vestModel, BattleVestInfo.class);
                        result.setToken(imInfo.getToken());
                        result.setUserId(imInfo.getUserId());
                        resultList.add(result);
                    }
                }
            }
        }
        return resultList;
    }
}
