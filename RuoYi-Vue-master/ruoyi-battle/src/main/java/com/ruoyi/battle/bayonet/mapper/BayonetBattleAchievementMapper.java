package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetBattleAchievementEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetBattleAchievementVO;
import  com.ruoyi.battle.bayonet.domain.BayonetGroupRecordDetailVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetBattleAchievementMapper extends BaseMapper<BayonetBattleAchievementEntity> {

    @SelectProvider(type = BayonetBattleAchievementProvider.class, method = "queryByBattleId")
    @ResultType(BayonetBattleAchievementVO.class)
    List<BayonetBattleAchievementVO> queryByBattleId(@Param("id") Long id);

    @Select(" select t1.id,t2.hit_area_name hit_area,t1.hit_strength,t1.happen_time, " +
            " (select `name` from t_bayonet_personnel tbp where tbp.id = t1.attacker_id) attacker, " +
            " (select `name` from t_bayonet_personnel tbp where tbp.id = t1.opponent_id) opponent " +
            " from t_bayonet_battle_achievement t1 left join t_bayonet_hit_area_config t2 on t1.hit_area_id = t2.id " +
            " where t1.battle_group_record_id = #{id} order by t1.happen_time")
    @ResultType(BayonetGroupRecordDetailVO.class)
    List<BayonetGroupRecordDetailVO> queryRecordDetailByRecordId(@Param("id") Long id);

    @Select(" select t1.`type` from t_bayonet_competition_config t1 where t1.id = (select config_id from t_bayonet_battle t2 where t2.id = (select battle_id from t_bayonet_battle_achievement t3 where t3.battle_group_record_id=#{id} limit 1)) ")
    @ResultType(Integer.class)
    Integer getTypeByRecordId(@Param("id") Long id);

    @Select(" select count(*) from t_bayonet_battle_achievement t1 where t1.battle_id in (select t2.id from t_bayonet_battle t2 where t2.config_id = #{id}) ")
    @ResultType(Long.class)
    Long countByConfigId(@Param("id") Long id);
}
