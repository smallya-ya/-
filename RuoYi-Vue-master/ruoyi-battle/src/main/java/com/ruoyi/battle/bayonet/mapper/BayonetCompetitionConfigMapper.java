package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigQueryDO;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetCompetitionConfigMapper extends BaseMapper<BayonetCompetitionConfigEntity> {

    @SelectProvider(type = BayonetCompetitionConfigProvider.class, method = "queryList")
    @ResultType(BayonetCompetitionConfigVO.class)
    List<BayonetCompetitionConfigVO> queryList(@Param("query") BayonetCompetitionConfigQueryDO query);

    @SelectProvider(type = BayonetCompetitionConfigProvider.class, method = "getById")
    @ResultType(BayonetCompetitionConfigVO.class)
    BayonetCompetitionConfigVO getById(@Param("id") Long id);
}
