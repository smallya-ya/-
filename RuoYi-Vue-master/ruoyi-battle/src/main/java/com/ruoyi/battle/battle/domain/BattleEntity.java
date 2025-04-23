package com.ruoyi.battle.battle.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;

import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */
@TableName("t_battle")
public class BattleEntity extends CommonEntity {

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long mapId;

    private int mapType;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private int isLoadAmmo;

    @TableField(exist = false)
    private int ammo;

    @TableField(exist = false)
    private int isLoadAmmo2;

    @TableField(exist = false)
    private int ammo2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsLoadAmmo() {
        return isLoadAmmo;
    }

    public void setIsLoadAmmo(int isLoadAmmo) {
        this.isLoadAmmo = isLoadAmmo;
    }

    public int getIsLoadAmmo2() {
        return isLoadAmmo2;
    }

    public void setIsLoadAmmo2(int isLoadAmmo2) {
        this.isLoadAmmo2 = isLoadAmmo2;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo2() {
        return ammo2;
    }

    public void setAmmo2(int ammo2) {
        this.ammo2 = ammo2;
    }

    public static final class BattleEntityBuilder {
        private String name;
        private Long mapId;
        private int mapType;
        private LocalDateTime beginTime;
        private LocalDateTime endTime;
        private Integer status;
        private String remark;
        private int isLoadAmmo;
        private int isLoadAmmo2;
        private int ammo;
        private int ammo2;

        private BattleEntityBuilder() {
        }

        public static BattleEntityBuilder aBattleEntity() {
            return new BattleEntityBuilder();
        }

        public BattleEntityBuilder ammo(int ammo) {
            this.ammo = ammo;
            return this;
        }

        public BattleEntityBuilder ammo2(int ammo2) {
            this.ammo2 = ammo2;
            return this;
        }

        public BattleEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BattleEntityBuilder mapId(Long mapId) {
            this.mapId = mapId;
            return this;
        }

        public BattleEntityBuilder mapType(int mapType) {
            this.mapType = mapType;
            return this;
        }

        public BattleEntityBuilder beginTime(LocalDateTime beginTime) {
            this.beginTime = beginTime;
            return this;
        }

        public BattleEntityBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public BattleEntityBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public BattleEntityBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public BattleEntityBuilder isLoadAmmo(int isLoadAmmo) {
            this.isLoadAmmo = isLoadAmmo;
            return this;
        }

        public BattleEntityBuilder isLoadAmmo2(int isLoadAmmo2) {
            this.isLoadAmmo2 = isLoadAmmo2;
            return this;
        }

        public BattleEntity build() {
            BattleEntity battleEntity = new BattleEntity();
            battleEntity.setName(name);
            battleEntity.setMapId(mapId);
            battleEntity.setMapType(mapType);
            battleEntity.setBeginTime(beginTime);
            battleEntity.setEndTime(endTime);
            battleEntity.setStatus(status);
            battleEntity.setRemark(remark);
            battleEntity.setIsLoadAmmo(isLoadAmmo);
            battleEntity.setIsLoadAmmo2(isLoadAmmo2);
            battleEntity.setAmmo(ammo);
            battleEntity.setAmmo2(ammo2);
            return battleEntity;
        }
    }
}
