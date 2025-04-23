package com.ruoyi.battle.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;

/**
 * @author hongjiasen
 */
@TableName("t_battle_team_config2")
public class BattleTeamConfigEntity2 extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long battleBaseConfigId;

    private String team;

    private String nums;

    private String name;

    private String primaryWeapon;

    private String secondaryWeapon;

    public Long getBattleBaseConfigId() {
        return battleBaseConfigId;
    }

    public void setBattleBaseConfigId(Long battleBaseConfigId) {
        this.battleBaseConfigId = battleBaseConfigId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
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
