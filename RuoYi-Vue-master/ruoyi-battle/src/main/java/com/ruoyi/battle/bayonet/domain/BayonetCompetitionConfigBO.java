package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionConfigBO {

    @ApiModelProperty(value = "ID，更新时需要")
    private Long id;

    @ApiModelProperty(value = "序号，唯一，不可重复", required = true)
    private Integer serialNo;

    @ApiModelProperty(value = "5种演习类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "演习方案", required = true)
    private String competitionName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "得分方案ID", required = true)
    private Long scorePlanId;

    @ApiModelProperty(value = "限时（分钟）", required = true)
    private Integer limitedTime;

    @ApiModelProperty(value = "规定总枪数")
    private Integer totalHitNum;

//    @ApiModelProperty(value = "距离", required = true)
//    private String distance;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "对抗人员组，两两对抗")
    private List<BayonetCompetitionGroupBO> groups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Long getScorePlanId() {
        return scorePlanId;
    }

    public void setScorePlanId(Long scorePlanId) {
        this.scorePlanId = scorePlanId;
    }

    public Integer getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(Integer limitedTime) {
        this.limitedTime = limitedTime;
    }

//    public String getDistance() {
//        return distance;
//    }
//
//    public void setDistance(String distance) {
//        this.distance = distance;
//    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BayonetCompetitionGroupBO> getGroups() {
        return groups;
    }

    public void setGroups(List<BayonetCompetitionGroupBO> groups) {
        this.groups = groups;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTotalHitNum() {
        return totalHitNum;
    }

    public void setTotalHitNum(Integer totalHitNum) {
        this.totalHitNum = totalHitNum;
    }
}
