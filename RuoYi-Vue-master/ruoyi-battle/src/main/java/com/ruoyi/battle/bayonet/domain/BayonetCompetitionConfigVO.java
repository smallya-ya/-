package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionConfigVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "序号")
    private Integer serialNo;

    @ApiModelProperty(value = "名称")
    private String competitionName;

    @ApiModelProperty(value = "5种演习类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "规定总枪数")
    private Integer totalHitNum;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "得分方案Id")
    private Long scorePlanId;

    @ApiModelProperty(value = "得分方案")
    private String scorePlan;

    @ApiModelProperty(value = "限时（分钟）")
    private Integer limitedTime;

    @ApiModelProperty(value = "距离")
    private String distance;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "对抗人员组，两两对抗")
    private List<BayonetCompetitionGroupVO> groups;

    public Long getScorePlanId() {
        return scorePlanId;
    }

    public void setScorePlanId(Long scorePlanId) {
        this.scorePlanId = scorePlanId;
    }

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

    public String getScorePlan() {
        return scorePlan;
    }

    public void setScorePlan(String scorePlan) {
        this.scorePlan = scorePlan;
    }

    public Integer getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(Integer limitedTime) {
        this.limitedTime = limitedTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BayonetCompetitionGroupVO> getGroups() {
        return groups;
    }

    public void setGroups(List<BayonetCompetitionGroupVO> groups) {
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
