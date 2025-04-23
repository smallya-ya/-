package com.ruoyi.battle.bayonet;

/**
 * @author hongjiasen
 */

public enum BayonetModeEnum {

    /**
     * 刺杀模式
     */
    CISHA(1, "刺杀模式", "刺中即暂停，规定时间内刺中次数最多者胜利")
    ,
    /**
     * 胜分模式
     */
    SHENGFEN(2, "胜分模式", "规定时间得分多者胜利")
    ,
    /**
     * 胜枪模式
     */
    SHENGQIANG(3, "胜枪模式", "规定时间内刺中次数多者胜利")
    ,
    /**
     * 快枪模式
     */
    KUAIQIANG(4, "快枪模式", "没时间限制，最先达到规定枪数者，胜利")
    ,
    /**
     * 五枪三胜
     */
    WUQIANGSANSHENG(5, "五枪三胜", "规定时间，规定总枪数，先完成目标枪数者胜利");

    private int type;
    private String name;
    private String desc;

    BayonetModeEnum(int type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getModeName(int type) {
        for (BayonetModeEnum mode : BayonetModeEnum.values()) {
            if (type == mode.getType()) {
                return mode.getName();
            }
        }
        return "未知模式";
    }
}
