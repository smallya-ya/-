package com.ruoyi.battle.bayonet.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class GroupMemberBO {

    @ApiModelProperty(value = "组织id")
    private Long unitId;

    @ApiModelProperty(value = "人员id")
    private Long personId;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
