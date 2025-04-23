package com.ruoyi.common.battle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongjiasen
 */
public class Constant {

    public static final int HIGH_PRIORITY = 0;

    public static final int LOW_PRIORITY = 10;

    public static final String JASYPT_ENCRYPTOR_PASSWORD = "scds@hit2020";

    public static final int BUFFER_SIZE = 5000;

    public static final String TRUE = "1";
    public static final String FORCE_UP = "forceup";

    public static final int BATTLE_SERIALPORT_MODE = 1;
    public static final int BATTLE_MQTT_MODE = 2;

    public static final int STOP_BATTLE_RESPON_TIME = 5;

    public static final int OUTDOOR_TYPE = 0;
    public static final int INDOOR_TYPE = 1;
    public static final int AOWEI_TYPE = 2;

    public static final int PRIMARY_WEAPON = 2;
    public static final int SECONDARY_WEAPON = 3;

    public static final int STANDALONE_MODE = 1;
    public static final int ONLINE_MODE = 0;

    public static final int NORMAL_STATUS = 0;
    public static final int UNNORMAL_STATUS = 1;
    public static final int CALIBRATION_GUN = 2;

    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String FAIL_STATUS = "FAIL";

    public static final int OFF_LINE_STATUS = 0;
    public static final int LIVE_STATUS = 1;
    public static final int DIE_STATUS = 2;
    public static final int TRAING_MODE = 0;
    public static final int DRILL_MODE = 1;

    public static final int START_BATTLE_TPYE = 0;
    public static final int SHOT_LOG_TYPE = 1;
    public static final int DIE_LOG_TYPE = 2;
    public static final int LIVE_LOG_TYPE = 3;
    public static final int INJURE_LOG_TYPE = 4;
    public static final int RELOAD_LOG_TPYE = 5;
    public static final int END_BATTLE_TPYE = 5;

    public static final int PEOPLE_OR_OBJECT_TYPE = 1800;

    public static final int MORTAR_DEAD_DISTANCE = 20;

    public static final Map<String, Byte> TEAM_MAP = new HashMap<String, Byte>(4) {{
        put("red", Byte.parseByte("1"));
        put("blue", Byte.parseByte("2"));
        put("orange", Byte.parseByte("3"));
        put("yellow", Byte.parseByte("4"));
    }};

    /** 默认日期时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
}
