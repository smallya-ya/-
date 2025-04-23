package com.ruoyi.battle.battle.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author hongjiasen
 */
public class BattleVestInfo {

    @ApiModelProperty(value = "马甲队伍：red-红队；blue-蓝队；orange-人质；yellow-劫匪")
    protected String team;

    @ApiModelProperty(value = "马甲编号")
    protected int num;

    @ApiModelProperty(value = "马甲名字")
    protected String name;

    @ApiModelProperty(value = "主武器")
    protected String weapon1;

    @ApiModelProperty(value = "主武器弹药")
    protected int ammo1;

    @ApiModelProperty(value = "副武器")
    protected String weapon2;

    @ApiModelProperty(value = "副武器弹药")
    protected int ammo2;

    @ApiModelProperty(value = "血量")
    protected int hp;

    @ApiModelProperty(value = "马甲状态：0-离线；1-存活；2-死亡")
    protected int status;

    @ApiModelProperty(value = "纬度")
    protected BigDecimal lat = BigDecimal.ZERO;

    @ApiModelProperty(value = "经度")
    protected BigDecimal lng = BigDecimal.ZERO;

    @ApiModelProperty(value = "融云token")
    protected String token;

    @ApiModelProperty(value = "融云userID")
    protected String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getWeapon1() {
        return weapon1;
    }

    public void setWeapon1(String weapon1) {
        this.weapon1 = weapon1;
    }

    public int getAmmo1() {
        return ammo1;
    }

    public void setAmmo1(int ammo1) {
        this.ammo1 = ammo1;
    }

    public String getWeapon2() {
        return weapon2;
    }

    public void setWeapon2(String weapon2) {
        this.weapon2 = weapon2;
    }

    public int getAmmo2() {
        return ammo2;
    }

    public void setAmmo2(int ammo2) {
        this.ammo2 = ammo2;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
