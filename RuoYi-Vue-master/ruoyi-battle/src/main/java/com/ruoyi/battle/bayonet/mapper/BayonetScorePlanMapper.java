package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanQueryDO;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetScorePlanMapper extends BaseMapper<BayonetScorePlanEntity> {

    @SelectProvider(type = BayonetScorePlanProvider.class, method = "queryList")
    @Results(id = "ScoreVO", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "plan_name", property = "planName"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "id", property = "hitScoreList"
                    , many = @Many(select = " com.ruoyi.battle.bayonet.mapper.BayonetHitScoreMapper.queryByBaseConfigId"
                    , fetchType = FetchType.EAGER))
    })
    List<BayonetScorePlanVO> queryList(@Param("query") BayonetScorePlanQueryDO query);
}
