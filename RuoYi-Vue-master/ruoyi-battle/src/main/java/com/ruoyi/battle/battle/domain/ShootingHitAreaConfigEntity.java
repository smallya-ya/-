package com.ruoyi.battle.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_shooting_hit_area_config")
public class ShootingHitAreaConfigEntity extends CommonEntity {

    @ApiModelProperty(value = "击中部位编号", required = true)
    private Integer hitCode;

    @ApiModelProperty(value = "击中部位名称", required = true)
    private String hitAreaName;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "伤害值")
    private Integer damage;

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

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }
}
