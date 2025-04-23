package com.ruoyi.battle.battle.domain;

import com.ruoyi.common.battle.domain.BaseQueryDO;

/**
 * @author hongjiasen
 */
public class BattleSettingQueryDO extends BaseQueryDO {

    private String name;
    private String mapName;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
