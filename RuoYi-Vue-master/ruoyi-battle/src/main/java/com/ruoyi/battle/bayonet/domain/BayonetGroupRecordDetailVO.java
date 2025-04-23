package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author hongjiasen
 */
public class BayonetGroupRecordDetailVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String attacker;

    private String opponent;

    private String hitArea;

    private int hitStrength;

    private int happenTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttacker() {
        return attacker;
    }

    public void setAttacker(String attacker) {
        this.attacker = attacker;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getHitArea() {
        return hitArea;
    }

    public void setHitArea(String hitArea) {
        this.hitArea = hitArea;
    }

    public int getHitStrength() {
        return hitStrength;
    }

    public void setHitStrength(int hitStrength) {
        this.hitStrength = hitStrength;
    }

    public int getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(int happenTime) {
        this.happenTime = happenTime;
    }
}
