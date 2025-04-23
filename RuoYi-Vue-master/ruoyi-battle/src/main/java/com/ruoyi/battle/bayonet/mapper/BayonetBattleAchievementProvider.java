package com.ruoyi.battle.bayonet.mapper;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author hongjiasen
 */
public class BayonetBattleAchievementProvider {

    public String queryByBattleId(Long id) {
        SQL sql = new SQL();
        sql.SELECT("t2.num,t2.`name`,t3.hit_area_name hit_area,t1.hit_area_id,t1.hit_times,t1.hit_strength,t1.happen_time")
                .FROM("t_bayonet_battle_achievement t1, t_bayonet_personnel t2, t_bayonet_hit_area_config t3")
                .WHERE("t1.attacker_id = t2.id and t1.hit_area_id = t3.id and t1.battle_id = #{id}")
                .ORDER_BY("t1.happen_time");
        return sql.toString();
    }

    public String queryBattleDetailById(Long id) {
        SQL sql = new SQL();
        sql.SELECT("t1.id,t2.hit_area_name,t1.hit_times,t1.hit_strength,t1.happen_time")
                .SELECT("(select competition_name from t_bayonet_competition_config tbcc where tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) battle")
                .SELECT("(select `name` from t_bayonet_personnel tbp where tbp.id = t1.attacker_id) attacker")
                .SELECT("(select `name` from t_bayonet_personnel tbp where tbp.id = t1.opponent_id) opponent")
                .FROM("t_bayonet_battle_achievement t1 left join t_bayonet_hit_area_config t2 on t1.hit_area_id = t2.id");

        return sql.toString();
    }
}
