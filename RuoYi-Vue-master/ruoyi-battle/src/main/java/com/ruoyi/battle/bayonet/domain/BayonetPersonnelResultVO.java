package com.ruoyi.battle.bayonet.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author hongjiasen
 */
public class BayonetPersonnelResultVO {

    @ApiModelProperty(value = "组织")
    private String unitName;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "编号")
    private int num;

    @ApiModelProperty(value = "性别")
    private int gender;

    @ApiModelProperty(value = "年龄")
    private int age;

    @ApiModelProperty(value = "总得分")
    private int score;

    @ApiModelProperty(value = "是否胜利")
    private boolean isWin;

    @ApiModelProperty(value = "击中成绩列表")
    private List<BayonetBattleAchievementVO> achievement;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public List<BayonetBattleAchievementVO> getAchievement() {
        return achievement;
    }

    public void setAchievement(List<BayonetBattleAchievementVO> achievement) {
        this.achievement = achievement;
    }
}
