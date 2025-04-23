package com.ruoyi.battle.battle.domain;

import java.util.List;

/**
 * @author hongjiasen
 */
public class BattleRecordVO extends BattleRecordEntity {

    private List<BattleRecordDetailEntity> vestData;

    public List<BattleRecordDetailEntity> getVestData() {
        return vestData;
    }

    public void setVestData(List<BattleRecordDetailEntity> vestData) {
        this.vestData = vestData;
    }
}
