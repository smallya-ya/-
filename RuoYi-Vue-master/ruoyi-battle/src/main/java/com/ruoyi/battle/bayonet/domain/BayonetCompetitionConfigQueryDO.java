package com.ruoyi.battle.bayonet.domain;

import com.ruoyi.common.battle.domain.BaseQueryDO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionConfigQueryDO extends BaseQueryDO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "序号")
    private Integer serialNo;

    @ApiModelProperty(value = "名称")
    private String competitionName;

    @ApiModelProperty(value = "得分方案")
    private String scorePlan;

    @ApiModelProperty(value = "限时（分钟）")
    private Integer limitedTime;

    @ApiModelProperty(value = "距离")
    private String distance;

    @ApiModelProperty(value = "备注信息")
    private String remark;

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
}
