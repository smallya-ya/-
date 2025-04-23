package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_hit_score")
public class BayonetHitScoreEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "击中部位ID", required = true)
    private long hitAreaId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "方案id")
    private long planId;

    @ApiModelProperty(value = "分值", required = true)
    private int score;

    public long getHitAreaId() {
        return hitAreaId;
    }

    public void setHitAreaId(long hitAreaId) {
        this.hitAreaId = hitAreaId;
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
