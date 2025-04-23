package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_score_plan")
public class BayonetScorePlanEntity extends CommonEntity {

    @ApiModelProperty(value = "得分方案名")
    private String planName;

    @TableField(exist = false)
    @ApiModelProperty(value = "得分配置项")
    private List<BayonetHitScoreEntity> hitScoreList;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public List<BayonetHitScoreEntity> getHitScoreList() {
        return hitScoreList;
    }

    public void setHitScoreList(List<BayonetHitScoreEntity> hitScoreList) {
        this.hitScoreList = hitScoreList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
