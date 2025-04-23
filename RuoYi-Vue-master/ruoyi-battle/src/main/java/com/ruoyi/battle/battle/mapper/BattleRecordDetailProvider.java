package com.ruoyi.battle.battle.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */

public class BattleRecordDetailProvider {

    public String detailTrail(Long battleId, LocalDateTime time) {
        SQL sql = new SQL();
        sql.SELECT("t1.*")
                .FROM("t_battle_record_detail t1", "t_battle_record t2" , "t_battle t3")
                .WHERE("t1.record_id = t2.id", "t2.battle_id = t3.id")
                .WHERE("t3.id = #{battleId}", "t2.time <= #{time}");
        return sql.toString();
    }
}
