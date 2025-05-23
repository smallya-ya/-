package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;

/**
 * @author hongjiasen
 */
public interface AbstractDataHandler {

    /**
     * 数据和校验：AFFA前一位的数值=前边所有数据的和（一个字节，高位去掉）
     * @param data 数据帧
     * @return
     */
    default boolean dataVerification(byte[] data) {
        String dataStr = HexUtil.encodeHexStr(data);
        int position = dataStr.indexOf("affa") / 2 - 1;
        byte dataVer = (byte) 0;
        for (int i = 0; i < position; i++) {
            dataVer += data[i];
        }
        return dataVer == data[position];
    }

    /**
     * 实际处理数据帧方法，由具体实现类实现
     * @param data
     */
    void handle(byte[] data);
}
