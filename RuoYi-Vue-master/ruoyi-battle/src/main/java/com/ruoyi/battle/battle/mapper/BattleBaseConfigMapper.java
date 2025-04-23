package com.ruoyi.battle.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.battle.battle.domain.BattleBaseConfigEntity;
import com.ruoyi.battle.battle.domain.BattleSettingQueryDO;
import com.ruoyi.battle.battle.domain.BattleSettingVO;
import com.ruoyi.battle.battle.domain.BattleSettingVO2;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author hongjiasen
 */
@Mapper
public interface BattleBaseConfigMapper extends BaseMapper<BattleBaseConfigEntity> {

    @SelectProvider(type = BattleBaseConfigProvider.class, method = "listBattleSettingByQuery")
    @Results(id = "battleSettingVO", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "mode", property = "mode"),
            @Result(column = "map_id", property = "mapId"),
            @Result(column = "map_name", property = "mapName"),
            @Result(column = "map_type", property = "mapType"),
            @Result(column = "id", property = "teamData"
                    , many = @Many(select = " com.ruoyi.battle.battle.mapper.BattleTeamConfigMapper.queryByBaseConfigId"
                    , fetchType = FetchType.EAGER))
    })
    List<BattleSettingVO> listBattleSettingByQuery(BattleSettingQueryDO battleSettingQueryDO);

    @SelectProvider(type = BattleBaseConfigProvider.class, method = "listBattleSettingByQuery")
    @Results(id = "battleSettingVO2", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "mode", property = "mode"),
            @Result(column = "map_id", property = "mapId"),
            @Result(column = "map_name", property = "mapName"),
            @Result(column = "map_type", property = "mapType"),
            @Result(column = "id", property = "teamData"
                    , many = @Many(select = " com.ruoyi.battle.battle.mapper.BattleTeamConfig2Mapper.queryByBaseConfigId"
                    , fetchType = FetchType.EAGER))
    })
    List<BattleSettingVO2> listBattleSettingByQuery2(BattleSettingQueryDO battleSettingQueryDO);
}
