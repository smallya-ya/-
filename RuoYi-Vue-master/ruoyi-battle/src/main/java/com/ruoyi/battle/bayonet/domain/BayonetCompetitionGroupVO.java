package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionGroupVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    private Integer limitedTime;

    private Integer totalHitNum;

    private BayonetPersonnelVO member1;

    private BayonetPersonnelVO member2;

    public BayonetCompetitionGroupVO() {
    }

//    public BayonetCompetitionGroupVO(Long groupId, BayonetPersonnelVO member1, BayonetPersonnelVO member2) {
//        this.groupId = groupId;
//        this.member1 = member1;
//        this.member2 = member2;
//    }


    public BayonetCompetitionGroupVO(Long groupId, Integer limitedTime, BayonetPersonnelVO member1, BayonetPersonnelVO member2) {
        this.groupId = groupId;
        this.limitedTime = limitedTime;
        this.member1 = member1;
        this.member2 = member2;
    }

    public BayonetCompetitionGroupVO(Long groupId, Integer limitedTime, Integer totalHitNum, BayonetPersonnelVO member1, BayonetPersonnelVO member2) {
        this.groupId = groupId;
        this.limitedTime = limitedTime;
        this.totalHitNum = totalHitNum;
        this.member1 = member1;
        this.member2 = member2;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(Integer limitedTime) {
        this.limitedTime = limitedTime;
    }

    public BayonetPersonnelVO getMember1() {
        return member1;
    }

    public void setMember1(BayonetPersonnelVO member1) {
        this.member1 = member1;
    }

    public BayonetPersonnelVO getMember2() {
        return member2;
    }

    public void setMember2(BayonetPersonnelVO member2) {
        this.member2 = member2;
    }

    public Integer getTotalHitNum() {
        return totalHitNum;
    }

    public void setTotalHitNum(Integer totalHitNum) {
        this.totalHitNum = totalHitNum;
    }
}
