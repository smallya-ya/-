package com.ruoyi.battle.battle.thread;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.context.BattleContext;
import  com.ruoyi.battle.battle.service.HandleService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.InstructionProcessQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * 实兵演习指令解析线程
 * @author hongjiasen
 */
public class InstructionProcessThread implements Runnable, InstructionProcessQueue {

    private Logger log = LoggerFactory.getLogger(getClass());
    private final int END_FRAME_LENGTH = 4;

    private BattleContext context;
    private BlockingQueue<byte[]> dataQueue;
    private HandleService handleService;
    protected byte[] leftData;

    public InstructionProcessThread(BattleContext context, BlockingQueue<byte[]> dataQueue, HandleService handleService) {
        this.context = context;
        this.dataQueue = dataQueue;
        this.handleService = handleService;
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
        if (null != leftData && leftData.length > Constant.BUFFER_SIZE) {
            log.error("[指令处理]：缓冲区数据过多，清空缓冲区");
            leftData = null;
        }

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
        int startFrame = dataStr.indexOf("ff0");
        int endFrame = dataStr.lastIndexOf("affa");

        if ((startFrame == 0 && endFrame < startFrame) || (startFrame < 0 && endFrame > 0) || (startFrame < 0 && endFrame < 0)) {
            leftData = ArrayUtil.addAll(leftData, buff);
            log.info("[指令处理]：未收到完整数据帧，[{}]暂时放入缓冲区，缓冲区数据[{}]", HexUtil.encodeHexStr(buff), HexUtil.encodeHexStr(buff));
        }

        // 包含完整的一条或多条数据帧指令
        if (startFrame == 0 && endFrame == dataStr.length() - END_FRAME_LENGTH) {
            log.info("[指令处理]：接收到下位机完整数据帧[{}]，开始进行处理", HexUtil.encodeHexStr(buff));
            handleService.handle(buff);
        }

        // 包含完整数据帧指令，尾部还有数据帧的一部分
        if (startFrame == 0 && endFrame > 0 && endFrame < dataStr.length() - END_FRAME_LENGTH) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame, endFrame / 2 + 2);
            leftData = Arrays.copyOfRange(buff, endFrame / 2 + 2, buff.length);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]暂时放入缓冲区，缓冲区数据[{}]"
                    , HexUtil.encodeHexStr(targetData)
                    , HexUtil.encodeHexStr(leftData)
                    , HexUtil.encodeHexStr(leftData));
            handleService.handle(targetData);
        }

        // 起始位置是其他的数据帧尾部，后面包含完整数据帧
        if (startFrame > 0 && endFrame == dataStr.length() - END_FRAME_LENGTH) {
            byte[] targetData = Arrays.copyOfRange(buff, startFrame / 2, buff.length);
            byte[] garbageData = Arrays.copyOfRange(buff, 0, startFrame / 2);
            log.info("[指令处理]：未收到完整数据帧，截取完整数据帧[{}]，剩余部分[{}]抛弃"
                    , HexUtil.encodeHexStr(targetData)
                    , HexUtil.encodeHexStr(garbageData));
            handleService.handle(targetData);
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
            handleService.handle(targetData);
        }
    }

    @Override
    public BlockingQueue<byte[]> getDataQueue() {
        return dataQueue;
    }
}
