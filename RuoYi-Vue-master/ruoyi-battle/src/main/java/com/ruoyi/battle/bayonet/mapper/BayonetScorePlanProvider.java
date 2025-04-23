package com.ruoyi.battle.bayonet.mapper;

import cn.hutool.core.util.StrUtil;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanQueryDO;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author hongjiasen
 */
public class BayonetScorePlanProvider {

    public String queryList(BayonetScorePlanQueryDO query) {
        SQL sql = new SQL();
        sql.SELECT("id, plan_name, remark")
                .FROM("t_bayonet_score_plan")
                .WHERE(" 1 = 1 ");
        if (StrUtil.isNotBlank(query.getPlanName())) {
            sql.WHERE("plan_name like concat('%',#{query.planName},'%')");
        }
        if (StrUtil.isNotBlank(query.getRemark())) {
            sql.WHERE("remark like concat('%',#{query.remark},'%')");
        }
        sql.ORDER_BY("create_time desc");
        return sql.toString();
    }
}
