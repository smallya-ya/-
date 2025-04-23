package com.ruoyi.battle.battle.context;

import com.ruoyi.battle.battle.thread.BattleRecordThread;

/**
 * @author hongjiasen
 */
public interface AoweiShootingBattleContext extends BattleContext {

    /**
     * 返回实兵演习中的演习记录线程
     * @return
     */
    BattleRecordThread getBattleRecordThread();
}
