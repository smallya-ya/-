package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetBattleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetBattleMapper extends BaseMapper<BayonetBattleEntity> {

    @Select(" select id from t_bayonet_battle where config_id = #{configId} ")
    @ResultType(Long.class)
    List<Long> queryByConfigId(@Param("configId") Long configId);
}
