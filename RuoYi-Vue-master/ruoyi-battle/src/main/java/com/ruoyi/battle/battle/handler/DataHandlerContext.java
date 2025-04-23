package com.ruoyi.battle.battle.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongjiasen
 */
public class DataHandlerContext {

    private static Map<Byte, CommonDataHandler> dataHandlerMap = new HashMap<Byte, CommonDataHandler>() {{
        put(Byte.valueOf("03", 16), new HeartbeatHandler());
        put(Byte.valueOf("04", 16), new OutdoorGPSHandler());
        put(Byte.valueOf("05", 16), new HitRecordHandler());
        put(Byte.valueOf("06", 16), new IndoorLocationHandler());
        put(Byte.valueOf("07", 16), new MortarHitAreaHandler());
        put(Byte.valueOf("0a", 16), new SimNoRecordHandler());
    }};

    public static CommonDataHandler getHandler(byte dataType) {
        return dataHandlerMap.getOrDefault(dataType, new DefaultHandler());
    }
}
