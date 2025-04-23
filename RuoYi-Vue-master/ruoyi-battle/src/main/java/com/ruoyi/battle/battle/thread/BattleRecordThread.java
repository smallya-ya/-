package com.ruoyi.battle.battle.thread;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.battle.context.BattleContext;
import com.ruoyi.battle.battle.domain.BattleRecordDetailEntity;
import com.ruoyi.battle.battle.domain.BattleRecordEntity;
import  com.ruoyi.battle.battle.mapper.BattleRecordDetailMapper;
import  com.ruoyi.battle.battle.mapper.BattleRecordMapper;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hongjiasen
 */
public class BattleRecordThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private BattleContext context;
    private Long battleId;
    private Collection<ShootingVestModel> vestModels;
    private BattleRecordMapper battleRecordMapper;
    private BattleRecordDetailMapper battleRecordDetailMapper;
    private LocalDateTime lastFrontEndHeartBeatTime;
    private int lostFrontEndHeartBeatLimit;

    public BattleRecordThread(BattleContext context, Long battleId
            , Collection<ShootingVestModel> vestModels) {
        this.context = context;
        this.battleId = battleId;
        this.vestModels = vestModels;

        this.lastFrontEndHeartBeatTime = LocalDateTime.now();

        this.battleRecordMapper = SpringUtil.getBean(BattleRecordMapper.class);
        this.battleRecordDetailMapper = SpringUtil.getBean(BattleRecordDetailMapper.class);
        this.lostFrontEndHeartBeatLimit = Integer.parseInt(SpringUtil.getProperty("lostFrontEndHeartBeatLimit"));
        log.info("[前端心跳监控]：最大允许失去心跳时间{}秒", lostFrontEndHeartBeatLimit);
    }

    @Override
    public void run() {
        int index = 1;
        while (!this.context.isStop()) {
            List<BattleRecordDetailEntity> vestDataList = new ArrayList<>(vestModels.size());
            for (ShootingVestModel model : vestModels) {
                vestDataList.add(BeanUtil.copyProperties(model, BattleRecordDetailEntity.class));
            }
            if (CollectionUtil.isNotEmpty(vestDataList)) {
                BattleRecordEntity battleRecordEntity = BattleRecordEntity.BattleRecordEntityBuilder
                        .aBattleRecordEntity()
                        .battleId(battleId)
                        .index(index)
                        .time(LocalDateTime.now())
                        .build();
                battleRecordMapper.insert(battleRecordEntity);
                vestDataList.forEach(vest -> {
                    vest.setRecordId(battleRecordEntity.getId());
                    battleRecordDetailMapper.insert(vest);
                });
                log.info("[战场回放记录线程]：第{}次记录数据", index);
                index++;
            }

            // 心跳超时处理
//            if (lastFrontEndHeartBeatTime.plusSeconds(lostFrontEndHeartBeatLimit).isBefore(LocalDateTime.now())) {
//                log.error("[前端心跳监控]：上次前端请求时间为{}，超过允许失去心跳时间{}，结束本次演习"
//                        , lastFrontEndHeartBeatTime, lostFrontEndHeartBeatLimit);
//                try {
//                    BattleService battleService = SpringUtil.getBean(BattleService.class);
//                    battleService.endBattle();
//                    break;
//                } catch (SerialPortException e) {
//                    log.error("[战场回放记录线程]：任务结束", e);
//                    break;
//                } catch (InterruptedException e) {
//                    log.error("[战场回放记录线程]：任务结束", e);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }

            try {
                if (Constant.INDOOR_TYPE == context.getMapEntity().getType()) {
                    TimeUnit.MILLISECONDS.sleep(200);
                } else {
                    TimeUnit.SECONDS.sleep(15);
                }
            } catch (InterruptedException e) {
                log.error("[战场回放记录线程]：任务结束");
            }
        }
        log.info("[战场回放记录线程]：结束");
    }

    /**
     * 刷新心跳时间
     */
    public void refeshHeartbeatTime() {
//        log.info("[心跳管理]：刷新心跳，上次心跳时间{}", this.lastFrontEndHeartBeatTime);
        this.lastFrontEndHeartBeatTime = LocalDateTime.now();
    }
}
