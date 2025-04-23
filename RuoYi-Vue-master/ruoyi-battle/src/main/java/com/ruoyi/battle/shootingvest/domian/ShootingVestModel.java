package com.ruoyi.battle.shootingvest.domian;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.battle.battle.domain.HitRecordModel;
import com.ruoyi.common.battle.domain.CommonEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */
public class ShootingVestModel extends CommonEntity {

    protected String team;

    @OrderBy(asc = true)
    protected int num;

    protected String name;

    protected String weapon1;

    protected int ammo1;

    protected String weapon2;

    protected int ammo2;

    protected int hp;

    protected int status;

    @TableField(exist = false)
    private int realStatus;

    protected int mode;//0:自动 1:手动

    @TableField(exist = false)
    private String simNo;//模拟编号

    @TableField(exist = false)
    private LocalDateTime lastReportTime;

    @TableField(numericScale = "6")
    protected BigDecimal lat = BigDecimal.ZERO;//纬度

    @TableField(numericScale = "6")
    protected BigDecimal lng = BigDecimal.ZERO;//经度

    @TableField(numericScale = "6")
    protected BigDecimal wgs84Lat = BigDecimal.ZERO;//高德坐标纬度

    @TableField(numericScale = "6")
    protected BigDecimal wgs84Lng = BigDecimal.ZERO;//高德坐标经度

    @TableField(exist = false)
    private int manualLiveFlag = -1;//手动存活标识

    @TableField(exist = false)
    private HitRecordModel hitRecordModel;//击记录

    public int getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(int realStatus) {
        this.realStatus = realStatus;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public HitRecordModel getHitRecordModel() {
        return hitRecordModel;
    }

    public void setHitRecordModel(HitRecordModel hitRecordModel) {
        this.hitRecordModel = hitRecordModel;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public LocalDateTime getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(LocalDateTime lastReportTime) {
        this.lastReportTime = lastReportTime;
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

    public BigDecimal getWgs84Lat() {
        return wgs84Lat;
    }

    public void setWgs84Lat(BigDecimal wgs84Lat) {
        this.wgs84Lat = wgs84Lat;
    }

    public BigDecimal getWgs84Lng() {
        return wgs84Lng;
    }

    public void setWgs84Lng(BigDecimal wgs84Lng) {
        this.wgs84Lng = wgs84Lng;
    }

    public int getManualLiveFlag() {
        return manualLiveFlag;
    }

    public void setManualLiveFlag(int manualLiveFlag) {
        this.manualLiveFlag = manualLiveFlag;
    }


    public static final class ShootingVestModelBuilder {
        protected String team;
        protected int num;
        protected String name;
        protected String weapon1;
        protected int ammo1;
        protected String weapon2;
        protected int ammo2;
        protected int hp;
        protected int status;
        protected int mode;
        protected BigDecimal lat = BigDecimal.ZERO;
        protected BigDecimal lng = BigDecimal.ZERO;
        protected BigDecimal wgs84Lat = BigDecimal.ZERO;
        protected BigDecimal wgs84Lng = BigDecimal.ZERO;
        private LocalDateTime lastReportTime;
        private int manualLiveFlag = -1;
        private HitRecordModel hitRecordModel;
        private int realStatus;

        private ShootingVestModelBuilder() {
        }

        public static ShootingVestModelBuilder aShootingVestModel() {
            return new ShootingVestModelBuilder();
        }

        public ShootingVestModelBuilder team(String team) {
            this.team = team;
            return this;
        }

        public ShootingVestModelBuilder num(int num) {
            this.num = num;
            return this;
        }

        public ShootingVestModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ShootingVestModelBuilder weapon1(String weapon1) {
            this.weapon1 = weapon1;
            return this;
        }

        public ShootingVestModelBuilder ammo1(int ammo1) {
            this.ammo1 = ammo1;
            return this;
        }

        public ShootingVestModelBuilder weapon2(String weapon2) {
            this.weapon2 = weapon2;
            return this;
        }

        public ShootingVestModelBuilder ammo2(int ammo2) {
            this.ammo2 = ammo2;
            return this;
        }

        public ShootingVestModelBuilder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public ShootingVestModelBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ShootingVestModelBuilder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public ShootingVestModelBuilder lastReportTime(LocalDateTime lastReportTime) {
            this.lastReportTime = lastReportTime;
            return this;
        }

        public ShootingVestModelBuilder lat(BigDecimal lat) {
            this.lat = lat;
            return this;
        }

        public ShootingVestModelBuilder lng(BigDecimal lng) {
            this.lng = lng;
            return this;
        }

        public ShootingVestModelBuilder wgs84Lat(BigDecimal wgs84Lat) {
            this.wgs84Lat = wgs84Lat;
            return this;
        }

        public ShootingVestModelBuilder wgs84Lng(BigDecimal wgs84Lng) {
            this.wgs84Lng = wgs84Lng;
            return this;
        }

        public ShootingVestModelBuilder manualLiveFlag(int manualLiveFlag) {
            this.manualLiveFlag = manualLiveFlag;
            return this;
        }

        public ShootingVestModelBuilder hitRecordModel(HitRecordModel hitRecordModel) {
            this.hitRecordModel = hitRecordModel;
            return this;
        }

        public ShootingVestModelBuilder realStatus(int realStatus) {
            this.realStatus = realStatus;
            return this;
        }

        public ShootingVestModel build() {
            ShootingVestModel shootingVestModel = new ShootingVestModel();
            shootingVestModel.setTeam(team);
            shootingVestModel.setNum(num);
            shootingVestModel.setName(name);
            shootingVestModel.setWeapon1(weapon1);
            shootingVestModel.setAmmo1(ammo1);
            shootingVestModel.setWeapon2(weapon2);
            shootingVestModel.setAmmo2(ammo2);
            shootingVestModel.setHp(hp);
            shootingVestModel.setStatus(status);
            shootingVestModel.setMode(mode);
            shootingVestModel.setLastReportTime(lastReportTime);
            shootingVestModel.setLat(lat);
            shootingVestModel.setLng(lng);
            shootingVestModel.setWgs84Lat(wgs84Lat);
            shootingVestModel.setWgs84Lng(wgs84Lng);
            shootingVestModel.setManualLiveFlag(manualLiveFlag);
            shootingVestModel.setHitRecordModel(hitRecordModel);
            shootingVestModel.setRealStatus(realStatus);
            return shootingVestModel;
        }
    }
}
