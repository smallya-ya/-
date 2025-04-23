package com.ruoyi.battle.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.battle.battle.domain.BattleTeamConfigEntity2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BattleTeamConfig2Mapper extends BaseMapper<BattleTeamConfigEntity2> {

    @Select("select * from t_battle_team_config2 where battle_base_config_id = #{id} order by team")
    @Results(id = "battleTeamConfigEntity2", value = {
            @Result(column = "battleBaseConfigId", property = "battle_base_config_id"),
            @Result(column = "team", property = "team"),
            @Result(column = "nums", property = "nums"),
            @Result(column = "name", property = "name"),
            @Result(column = "primary_weapon", property = "primaryWeapon"),
            @Result(column = "secondary_weapon", property = "secondaryWeapon")
    })
    List<BattleTeamConfigEntity2> queryByBaseConfigId(Long id);
}
