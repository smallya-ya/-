package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * 刺杀单位
 * @author hongjiasen
 */
@TableName("t_bayonet_unit")
public class BayonetUnitEntity extends CommonEntity {

    @ApiModelProperty(value = "单位名称", required = true)
    private String unitName;

    @ApiModelProperty(value = "负责人", required = true)
    private String leader;

    @ApiModelProperty(value = "人员数量", required = true)
    private Integer quantity;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
