package com.ruoyi.battle.bayonet.mapper;

import cn.hutool.core.util.StrUtil;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelQueryDO;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author hongjiasen
 */
public class BayonetPersonnelProvider {

    public String queryList(BayonetPersonnelQueryDO query) {
        SQL sql = new SQL();
        sql.SELECT("t1.id, t1.`unit_id`, t2.`unit_name`, t1.`name`, t1.`num`, t1.`gender`, t1.`age`, t1.`remark`")
                .FROM("t_bayonet_personnel t1", "t_bayonet_unit t2")
                .WHERE("t1.`unit_id` = t2.id");
        if (null != query.getId()) {
            sql.WHERE("t1.`id` = #{query.id}");
        }
        if (null != query.getUnitId()) {
            sql.WHERE("t1.`unit_id` = #{query.unitId}");
        }
        if (StrUtil.isNotBlank(query.getUnitName())) {
            sql.WHERE("t2.`unit_name` like concat('%',#{query.unitName},'%')");
        }
        if (StrUtil.isNotBlank(query.getName())) {
            sql.WHERE("t1.`name` like concat('%',#{query.name},'%')");
        }
        if (null != query.getNum()) {
            sql.WHERE("t1.`num` = #{query.num}");
        }
        if (null != query.getGender()) {
            sql.WHERE("t1.`gender` = #{query.gender}");
        }
        if (null != query.getAge()) {
            sql.WHERE("t1.`age` = #{query.age}");
        }
        if (StrUtil.isNotBlank(query.getRemark())) {
            sql.WHERE("t1.`remark` like concat('%',#{query.remark},'%')");
        }
        sql.ORDER_BY("t1.`unit_id`", "t1.`num`");
        return sql.toString();
    }

    public String queryById(Long id) {
        SQL sql = new SQL();
        sql.SELECT("t1.id, t1.`unit_id`, t2.`unit_name`, t1.`name`, t1.`num`, t1.`gender`, t1.`age`, t1.`remark`")
                .FROM("t_bayonet_personnel t1", "t_bayonet_unit t2")
                .WHERE("t1.`unit_id` = t2.id and t1.`id` = #{id}");
        return sql.toString();
    }
}
