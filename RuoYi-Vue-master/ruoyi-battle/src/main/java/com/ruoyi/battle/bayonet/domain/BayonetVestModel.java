package com.ruoyi.battle.bayonet.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongjiasen
 */
public class BayonetVestModel {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "编号", required = true)
    private int num;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "队伍", required = true)
    private String team;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "每次演习对抗组id")
    private Long battleGroupRecordId;

    @ApiModelProperty(value = "对手编号")
    private int opponentNum;

    @ApiModelProperty(value = "当前击中部位")
    private int currentHitArea;

    @ApiModelProperty(value = "总击中次数")
    private AtomicInteger numOfTotalHits = new AtomicInteger(0);

    @ApiModelProperty(value = "总得分")
    private AtomicInteger totalScore = new AtomicInteger(0);

    @ApiModelProperty(value = "击中记录")
    private List<HitAreaLogDetail> hitAreaLog = new ArrayList<>();

    @ApiModelProperty(value = "演习记录")
    private List<BayonetVestLogVO> battleLog = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Long getBattleGroupRecordId() {
        return battleGroupRecordId;
    }

    public void setBattleGroupRecordId(Long battleGroupRecordId) {
        this.battleGroupRecordId = battleGroupRecordId;
    }

    public int getOpponentNum() {
        return opponentNum;
    }

    public void setOpponentNum(int opponentNum) {
        this.opponentNum = opponentNum;
    }

    public int getCurrentHitArea() {
        return currentHitArea;
    }

    public void setCurrentHitArea(int currentHitArea) {
        this.currentHitArea = currentHitArea;
    }

    public Integer getNumOfTotalHits() {
        return numOfTotalHits.get();
    }

    public void getAndIncrement() {
        this.numOfTotalHits.getAndIncrement();
    }

    public Integer getTotalScore() {
        return totalScore.get();
    }

    public int addScore(Integer score) {
        return this.totalScore.addAndGet(score);
    }

    public List<HitAreaLogDetail> getHitAreaLog() {
        return hitAreaLog;
    }

    public List<BayonetVestLogVO> getBattleLog() {
        return battleLog;
    }

    public static final class BayonetVestModelBuilder {
        private Long id;
        private int num;
        private String name;
        private String team;

        private Long battleGroupRecordId;

        private int opponentNum;

        private BayonetVestModelBuilder() {
        }

        public static BayonetVestModelBuilder aBayonetVestModel() {
            return new BayonetVestModelBuilder();
        }

        public BayonetVestModelBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BayonetVestModelBuilder num(int num) {
            this.num = num;
            return this;
        }

        public BayonetVestModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BayonetVestModelBuilder team(String team) {
            this.team = team;
            return this;
        }

        public BayonetVestModelBuilder battleGroupRecordId(Long battleGroupRecordId) {
            this.battleGroupRecordId = battleGroupRecordId;
            return this;
        }

        public BayonetVestModelBuilder opponentNum(int opponentNum) {
            this.opponentNum = opponentNum;
            return this;
        }

        public BayonetVestModel build() {
            BayonetVestModel bayonetVestModel = new BayonetVestModel();
            bayonetVestModel.setId(id);
            bayonetVestModel.setNum(num);
            bayonetVestModel.setName(name);
            bayonetVestModel.setTeam(team);
            bayonetVestModel.setBattleGroupRecordId(battleGroupRecordId);
            bayonetVestModel.setOpponentNum(opponentNum);
            return bayonetVestModel;
        }
    }
}
