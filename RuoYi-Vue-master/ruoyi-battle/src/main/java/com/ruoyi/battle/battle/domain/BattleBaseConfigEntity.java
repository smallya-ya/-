package com.ruoyi.battle.battle.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;

/**
 * @author hongjiasen
 */
@TableName("t_battle_base_config")
public class BattleBaseConfigEntity extends CommonEntity {

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long mapId;

    private Integer mode;

    @TableField("`type`")
    private Integer type;

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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static final class BattleBaseConfigEntityBuilder {
        private String name;
        private Long mapId;
        private Integer mode;
        private Integer type;

        private BattleBaseConfigEntityBuilder() {
        }

        public static BattleBaseConfigEntityBuilder aBattleBaseConfigEntity() {
            return new BattleBaseConfigEntityBuilder();
        }

        public BattleBaseConfigEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BattleBaseConfigEntityBuilder mapId(Long mapId) {
            this.mapId = mapId;
            return this;
        }

        public BattleBaseConfigEntityBuilder mode(Integer mode) {
            this.mode = mode;
            return this;
        }

        public BattleBaseConfigEntityBuilder type(Integer type) {
            this.type = type;
            return this;
        }

        public BattleBaseConfigEntity build() {
            BattleBaseConfigEntity battleBaseConfigEntity = new BattleBaseConfigEntity();
            battleBaseConfigEntity.setName(name);
            battleBaseConfigEntity.setMapId(mapId);
            battleBaseConfigEntity.setMode(mode);
            battleBaseConfigEntity.setType(type);
            return battleBaseConfigEntity;
        }
    }
}
