package com.ruoyi.battle.bayonet.mapper;

import cn.hutool.core.util.StrUtil;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigQueryDO;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionConfigProvider {

    public String queryList(BayonetCompetitionConfigQueryDO query) {
        SQL sql = new SQL();
        sql.SELECT("t1.id, t1.`serial_no`, t1.`competition_name`, t1.`score_plan_id`, t2.`plan_name` score_plan, t1.`limited_time`, t1.`distance`, t1.`remark`, t1.`type`, t1.total_hit_num")
                .FROM("t_bayonet_competition_config t1", "t_bayonet_score_plan t2")
                .WHERE("t1.`score_plan_id` = t2.id");
        if (null != query.getId()) {
            sql.WHERE("t1.`id` = #{query.id}");
        }
        if (null != query.getSerialNo()) {
            sql.WHERE("t1.`serial_no` = #{query.serialNo}");
        }
        if (StrUtil.isNotBlank(query.getCompetitionName())) {
            sql.WHERE("t1.`competition_name` like concat('%',#{query.competitionName},'%')");
        }
        if (StrUtil.isNotBlank(query.getScorePlan())) {
            sql.WHERE("t2.`plan_name` like concat('%',#{query.scorePlan},'%')");
        }
        if (null != query.getLimitedTime()) {
            sql.WHERE("t1.`limited_time` = #{query.limitedTime}");
        }
        if (StrUtil.isNotBlank(query.getDistance())) {
            sql.WHERE("t1.`distance` = #{query.distance}");
        }
        if (StrUtil.isNotBlank(query.getRemark())) {
            sql.WHERE("t1.`remark` like concat('%',#{query.remark},'%')");
        }
        sql.ORDER_BY("t1.create_time desc");
        return sql.toString();
    }

    public String getById(Long id) {
        SQL sql = new SQL();
        sql.SELECT("t1.id, t1.`serial_no`, t1.`competition_name`, t1.`score_plan_id`, t2.`plan_name` score_plan, t1.`limited_time`, t1.`distance`, t1.`remark`")
                .FROM("t_bayonet_competition_config t1", "t_bayonet_score_plan t2")
                .WHERE("t1.`score_plan_id` = t2.id and t1.id = #{id}");
        return sql.toString();
    }
}
