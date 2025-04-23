package com.ruoyi.battle.bayonet.thread;

import  com.ruoyi.battle.bayonet.domain.BayonetVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

/**
 * @author hongjiasen
 */
public class BattleRollThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Collection<BayonetVestModel> vestModelList;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public BattleRollThread(Collection<BayonetVestModel> vestModelList, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.vestModelList = vestModelList;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[轮询线程]：启动");
        for (BayonetVestModel vestModel : vestModelList) {
            SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                    .aSendDataMissionModel()
                    .vestNum(vestModel.getNum())
                    .isWait(true)
                    .priority(Constant.LOW_PRIORITY)
                    .data(DataFrameUtils.createBayonetPollingData(vestModel))
                    .dateTime(LocalDateTime.now())
                    .build();
            try {
                rollingMissionBlockingQueue.put(data);
            } catch (InterruptedException e) {
                log.error("[轮询线程]：任务结束");
                continue;
            } catch (Exception e) {
                log.error("[轮询线程]：下发轮询指令时出现异常", e);
                continue;
            }
        }
    }
}
