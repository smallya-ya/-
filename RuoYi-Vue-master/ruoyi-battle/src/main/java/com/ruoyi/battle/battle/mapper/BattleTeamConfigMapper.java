package com.ruoyi.battle.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BattleTeamConfigMapper extends BaseMapper<BattleTeamConfigEntity> {

    @Select("select * from t_battle_team_config where battle_base_config_id = #{id} order by team")
    @Results(id = "battleTeamConfigEntity", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "battle_base_config_id", property = "battleBaseConfigId"),
            @Result(column = "team", property = "team"),
            @Result(column = "start_num", property = "startNum"),
            @Result(column = "end_num", property = "endNum"),
            @Result(column = "primary_weapon", property = "primaryWeapon"),
            @Result(column = "secondary_weapon", property = "secondaryWeapon")
    })
    List<BattleTeamConfigEntity> queryByBaseConfigId(Long id);
}
