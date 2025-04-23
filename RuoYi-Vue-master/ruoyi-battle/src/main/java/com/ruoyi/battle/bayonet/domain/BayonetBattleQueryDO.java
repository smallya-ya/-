package com.ruoyi.battle.bayonet.domain;

import com.ruoyi.common.battle.domain.BaseQueryDO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BayonetBattleQueryDO extends BaseQueryDO {

    @ApiModelProperty(value = "演习类型")
    private Integer type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
