package com.ruoyi.battle.battle.thread;

import  com.ruoyi.battle.battle.context.ShootingBattleContext;
import  com.ruoyi.battle.battle.service.HandleService;

import java.util.concurrent.BlockingQueue;

/**
 * @author hongjiasen
 */
public class IndoorLocationThread extends InstructionProcessThread {

    public IndoorLocationThread(ShootingBattleContext context, BlockingQueue<byte[]> dataQueue, HandleService handleService) {
        super(context, dataQueue, handleService);
    }
}
