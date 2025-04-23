package com.ruoyi.battle.battle.thread;

import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author hongjiasen
 */
public class BattleRollThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Collection<ShootingVestModel> vestModelList;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public BattleRollThread(Collection<ShootingVestModel> vestModelList, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.vestModelList = vestModelList
                .stream()
                .sorted(Comparator.comparingInt(ShootingVestModel::getNum))
                .collect(Collectors.toList());
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[轮询线程]：启动");
        for (ShootingVestModel vestModel : vestModelList) {
            if (vestModel.getHp() == 0 && vestModel.getMode() == Constant.DRILL_MODE && vestModel.getManualLiveFlag() != 1) {
                log.info("[轮询线程]：{}号阵亡，跳过轮询", vestModel.getNum());
                continue;
            }
            SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                    .aSendDataMissionModel()
                    .vestNum(vestModel.getNum())
                    .isWait(true)
                    .priority(Constant.LOW_PRIORITY)
                    .data(DataFrameUtils.createPollingData(vestModel))
                    .dateTime(LocalDateTime.now())
                    .build();
            try {
                rollingMissionBlockingQueue.put(data);
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error("[轮询线程]：任务结束");
                continue;
            } catch (Exception e) {
                log.error("[轮询线程]：下发轮询指令时出现异常", e);
                continue;
            }
        }
        log.info("[轮询线程]：一次轮询结束");
    }

}
