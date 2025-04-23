package com.ruoyi.battle.bayonet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelQueryDO;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BayonetPersonnelMapper extends BaseMapper<BayonetPersonnelEntity> {

    @SelectProvider(type = BayonetPersonnelProvider.class, method = "queryList")
    @ResultType(BayonetPersonnelVO.class)
    List<BayonetPersonnelVO> queryList(@Param("query") BayonetPersonnelQueryDO query);

    @SelectProvider(type = BayonetPersonnelProvider.class, method = "queryById")
    @ResultType(BayonetPersonnelVO.class)
    BayonetPersonnelVO queryById(@Param("id")Long id);
}
