package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetHitScoreEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetHitScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetHitScoreMapper extends BaseMapper<BayonetHitScoreEntity> {

    @Select("select t1.id,t1.hit_area_id,t2.hit_code,t2.hit_area_name,t1.plan_id,t1.score from t_bayonet_hit_score t1,t_bayonet_hit_area_config t2 where t1.hit_area_id = t2.id and t1.plan_id = #{id} ORDER BY t1.score")
    @ResultType(BayonetHitScoreVO.class)
    List<BayonetHitScoreVO> queryByBaseConfigId(@Param("id") Long id);
}
