package com.ruoyi.battle.battle.context;

import com.ruoyi.battle.battle.domain.BattleEntity;
import com.ruoyi.battle.battle.thread.BaseMapDataThread;
import com.ruoyi.battle.battle.thread.SerialPortWriteDataThread;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.domain.SendDataMissionModel;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public interface BattleContext {

    /**
     * 实兵演习初始化
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 实兵演习开始
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 实兵演习结束
     * @throws Exception
     */
    void stop(boolean exist) throws Exception;

    /**
     * 实兵演习是否结束
     * @return
     */
    boolean isStop();

    /**
     * 返回实兵演习中的地图信息
     * @return
     */
    BaseMapEntity getMapEntity();

    /**
     * 返回实兵演习中的演习信息
     * @return
     */
    BattleEntity getBattleEntity();

    /**
     * 返回实兵演习中的马甲列表信息
     * @return
     */
    Map<Integer, ShootingVestModel> getVestEntityMap();

    /**
     * 返回实兵演习中的地图处理线程
     * @return
     */
    BaseMapDataThread getMapDataThread();

    /**
     * 返回实兵演习中的地图处理线程
     * @return
     */
    SerialPortWriteDataThread getSerialPortWriteDataThread();

    /**
     * 返回实兵演习中的任务队列
     * @return
     */
    BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue();
}
