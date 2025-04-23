package com.ruoyi.battle.bayonet.thread;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.battle.handler.AbstractDataHandler;
import  com.ruoyi.battle.bayonet.Constant;
import  com.ruoyi.battle.bayonet.context.BayonetBattleContext;
import  com.ruoyi.battle.bayonet.controller.BayonetBattleWebSocketController;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleGroupRecordMapper;
import  com.ruoyi.battle.bayonet.mapper.BayonetPersonnelMapper;
import  com.ruoyi.battle.bayonet.service.BayonetBattleAchievementService;
import com.ruoyi.common.battle.InstructionProcessQueue;
import com.ruoyi.common.battle.exception.DataHandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class InstructionProcessThread implements Runnable, AbstractDataHandler, InstructionProcessQueue {

    private Logger log = LoggerFactory.getLogger(getClass());
    private final int END_FRAME_LENGTH = 4;
    private Pattern pattern = Pattern.compile("^(030000)\\w{18}(affa)$");

    private BayonetBattleContext context;
    private Map<Integer, BayonetHitAreaConfigEntity> hitAreaMap;
    private Map<Long, BayonetHitScoreEntity> hitAreaScoreMap;
    private BlockingQueue<byte[]> dataQueue;
    protected byte[] leftData;

    public InstructionProcessThread(BayonetBattleContext context, BlockingQueue<byte[]> dataQueue
            , Map<Integer, BayonetHitAreaConfigEntity> hitAreaMap, Map<Long, BayonetHitScoreEntity> hitAreaScoreMap) {
        this.context = context;
        this.dataQueue = dataQueue;
        this.hitAreaMap = hitAreaMap;
        this.hitAreaScoreMap = hitAreaScoreMap;
    }

    @Override
    public void run() {
        while (!this.context.isStop()) {
            try {
                this.process(dataQueue.take());
            } catch (InterruptedException e) {
                log.error("[指令处理]：任务结束");
            } catch (Exception e) {
                log.error("[指令处理]：处理下位机指令时发生异常", e);
            }
        }
        dataQueue.clear();
        log.info("[指令处理]：结束");
    }

    private void process(byte[] data) {
        byte[] buff = null;
        if (null != leftData) {
            buff = new byte[leftData.length + data.length];
            System.arraycopy(leftData, 0, buff, 0, leftData.length);
            System.arraycopy(data, 0, buff, leftData.length, data.length);
            log.info("[指令处理]：缓冲区存在数据，合并后数据帧为[{}]，合并后继续解析，并清空缓冲区", HexUtil.encodeHexStr(buff));
            leftData = null;
        } else {
            buff = data;
        }

        String dataStr = HexUtil.encodeHexStr(buff);
        int startFrame = dataStr.indexOf("030000");
        int endFrame = dataStr.lastIndexOf("affa");

        if ((startFrame == 0 && endFrame < startFrame) || (startFrame < 0 && endFrame > 0) || (startFrame < 0 && endFrame < 0)) {
            leftData = ArrayUtil.addAll(leftData, buff);
            log.info("[指令处理]：未收到完整数据帧，[{}]暂时放入缓冲区，缓冲区数据[{}]", HexUtil.encodeHexStr(buff), HexUtil.encodeHexStr(buff));
        }

        // 包含完整的一条或多条数据帧指令
        if (startFrame == 0 && endFrame == dataStr.length() - END_FRAME_LENGTH) {
            log.info("[指令处理]：接收到下位机完整数据帧[{}]，开始进行处理", HexUtil.encodeHexStr(buff));
            this.handle(buff);
        }

        // 包含完整数据帧指令，尾部还有数据帧的一部分
        if (startFrame == 0 && endFrame > 0 && endFrame < dataStr.length() - END_FRAME_LENGTH) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame, endFrame / 2 + 2);
            leftData = Arrays.copyOfRange(buff, endFrame / 2 + 2, buff.length);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]暂时放入缓冲区，缓冲区数据[{}]"
                    , HexUtil.encodeHexStr(targetData)
                    , HexUtil.encodeHexStr(leftData)
                    , HexUtil.encodeHexStr(leftData));
            this.handle(targetData);
        }

        // 起始位置是其他的数据帧尾部，后面包含完整数据帧
        if (startFrame > 0 && endFrame == dataStr.length() - END_FRAME_LENGTH) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame / 2, buff.length);
            byte[] garbageData = Arrays.copyOfRange(buff, 0, startFrame / 2);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]抛弃"
                    , HexUtil.encodeHexStr(targetData)
                    , HexUtil.encodeHexStr(garbageData));
            this.handle(targetData);
        }

        // 前后包含其余数据帧部分，中间包含完整数据帧
        if (startFrame > 0 && startFrame < endFrame && endFrame < dataStr.length() - END_FRAME_LENGTH) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame / 2, endFrame / 2 + 2);
            leftData = Arrays.copyOfRange(buff, endFrame / 2 + 2, buff.length);
            byte[] garbageData = Arrays.copyOfRange(buff, 0, startFrame / 2);
            log.info("[指令处理]：未收到完整数据帧，丢弃数据帧[{}]，截取完整数据帧[{}]，剩余部分[{}]暂时放入缓冲区，缓冲区数据[{}]"
                    , HexUtil.encodeHexStr(garbageData)
                    , HexUtil.encodeHexStr(targetData)
                    , HexUtil.encodeHexStr(leftData)
                    , HexUtil.encodeHexStr(leftData));
            this.handle(targetData);
        }
    }

    @Override
    public void handle(byte[] data) {
        String dataStr = HexUtil.encodeHexStr(data);
        int startFrame = 0;
        int endFrame;
        while (startFrame < dataStr.length()) {
            startFrame = dataStr.indexOf("030000", startFrame);
            endFrame = dataStr.indexOf("affa", startFrame);
            byte[] targetData = Arrays.copyOfRange(data, startFrame / 2, endFrame / 2 + 2);
            this.handle0(targetData);
            startFrame = endFrame + 4;
        }
    }

    private void handle0(byte[] data) {
        String str = HexUtil.encodeHexStr(data);
        log.info("[指令处理]：开始处理数据帧[{}]", str);
        if (!pattern.matcher(str).matches()) {
            throw new DataHandleException("[指令处理]：数据格式校验不通过，丢弃该数据帧" + str);
        }
        if(!dataVerification(data)) {
            throw new DataHandleException("[指令处理]：数据帧检验和验证不通过，丢弃该数据帧" + str);
        }
        int vestNum = Integer.parseInt(str.substring(10, 14), 16);
        int attacker = Integer.parseInt(str.substring(6, 10), 16);

        // 快枪模式	没时间限制，最先达到规定枪数者，胜利
        // 五枪三胜	规定时间，规定总枪数，先完成目标枪数者胜利
        // 对抗组里先达到规定枪数后不再处理后续击中指令
        if (this.context.getConfig().getType() == Constant.KUAIQIANG_MODE
                || this.context.getConfig().getType() == Constant.WUQIANGSANSHENG_MODE) {
            int m1HitNum = this.context.getVestEntityMap().get(attacker).getNumOfTotalHits();
            int m2HitNum = this.context.getVestEntityMap().get(vestNum).getNumOfTotalHits();
            if (m1HitNum == context.getConfig().getTotalHitNum() || m2HitNum == context.getConfig().getTotalHitNum()) {
                log.info("[指令处理]：该对抗组{}号击中{}次，{}号击中{}次，已分出胜负，不再处理后续击中指令", attacker, m1HitNum, vestNum, m2HitNum);
                return;
            }
        }

        BayonetVestModel vestEntity = this.context.getVestEntityMap().get(attacker);
        if (Objects.isNull(vestEntity)) {
            throw new DataHandleException(String.format("[指令处理]：收到%s号上报 未找到%s号数据，丢弃该数据帧%s", attacker, attacker, str));
        }

        int hitArea = Integer.parseInt(str.substring(14, 16), 16);
        int hitNum = Integer.parseInt(str.substring(16, 18), 16);
        int hitStrength = Integer.parseInt(str.substring(18, 22), 16);
        log.info("[指令处理]：{}号被{}号击中{}部位，当前该部位被击中{}次，力量{}", vestNum, attacker, this.hitAreaMap.get(hitArea).getHitAreaName(), hitNum, hitStrength);
        vestEntity.setCurrentHitArea(hitArea);
        for (HitAreaLogDetail log : vestEntity.getHitAreaLog()) {
            if(this.hitAreaMap.get(hitArea).getHitAreaName().equals(log.getHitPart())) {
                log.hitTimesGetAndIncrement();
                log.hitScoreGetAndIncrement(hitAreaScoreMap.get(this.hitAreaMap.get(hitArea).getId()).getScore());
                break;
            }
        }
        vestEntity.getAndIncrement();
        vestEntity.addScore(hitAreaScoreMap.get(this.hitAreaMap.get(hitArea).getId()).getScore());

        if (this.context.getConfig().getType() == Constant.KUAIQIANG_MODE
                || this.context.getConfig().getType() == Constant.WUQIANGSANSHENG_MODE) {
            if (vestEntity.getNumOfTotalHits().equals(context.getConfig().getTotalHitNum())) {
                LambdaQueryWrapper<BayonetBattleGroupRecordEntity> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(BayonetBattleGroupRecordEntity::getBattleId, context.getBayonetBattle().getId());
                List<BayonetBattleGroupRecordEntity> records = SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).selectList(wrapper);
                for (BayonetBattleGroupRecordEntity record : records) {
                    BayonetPersonnelVO m1 = SpringUtil.getBean(BayonetPersonnelMapper.class).queryById(record.getMemberId1());
                    BayonetPersonnelVO m2 = SpringUtil.getBean(BayonetPersonnelMapper.class).queryById(record.getMemberId2());
                    if (m1.getNum() == vestEntity.getNum()) {
                        record.setWinner(record.getMemberId1());
                        SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                        break;
                    }
                    if (m2.getNum() == vestEntity.getNum()) {
                        record.setWinner(record.getMemberId2());
                        SpringUtil.getBean(BayonetBattleGroupRecordMapper.class).updateById(record);
                        break;
                    }
                }
            }
        }

        BayonetBattleAchievementEntity achievement = new BayonetBattleAchievementEntity();
        achievement.setBattleId(this.context.getBayonetBattle().getId());
        achievement.setBattleGroupRecordId(vestEntity.getBattleGroupRecordId());
        achievement.setAttackerId(this.context.getVestEntityMap().get(attacker).getId());
        achievement.setOpponentId(this.context.getVestEntityMap().get(vestNum).getId());
        achievement.setHitAreaId(this.hitAreaMap.get(hitArea).getId());
        achievement.setHitTimes(hitNum);
        achievement.setHitStrength(hitStrength);
        // 计算击中相对开始发生的秒数
        Duration duration = Duration.between(context.getBayonetBattle().getStartTime(), LocalDateTime.now());
        achievement.setHappenTime(duration.toMillis() / 1000);
        SpringUtil.getBean(BayonetBattleAchievementService.class).save(achievement);

        vestEntity.getBattleLog().add(new BayonetVestLogVO(achievement.getId()
                , this.buildBattleLogMsg(achievement.getHappenTime(), this.hitAreaMap.get(hitArea).getHitAreaName(), hitStrength)));

        try {
            BayonetData bayonetData = new BayonetData();
            bayonetData.setNum(attacker);
            bayonetData.setName(this.context.getVestEntityMap().get(attacker).getName());
            bayonetData.setVestList(this.context.getVestEntityMap().values());
            BayonetBattleWebSocketController.notice(bayonetData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BlockingQueue<byte[]> getDataQueue() {
        return dataQueue;
    }

    private String buildBattleLogMsg(long happenTime, String hitArea, int hitStrength) {
        return String.format("%d秒击中%s%d公斤", happenTime, hitArea, hitStrength);
    }
}
