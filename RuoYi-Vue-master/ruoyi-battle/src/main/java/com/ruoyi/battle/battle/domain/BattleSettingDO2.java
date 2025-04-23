package com.ruoyi.battle.battle.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author hongjiasen
 */
@ApiModel(description = "用于接收实兵演习配置信息")
public class BattleSettingDO2 {

    @ApiModelProperty(value = "演习名称")
    protected String name;

    @ApiModelProperty(value = "地图ID")
    protected Long mapId;

    @ApiModelProperty(value = "演习模式")
    protected Integer mode;

    protected List<BattleTeamConfigEntity2> teamData;

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

    public List<BattleTeamConfigEntity2> getTeamData() {
        return teamData;
    }

    public void setTeamData(List<BattleTeamConfigEntity2> teamData) {
        this.teamData = teamData;
    }
}
