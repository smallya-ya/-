package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionGroupEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionGroupRecordDetailVO;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionGroupRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetCompetitionGroupMapper extends BaseMapper<BayonetCompetitionGroupEntity> {

   @Select(" <script> "
           + " select t1.id,t1.create_time happen_time,t1.battle_id, "
           + " (select competition_name from t_bayonet_competition_config tbcc where tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) battle, "
           + " (select `type` from t_bayonet_competition_config tbcc where tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) `type`, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.memberId1) m1, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.memberId2) m2, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.winner) winner, "
           + " (unix_timestamp(t1.update_time) - unix_timestamp(t1.create_time)) use_time "
           + " from t_bayonet_battle_group_record t1 where 1 = 1 "
           + " <if test='type != null'> and exists (select 1 from t_bayonet_competition_config tbcc where tbcc.`type` = #{type} and tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) </if> "
           + " order by t1.create_time desc "
           + " </script> ")
   @ResultType(BayonetCompetitionGroupRecordVO.class)
   List<BayonetCompetitionGroupRecordVO> getByQuery(@Param("type") Integer type);

   @Select(" <script> "
           + " select t1.id,t1.create_time happen_time,t1.battle_id, "
           + " (select competition_name from t_bayonet_competition_config tbcc where tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) battle, "
           + " (select `type` from t_bayonet_competition_config tbcc where tbcc.id=(select tbb.config_id from t_bayonet_battle tbb where tbb.id = t1.battle_id)) `type`, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.memberId1) m1, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.memberId2) m2, "
           + " (select `name` from t_bayonet_personnel t2 where t2.id = t1.winner) winner, "
           + " (unix_timestamp(t1.update_time) - unix_timestamp(t1.create_time)) use_time "
           + " from t_bayonet_battle_group_record t1 where t1.battle_id = #{battleId} "
           + " </script> ")
   @ResultType(BayonetCompetitionGroupRecordVO.class)
   List<BayonetCompetitionGroupRecordVO> getBattleResultByBattleId(@Param("battleId") Long battleId);

   @Select(" select " +
           " v1.id,v1.attacker name, " +
           " max(case v1.hit_area when '头部' then hit_times else 0 end) head, " +
           " max(case v1.hit_area when '左胸' then hit_times else 0 end) left_chest, " +
           " max(case v1.hit_area when '右胸' then hit_times else 0 end) right_chest, " +
           " max(case v1.hit_area when '左臂' then hit_times else 0 end) left_arm, " +
           " max(case v1.hit_area when '右臂' then hit_times else 0 end) right_arm, " +
           " max(case v1.hit_area when '左腹' then hit_times else 0 end) left_abdomen, " +
           " max(case v1.hit_area when '右腹' then hit_times else 0 end) right_abdomen, " +
           " max(case v1.hit_area when '左肋' then hit_times else 0 end) left_rib, " +
           " max(case v1.hit_area when '右肋' then hit_times else 0 end) right_rib, " +
           " sum(v1.hit_times2) total_hit_times, " +
           " sum(v1.hit_score) total_hit_score, " +
           " sum(v1.hit_strength) total_hit_strength " +
           " from " +
           " (select t1.id ,t1.battle_group_record_id record_id , " +
           " (select `name` from t_bayonet_personnel t10 where t10.id = t1.attacker_id) attacker , " +
           " (select `name` from t_bayonet_personnel t10 where t10.id = t1.opponent_id) opponent , " +
           " (select hit_area_name from t_bayonet_hit_area_config t11 where t11.id = t1.hit_area_id) hit_area , " +
           " (select `score` from t_bayonet_hit_score t66 where t66.hit_area_id=t1.hit_area_id and t66.plan_id=(select t77.score_plan_id from t_bayonet_competition_config t77 where t77.id = (select t78.config_id from t_bayonet_battle t78 where t78.id = t1.battle_id))) as hit_score, " +
           " t1.happen_time ,t1.hit_strength ,t1.hit_times ,1 as hit_times2 " +
           " from t_bayonet_battle_achievement t1 where t1.battle_group_record_id = #{recordId}) v1 " +
           " group by v1.attacker ")
   List<BayonetCompetitionGroupRecordDetailVO> getDetailByRecordId(@Param("recordId") Long recordId);
}

