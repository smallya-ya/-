package com.ruoyi.battle.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.battle.battle.domain.BattleRecordDetailEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BattleRecordDetailMapper extends BaseMapper<BattleRecordDetailEntity> {

    @Cacheable(value = "default", key = "#battleId + '-' + #time")
    @SelectProvider(type = BattleRecordDetailProvider.class, method = "detailTrail")
    @Results(id = "battleRecordDetailEntity", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "team", property = "team"),
            @Result(column = "num", property = "num"),
            @Result(column = "name", property = "name"),
            @Result(column = "weapon1", property = "weapon1"),
            @Result(column = "ammo1", property = "ammo1"),
            @Result(column = "weapon2", property = "weapon2"),
            @Result(column = "ammo2", property = "ammo2"),
            @Result(column = "hp", property = "hp"),
            @Result(column = "status", property = "status"),
            @Result(column = "weapon2", property = "weapon2"),
            @Result(column = "mode", property = "mode"),
            @Result(column = "lat", property = "lat"),
            @Result(column = "lng", property = "lng"),
            @Result(column = "wgs84_lat", property = "wgs84Lat"),
            @Result(column = "wgs84_lng", property = "wgs84Lng"),
            @Result(column = "record_id", property = "recordId")
    })
    List<BattleRecordDetailEntity> detailTrail(@Param("battleId") Long battleId, @Param("time") LocalDateTime time);
}
