package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author guest1
 */
public class BayonetScorePlanVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "得分方案名")
    private String planName;

    @ApiModelProperty(value = "得分配置项")
    private List<BayonetHitScoreVO> hitScoreList;

    @ApiModelProperty(value = "备注")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public List<BayonetHitScoreVO> getHitScoreList() {
        return hitScoreList;
    }

    public void setHitScoreList(List<BayonetHitScoreVO> hitScoreList) {
        this.hitScoreList = hitScoreList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
