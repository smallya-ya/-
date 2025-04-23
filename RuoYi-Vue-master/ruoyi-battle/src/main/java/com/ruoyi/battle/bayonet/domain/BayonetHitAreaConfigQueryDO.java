package com.ruoyi.battle.bayonet.domain;

import com.ruoyi.common.battle.domain.BaseQueryDO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetHitAreaConfigQueryDO extends BaseQueryDO {

    @ApiModelProperty(value = "击中部位编号")
    private Integer hitCode;

    @ApiModelProperty(value = "击中部位名称")
    private String hitAreaName;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
