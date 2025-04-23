package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionGroupRecordDetailVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "人员")
    private String name;

    @ApiModelProperty(value = "头部")
    private int head;

    @ApiModelProperty(value = "左胸")
    private int leftChest;

    @ApiModelProperty(value = "右胸")
    private int rightChest;

    @ApiModelProperty(value = "左臂")
    private int leftArm;

    @ApiModelProperty(value = "右臂")
    private int rightArm;

    @ApiModelProperty(value = "左腹")
    private int leftAbdomen;

    @ApiModelProperty(value = "右腹")
    private int rightAbdomen;

    @ApiModelProperty(value = "左肋")
    private int leftRib;

    @ApiModelProperty(value = "右肋")
    private int rightRib;

    @ApiModelProperty(value = "总次数")
    private int totalHitTimes;

    @ApiModelProperty(value = "总分数")
    private int totalHitScore;

    @ApiModelProperty(value = "总力量")
    private int totalHitStrength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getLeftChest() {
        return leftChest;
    }

    public void setLeftChest(int leftChest) {
        this.leftChest = leftChest;
    }

    public int getRightChest() {
        return rightChest;
    }

    public void setRightChest(int rightChest) {
        this.rightChest = rightChest;
    }

    public int getLeftArm() {
        return leftArm;
    }

    public void setLeftArm(int leftArm) {
        this.leftArm = leftArm;
    }

    public int getRightArm() {
        return rightArm;
    }

    public void setRightArm(int rightArm) {
        this.rightArm = rightArm;
    }

    public int getLeftAbdomen() {
        return leftAbdomen;
    }

    public void setLeftAbdomen(int leftAbdomen) {
        this.leftAbdomen = leftAbdomen;
    }

    public int getRightAbdomen() {
        return rightAbdomen;
    }

    public void setRightAbdomen(int rightAbdomen) {
        this.rightAbdomen = rightAbdomen;
    }

    public int getLeftRib() {
        return leftRib;
    }

    public void setLeftRib(int leftRib) {
        this.leftRib = leftRib;
    }

    public int getRightRib() {
        return rightRib;
    }

    public void setRightRib(int rightRib) {
        this.rightRib = rightRib;
    }

    public int getTotalHitTimes() {
        return totalHitTimes;
    }

    public void setTotalHitTimes(int totalHitTimes) {
        this.totalHitTimes = totalHitTimes;
    }

    public int getTotalHitScore() {
        return totalHitScore;
    }

    public void setTotalHitScore(int totalHitScore) {
        this.totalHitScore = totalHitScore;
    }

    public int getTotalHitStrength() {
        return totalHitStrength;
    }

    public void setTotalHitStrength(int totalHitStrength) {
        this.totalHitStrength = totalHitStrength;
    }
}
