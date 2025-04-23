package com.ruoyi.common.battle;

import java.util.concurrent.BlockingQueue;

/**
 * 用于定义获取指令处理队列
 * @author hongjiasen
 */
public interface InstructionProcessQueue {

    BlockingQueue<byte[]> getDataQueue();
}
