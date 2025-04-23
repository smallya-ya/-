package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetHitScoreVO {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "得分项ID")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "击中部位ID")
    private long hitAreaId;

    @ApiModelProperty(value = "击中部位编号")
    private Integer hitCode;

    @ApiModelProperty(value = "击中部位名称")
    private String hitAreaName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "方案id")
    private long planId;

    @ApiModelProperty(value = "分值", required = true)
    private int score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getHitAreaId() {
        return hitAreaId;
    }

    public void setHitAreaId(long hitAreaId) {
        this.hitAreaId = hitAreaId;
    }

    public Integer getHitCode() {
        return hitCode;
    }

    public void setHitCode(Integer hitCode) {
        this.hitCode = hitCode;
    }

    public String getHitAreaName() {
        return hitAreaName;
    }

    public void setHitAreaName(String hitAreaName) {
        this.hitAreaName = hitAreaName;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
