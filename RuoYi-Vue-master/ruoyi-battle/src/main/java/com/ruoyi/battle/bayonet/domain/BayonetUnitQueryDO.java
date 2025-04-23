package com.ruoyi.battle.bayonet.domain;

import com.ruoyi.common.battle.domain.BaseQueryDO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetUnitQueryDO extends BaseQueryDO {

//    @ApiModelProperty(value = "序号")
//    private String num;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "人员数量")
    private Integer quantity;

    @ApiModelProperty(value = "备注信息")
    private String remark;

//    public String getNum() {
//        return num;
//    }
//
//    public void setNum(String num) {
//        this.num = num;
//    }

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
