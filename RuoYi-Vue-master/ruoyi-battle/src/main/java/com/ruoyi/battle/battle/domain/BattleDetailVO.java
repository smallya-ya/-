package com.ruoyi.battle.battle.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;

import java.util.Collection;
import java.util.List;

/**
 * @author hongjiasen
 */
public class BattleDetailVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long battleId;
    private String mapBase64;
    private Collection<ShootingVestModel> vestEntityList;
    private List<BattleLogEntity> battleLogEntityList;
    private BaseMapEntity mapEntity;
    private Integer isLoadAmmo;
    private Integer isLoadAmmo2;
    private int ammo;
    private int ammo2;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public String getMapBase64() {
        return mapBase64;
    }

    public void setMapBase64(String mapBase64) {
        this.mapBase64 = mapBase64;
    }

    public Collection<ShootingVestModel> getVestEntityList() {
        return vestEntityList;
    }

    public void setVestEntityList(Collection<ShootingVestModel> vestEntityList) {
        this.vestEntityList = vestEntityList;
    }

    public List<BattleLogEntity> getBattleLogEntityList() {
        return battleLogEntityList;
    }

    public void setBattleLogEntityList(List<BattleLogEntity> battleLogEntityList) {
        this.battleLogEntityList = battleLogEntityList;
    }

    public BaseMapEntity getMapEntity() {
        return mapEntity;
    }

    public void setMapEntity(BaseMapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public Integer getIsLoadAmmo() {
        return isLoadAmmo;
    }

    public void setIsLoadAmmo(Integer isLoadAmmo) {
        this.isLoadAmmo = isLoadAmmo;
    }

    public Integer getIsLoadAmmo2() {
        return isLoadAmmo2;
    }

    public void setIsLoadAmmo2(Integer isLoadAmmo2) {
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

    public static final class BattleDetailVOBuilder {
        private Long battleId;
        private String mapBase64;
        private Collection<ShootingVestModel> vestEntityList;
        private List<BattleLogEntity> battleLogEntityList;
        private BaseMapEntity mapEntity;
        private Integer isLoadAmmo;
        private Integer isLoadAmmo2;
        private int ammo;
        private int ammo2;

        private BattleDetailVOBuilder() {
        }

        public static BattleDetailVOBuilder aBattleDetailVO() {
            return new BattleDetailVOBuilder();
        }

        public BattleDetailVOBuilder battleId(Long battleId) {
            this.battleId = battleId;
            return this;
        }

        public BattleDetailVOBuilder ammo(int ammo) {
            this.ammo = ammo;
            return this;
        }

        public BattleDetailVOBuilder ammo2(int ammo2) {
            this.ammo2 = ammo2;
            return this;
        }

        public BattleDetailVOBuilder mapBase64(String mapBase64) {
            this.mapBase64 = mapBase64;
            return this;
        }

        public BattleDetailVOBuilder vestEntityList(Collection<ShootingVestModel> vestEntityList) {
            this.vestEntityList = vestEntityList;
            return this;
        }

        public BattleDetailVOBuilder battleLogEntityList(List<BattleLogEntity> battleLogEntityList) {
            this.battleLogEntityList = battleLogEntityList;
            return this;
        }

        public BattleDetailVOBuilder mapEntity(BaseMapEntity mapEntity) {
            this.mapEntity = mapEntity;
            return this;
        }

        public BattleDetailVOBuilder isLoadAmmo(Integer isLoadAmmo) {
            this.isLoadAmmo = isLoadAmmo;
            return this;
        }

        public BattleDetailVOBuilder isLoadAmmo2(Integer isLoadAmmo2) {
            this.isLoadAmmo2 = isLoadAmmo2;
            return this;
        }

        public BattleDetailVO build() {
            BattleDetailVO battleDetailVO = new BattleDetailVO();
            battleDetailVO.setBattleId(battleId);
            battleDetailVO.setMapBase64(mapBase64);
            battleDetailVO.setVestEntityList(vestEntityList);
            battleDetailVO.setBattleLogEntityList(battleLogEntityList);
            battleDetailVO.setMapEntity(mapEntity);
            battleDetailVO.setIsLoadAmmo(isLoadAmmo);
            battleDetailVO.setIsLoadAmmo2(isLoadAmmo2);
            battleDetailVO.setAmmo(ammo);
            battleDetailVO.setAmmo2(ammo2);
            return battleDetailVO;
        }
    }
}
