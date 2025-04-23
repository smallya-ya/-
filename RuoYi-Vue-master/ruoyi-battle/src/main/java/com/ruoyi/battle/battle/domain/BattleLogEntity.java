package com.ruoyi.battle.battle.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */
@TableName("t_battle_log")
public class BattleLogEntity extends CommonEntity {

    @ApiModelProperty(value = "演习ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long battleId;

    @ApiModelProperty(value = "事件类型")
    private Integer type;

    @ApiModelProperty(value = "发生时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "演习日志")
    private String log;

    private int isShow;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public static final class BattleLogEntityBuilder {
        private Long battleId;
        private Integer type;
        private LocalDateTime time;
        private String log;
        private int isShow;

        private BattleLogEntityBuilder() {
        }

        public static BattleLogEntityBuilder aBattleLogEntity() {
            return new BattleLogEntityBuilder();
        }

        public BattleLogEntityBuilder battleId(Long battleId) {
            this.battleId = battleId;
            return this;
        }

        public BattleLogEntityBuilder type(Integer type) {
            this.type = type;
            return this;
        }

        public BattleLogEntityBuilder time(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public BattleLogEntityBuilder log(String log) {
            this.log = log;
            return this;
        }

        public BattleLogEntityBuilder isShow(int isShow) {
            this.isShow = isShow;
            return this;
        }

        public BattleLogEntity build() {
            BattleLogEntity battleLogEntity = new BattleLogEntity();
            battleLogEntity.setBattleId(battleId);
            battleLogEntity.setType(type);
            battleLogEntity.setTime(time);
            battleLogEntity.setLog(log);
            battleLogEntity.setIsShow(isShow);
            return battleLogEntity;
        }
    }
}
