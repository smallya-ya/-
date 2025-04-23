package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetBattleAchievementVO {

    @ApiModelProperty(value = "人员编号")
    private Integer num;

    @ApiModelProperty(value = "姓名")
    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "击中部位Id")
    private Long hitAreaId;

    @ApiModelProperty(value = "击中部位")
    private String hitArea;

    @ApiModelProperty(value = "击中次数")
    private Integer hitTimes;

    @ApiModelProperty(value = "击中力量")
    private Integer hitStrength;

    @ApiModelProperty(value = "击中时间（秒）")
    private long happenTime;

    @ApiModelProperty(value = "总击中次数")
    private Integer numOfTotalHits;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHitAreaId() {
        return hitAreaId;
    }

    public void setHitAreaId(Long hitAreaId) {
        this.hitAreaId = hitAreaId;
    }

    public String getHitArea() {
        return hitArea;
    }

    public void setHitArea(String hitArea) {
        this.hitArea = hitArea;
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

    public Integer getNumOfTotalHits() {
        return numOfTotalHits;
    }

    public void setNumOfTotalHits(Integer numOfTotalHits) {
        this.numOfTotalHits = numOfTotalHits;
    }

    public static final class BayonetBattleAchievementVOBuilder {
        private Integer num;
        private String name;
        private Long hitAreaId;
        private String hitArea;
        private Integer hitTimes;
        private Integer hitStrength;
        private long happenTime;
        private Integer numOfTotalHits;

        private BayonetBattleAchievementVOBuilder() {
        }

        public static BayonetBattleAchievementVOBuilder aBayonetBattleAchievementVO() {
            return new BayonetBattleAchievementVOBuilder();
        }

        public BayonetBattleAchievementVOBuilder num(Integer num) {
            this.num = num;
            return this;
        }

        public BayonetBattleAchievementVOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BayonetBattleAchievementVOBuilder hitAreaId(Long hitAreaId) {
            this.hitAreaId = hitAreaId;
            return this;
        }

        public BayonetBattleAchievementVOBuilder hitArea(String hitArea) {
            this.hitArea = hitArea;
            return this;
        }

        public BayonetBattleAchievementVOBuilder hitTimes(Integer hitTimes) {
            this.hitTimes = hitTimes;
            return this;
        }

        public BayonetBattleAchievementVOBuilder hitStrength(Integer hitStrength) {
            this.hitStrength = hitStrength;
            return this;
        }

        public BayonetBattleAchievementVOBuilder happenTime(long happenTime) {
            this.happenTime = happenTime;
            return this;
        }

        public BayonetBattleAchievementVOBuilder numOfTotalHits(Integer numOfTotalHits) {
            this.numOfTotalHits = numOfTotalHits;
            return this;
        }

        public BayonetBattleAchievementVO build() {
            BayonetBattleAchievementVO bayonetBattleAchievementVO = new BayonetBattleAchievementVO();
            bayonetBattleAchievementVO.setNum(num);
            bayonetBattleAchievementVO.setName(name);
            bayonetBattleAchievementVO.setHitAreaId(hitAreaId);
            bayonetBattleAchievementVO.setHitArea(hitArea);
            bayonetBattleAchievementVO.setHitTimes(hitTimes);
            bayonetBattleAchievementVO.setHitStrength(hitStrength);
            bayonetBattleAchievementVO.setHappenTime(happenTime);
            bayonetBattleAchievementVO.setNumOfTotalHits(numOfTotalHits);
            return bayonetBattleAchievementVO;
        }
    }
}
