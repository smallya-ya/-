package com.ruoyi.battle.battle.context;

import com.ruoyi.battle.battle.thread.BattleRecordThread;
import com.ruoyi.battle.battle.thread.SerialPortWriteDataThread;

/**
 * 射击演习上下文环境，包含马甲、地图、演习记录、各种线程集中管理等
 * @author hongjiasen
 */
public interface ShootingBattleContext extends BattleContext {

    /**
     * 返回实兵演习中的演习记录线程
     * @return
     */
    BattleRecordThread getBattleRecordThread();

    @Override
    default SerialPortWriteDataThread getSerialPortWriteDataThread() {
        return null;
    }
}
