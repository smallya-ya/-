package com.ruoyi.battle.bayonet;

/**
 * @author hongjiasen
 */
public class Constant {

    /**
     * 刺杀模式
     * 刺中即暂停，规定时间内刺中次数最多者胜利
     */
    @Deprecated
    public static final int CISHA_MODE = 1;

    /**
     * 胜分模式
     * 规定时间得分多者胜利
     */
    public static final int SHENGFEN_MODE = 2;

    /**
     * 胜枪模式
     * 规定时间内刺中次数多者胜利
     */
    public static final int SHENGQIANG_MODE = 3;

    /**
     * 快枪模式
     * 没时间限制，最先达到规定枪数者，胜利
     */
    public static final int KUAIQIANG_MODE = 4;

    /**
     * 五枪三胜
     * 规定时间，规定总枪数，先完成目标枪数者胜利
     */
    public static final int WUQIANGSANSHENG_MODE = 5;
}
