package com.ruoyi.battle.battle.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用于接收实兵演习人员配置信息
 * @author hongjiasen
 */
@ApiModel(description = "用于接收实兵演习人员配置信息")
public class BattleTeamConfigDO {

    @ApiModelProperty(value = "队伍")
    protected String team;

    @ApiModelProperty(value = "编号")
    protected int num;

    @ApiModelProperty(value = "姓名")
    protected String name;

    @ApiModelProperty(value = "主武器")
    protected String primaryWeapon;

    @ApiModelProperty(value = "副武器")
    protected String secondaryWeapon;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(String primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public String getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void setSecondaryWeapon(String secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }
}
