package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_battle_achievement")
public class BayonetBattleAchievementEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "演习id", required = true)
    private Long battleId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "每次演习对抗组id")
    private Long battleGroupRecordId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "攻击者id", required = true)
    private Long attackerId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "被击中者id", required = true)
    private Long opponentId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "击中部位id", required = true)
    private Long hitAreaId;

    @ApiModelProperty(value = "击中次数", required = true)
    private Integer hitTimes;

    @ApiModelProperty(value = "击中力量", required = true)
    private Integer hitStrength;

    @ApiModelProperty(value = "击中时间（秒）", required = true)
    private long happenTime;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public Long getAttackerId() {
        return attackerId;
    }

    public void setAttackerId(Long attackerId) {
        this.attackerId = attackerId;
    }

    public Long getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(Long opponentId) {
        this.opponentId = opponentId;
    }

    public Long getHitAreaId() {
        return hitAreaId;
    }

    public void setHitAreaId(Long hitAreaId) {
        this.hitAreaId = hitAreaId;
    }

    public Integer getHitTimes() {
        return hitTimes;
    }

    public void setHitTimes(Integer hitTimes) {
        this.hitTimes = hitTimes;
    }

    public Integer getHitStrength() {
        return hitStrength;
    }

    public void setHitStrength(Integer hitStrength) {
        this.hitStrength = hitStrength;
    }

    public long getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(long happenTime) {
        this.happenTime = happenTime;
    }

    public Long getBattleGroupRecordId() {
        return battleGroupRecordId;
    }

    public void setBattleGroupRecordId(Long battleGroupRecordId) {
        this.battleGroupRecordId = battleGroupRecordId;
    }
}
