package com.ruoyi.battle.battle.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import  com.ruoyi.battle.battle.context.*;
import com.ruoyi.battle.battle.domain.*;
import  com.ruoyi.battle.battle.mapper.*;
import  com.ruoyi.battle.im.service.RongCloudImInfoService;
import com.ruoyi.battle.map.domain.AoweiMapEntity;
import com.ruoyi.battle.map.domain.MapEntity;
import  com.ruoyi.battle.map.mapper.AoweiMapEntityMapper;
import  com.ruoyi.battle.map.mapper.MapEntityMapper;
import  com.ruoyi.battle.serialport.service.SerialIndoorPortService;
import  com.ruoyi.battle.serialport.service.SerialPortService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.battle.utils.ImageUtils;
import jssc.SerialPortException;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.battle.shootingvest.domian.Point;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hongjiasen
 */
@Service
public class BattleService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BattleBaseConfigMapper battleBaseConfigMapper;
    @Autowired
    private BattleTeamConfigMapper battleTeamConfigMapper;
    @Autowired
    private BattleTeamConfig2Mapper battleTeamConfig2Mapper;
    @Autowired
    private BattleMapper battleMapper;
    @Autowired
    private BattleLogMapper battleLogMapper;
    @Autowired
    private BattleRecordMapper battleRecordMapper;
    @Autowired
    private BattleRecordDetailMapper battleRecordDetailMapper;
    @Autowired
    private MapEntityMapper mapEntityMapper;
    @Autowired
    private AoweiMapEntityMapper aoweiMapEntityMapper;
    @Autowired
    private SerialPortService serialPortService;
    @Autowired
    private SerialIndoorPortService serialIndoorPortService;
    @Autowired
    private RongCloudImInfoService rongCloudImInfoService;
    @Autowired
    private ShootingAppInfoService shootingAppInfoService;

    public static BattleContext context;


    @PreDestroy
    @Transactional(noRollbackFor = { SerialPortException.class, InterruptedException.class, MqttException.class })
    public void destroy() throws Exception {
        if (Objects.nonNull(context)) {
            log.error("[演习管理]：异常关闭程序，终止正在进行的实兵演习-{}", context.getBattleEntity().getName());
            context.getBattleEntity().setEndTime(LocalDateTime.now());
            battleMapper.updateById(context.getBattleEntity());
            context.stop(false);
            context = null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createBattleSetting(BattleSettingDO battleSettingDO) {
        BattleBaseConfigEntity battleBaseConfigEntity = BattleBaseConfigEntity.BattleBaseConfigEntityBuilder
                .aBattleBaseConfigEntity().mapId(battleSettingDO.getMapId()).name(battleSettingDO.getName())
                .mode(battleSettingDO.getMode()).type(0).build();
        battleBaseConfigMapper.insert(battleBaseConfigEntity);
        for (BattleTeamConfigEntity battleTeamConfigEntity : battleSettingDO.getTeamData()) {
            battleTeamConfigEntity.setBattleBaseConfigId(battleBaseConfigEntity.getId());
            battleTeamConfigMapper.insert(battleTeamConfigEntity);
        }


        for (BattleTeamConfigEntity battleTeamConfigEntity : battleSettingDO.getTeamData()) {
            if (Objects.nonNull(battleTeamConfigEntity.getStartNum()) && Objects.nonNull(battleTeamConfigEntity.getEndNum())) {
                rongCloudImInfoService.batchRegister(battleTeamConfigEntity.getStartNum(), battleTeamConfigEntity.getEndNum());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createBattleSetting2(BattleSettingDO2 battleSettingDO) {
        BattleBaseConfigEntity battleBaseConfigEntity = BattleBaseConfigEntity.BattleBaseConfigEntityBuilder
                .aBattleBaseConfigEntity().mapId(battleSettingDO.getMapId()).name(battleSettingDO.getName())
                .mode(battleSettingDO.getMode()).type(1).build();
        battleBaseConfigMapper.insert(battleBaseConfigEntity);
        for (BattleTeamConfigEntity2 battleTeamConfigEntity : battleSettingDO.getTeamData()) {
            battleTeamConfigEntity.setBattleBaseConfigId(battleBaseConfigEntity.getId());
            battleTeamConfig2Mapper.insert(battleTeamConfigEntity);
        }
    }

//    @Transactional
    public BattleEntity startBattle(Long id, int mode) throws Exception {
        BattleBaseConfigEntity battleBaseConfigEntity = battleBaseConfigMapper.selectById(id);
        log.info("[演习管理]：开始演习{}", battleBaseConfigEntity.getName());
        String logg = "开始演习";


        LambdaQueryWrapper<BattleTeamConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(BattleTeamConfigEntity::getBattleBaseConfigId, id);
        List<BattleTeamConfigEntity> battleTeamConfigEntityList = battleTeamConfigMapper.selectList(query);
        MapEntity mapEntity = mapEntityMapper.selectById(battleBaseConfigEntity.getMapId());

        BattleEntity battleEntity = BattleEntity.BattleEntityBuilder.aBattleEntity()
                .name(battleBaseConfigEntity.getName()).beginTime(LocalDateTime.now()).isLoadAmmo(0).isLoadAmmo2(0)
                .ammo(0).ammo2(0)
                .mapId(battleBaseConfigEntity.getMapId()).status(1).mapType(mapEntity.getType())
                .remark(battleBaseConfigEntity.getId().toString()).build();
        battleMapper.insert(battleEntity);

        if (Objects.nonNull(context)) {
            log.error("[演习管理]：当前存在正在进行的演习{}，进行关闭", context.getBattleEntity().getName());
            this.endBattle(true);
        }

        if (Constant.BATTLE_SERIALPORT_MODE == mode) {
            if (Constant.INDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity,
                        battleTeamConfigEntityList, mapEntity, serialPortService.getSerialPort(),
                        serialIndoorPortService.getSerialPort());
            } else if (Constant.OUTDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity,
                        battleTeamConfigEntityList, mapEntity, serialPortService.getSerialPort(), null);
            } else {
                AoweiMapEntity aoweiMapEntity = aoweiMapEntityMapper.selectById(battleBaseConfigEntity.getMapId());
                context = new AoweiBattleSerialPortContext(battleEntity, battleBaseConfigEntity, battleTeamConfigEntityList
                        , aoweiMapEntity, serialPortService.getSerialPort(), null);
            }
        } else {
            if (Constant.OUTDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleMqttContext(battleEntity, battleBaseConfigEntity
                        , battleTeamConfigEntityList, mapEntity);
            } else if (Constant.AOWEI_TYPE == mapEntity.getType()) {
                AoweiMapEntity aoweiMapEntity = aoweiMapEntityMapper.selectById(battleBaseConfigEntity.getMapId());
                context = new AoweiBattleMqttContext(battleEntity, battleBaseConfigEntity
                        , battleTeamConfigEntityList, aoweiMapEntity);
            }
        }

        context.init();
        context.start();
        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.START_BATTLE_TPYE)
                .log(logg)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);
//        shootingAppInfoService.init(battleTeamConfigEntityList);
        return battleEntity;
    }

    public BattleEntity startBattle2(Long id, int mode) throws Exception {
        BattleBaseConfigEntity battleBaseConfigEntity = battleBaseConfigMapper.selectById(id);
        log.info("[演习管理]：开始演习{}", battleBaseConfigEntity.getName());
        String logg = "开始演习";

        BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                .aBattleLogEntity()
                .battleId(BattleService.context.getBattleEntity().getId())
                .time(LocalDateTime.now())
                .type(Constant.START_BATTLE_TPYE)
                .log(logg)
                .isShow(0)
                .build();
        battleLogMapper.insert(battleLogEntity);
        LambdaQueryWrapper<BattleTeamConfigEntity2> query = Wrappers.lambdaQuery();
        query.eq(BattleTeamConfigEntity2::getBattleBaseConfigId, id);
        List<BattleTeamConfigEntity2> battleTeamConfigEntityList = battleTeamConfig2Mapper.selectList(query);
        MapEntity mapEntity = mapEntityMapper.selectById(battleBaseConfigEntity.getMapId());

        BattleEntity battleEntity = BattleEntity.BattleEntityBuilder.aBattleEntity()
                .name(battleBaseConfigEntity.getName()).beginTime(LocalDateTime.now()).isLoadAmmo(0).isLoadAmmo2(0)
                .ammo(0).ammo2(0)
                .mapId(battleBaseConfigEntity.getMapId()).status(1).mapType(mapEntity.getType())
                .remark(battleBaseConfigEntity.getId().toString()).build();
        battleMapper.insert(battleEntity);

        if (Objects.nonNull(context)) {
            log.error("[演习管理]：当前存在正在进行的演习{}，进行关闭", context.getBattleEntity().getName());
            this.endBattle(true);
        }

        if (Constant.BATTLE_SERIALPORT_MODE == mode) {
            if (Constant.INDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity,
                        mapEntity, serialPortService.getSerialPort(), serialIndoorPortService.getSerialPort()
                        , battleTeamConfigEntityList);
            } else if (Constant.OUTDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleSerialPortContext(battleEntity, battleBaseConfigEntity
                        , mapEntity, serialPortService.getSerialPort(), null
                        , battleTeamConfigEntityList);
            } else {
                AoweiMapEntity aoweiMapEntity = aoweiMapEntityMapper.selectById(battleBaseConfigEntity.getMapId());
                context = new AoweiBattleSerialPortContext(battleEntity, battleBaseConfigEntity
                        , mapEntity, serialPortService.getSerialPort(), null
                        , battleTeamConfigEntityList);
            }
        } else {
            if (Constant.OUTDOOR_TYPE == mapEntity.getType()) {
                context = new ShootingBattleMqttContext(battleEntity, battleBaseConfigEntity
                        , mapEntity, battleTeamConfigEntityList);
            } else if (Constant.AOWEI_TYPE == mapEntity.getType()) {
                AoweiMapEntity aoweiMapEntity = aoweiMapEntityMapper.selectById(battleBaseConfigEntity.getMapId());
                context = new AoweiBattleMqttContext(battleEntity, battleBaseConfigEntity, aoweiMapEntity
                        , battleTeamConfigEntityList);
            }

        }

        context.init();
        context.start();
        return battleEntity;
    }

//    @Transactional(noRollbackFor = { SerialPortException.class, InterruptedException.class, MqttException.class })
    public void endBattle(boolean exist) throws Exception {
        if (Objects.nonNull(context)) {
            log.info("[演习管理]：结束演习{}", context.getBattleEntity().getName());

            String logg = "结束演习";

            BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                    .aBattleLogEntity()
                    .battleId(BattleService.context.getBattleEntity().getId())
                    .time(LocalDateTime.now())
                    .type(Constant.END_BATTLE_TPYE)
                    .log(logg)
                    .isShow(0)
                    .build();
            battleLogMapper.insert(battleLogEntity);
            context.getBattleEntity().setEndTime(LocalDateTime.now());
            battleMapper.updateById(context.getBattleEntity());
            context.stop(false);
            context = null;
//            shootingAppInfoService.clear();
        } else {
            log.info("[演习管理]：当前无正在进行的演习");
        }
    }

    @Transactional(readOnly = true)
    public PageInfo<BattleSettingVO> listBattleSettingByQuery(BattleSettingQueryDO battleSettingQueryDO) {
        PageHelper.startPage(battleSettingQueryDO.getPage(), battleSettingQueryDO.getPageSize());
        return new PageInfo<>(battleBaseConfigMapper.listBattleSettingByQuery(battleSettingQueryDO));
    }

    @Transactional(readOnly = true)
    public PageInfo<BattleSettingVO2> listBattleSettingByQuery2(BattleSettingQueryDO battleSettingQueryDO) {
        PageHelper.startPage(battleSettingQueryDO.getPage(), battleSettingQueryDO.getPageSize());
        return new PageInfo<>(battleBaseConfigMapper.listBattleSettingByQuery2(battleSettingQueryDO));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBattleSetting(Long id) {
        LambdaQueryWrapper<BattleTeamConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(BattleTeamConfigEntity::getBattleBaseConfigId, id);
        battleTeamConfigMapper.delete(query);

        LambdaQueryWrapper<BattleTeamConfigEntity2> query2 = Wrappers.lambdaQuery();
        query2.eq(BattleTeamConfigEntity2::getBattleBaseConfigId, id);
        battleTeamConfig2Mapper.delete(query2);

        battleBaseConfigMapper.deleteById(id);
    }

    @Transactional
    public BattleDetailVO getNowBattleDetail(LocalDateTime fromTime) {
        if (Objects.isNull(context)) {
            return null;
        }
//        else {
//            context.getBattleRecordThread().refeshHeartbeatTime();
//        }
        //加强判断----------------------------------------------------------------
//        if (context == null || context.getBattleEntity() == null) {
//            throw new IllegalArgumentException("Context or battle entity is null");
//        }
        // 验证 battleLogMapper 是否为 null
//        if (battleLogMapper == null) {
//            throw new IllegalStateException("battleLogMapper is not initialized");
//        }
//        if (battleLogMapper == null) {
//            new Exception("Stack trace").printStackTrace();
//            throw new IllegalStateException("battleLogMapper is not initialized");
//        }
        //-----------------------------------------------------------------------
        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, context.getBattleEntity().getId());
        logQuery.le(Objects.nonNull(fromTime), BattleLogEntity::getTime, fromTime);
        logQuery.orderByAsc(BattleLogEntity::getTime);
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);
//        log.info("Fetched {} battle logs", battleLogEntityList.size());
//        battleLogEntityList.forEach(log -> {
//            log.setIsShow(1);
//            battleLogMapper.updateById(log);
//            shootingAppInfoService.log(log);
//        });

        return BattleDetailVO.BattleDetailVOBuilder.aBattleDetailVO()
                .battleId(context.getBattleEntity().getId())
                .vestEntityList(context.getVestEntityMap().values())
                .mapBase64(Objects.nonNull(context.getMapDataThread()) ? context.getMapDataThread().getMapImgBase64()
                        : null)
                .mapEntity(context.getMapEntity()).battleLogEntityList(battleLogEntityList)
                .isLoadAmmo(context.getBattleEntity().getIsLoadAmmo())
                .isLoadAmmo2(context.getBattleEntity().getIsLoadAmmo2())
                .ammo(context.getBattleEntity().getAmmo())
                .ammo2(context.getBattleEntity().getAmmo2()).build();
    }


//--------------------------------------------------测试----------------------------------------------------------------------
@Transactional
public String startPeriodicBattleDetailQuery(LocalDateTime fromTime) {
    BattleDetailVO battleDetailVO = getNowBattleDetail(fromTime);
    if (battleDetailVO == null) {
        log.info("演习数据为空，无法获取");
        return "<p>{\"Action\":\"getPlanResultData\",\"Status\":\"error\",\"Data\":\"No data found\"}</p>";
    }

    // 构建 JSON 对象
    JSONObject result = new JSONObject();
    result.put("Action", "getPlanResultData");
    //BattleEntity.getBattleStatus(battleDetailVO.getBattleEntity().getStatus()).ifPresent(status -> result.put("Status", status));
    result.put("Status", "ok");
    JSONObject data = new JSONObject();
    data.put("id", battleDetailVO.getBattleId());

    // 演习日志
    JSONArray battleLogs = new JSONArray();
    for (BattleLogEntity log : battleDetailVO.getBattleLogEntityList()) {
        JSONObject logObj = new JSONObject();
        logObj.put("time", log.getTime().toString());
        logObj.put("log", log.getLog());
        battleLogs.add(logObj);
    }
    data.put("演习日志", battleLogs);

    // 红队统计
    JSONArray redTeamStats = buildTeamStats(battleDetailVO.getVestEntityList(), "red");
    data.put("红队统计", redTeamStats);
    // 蓝队统计
    JSONArray blueTeamStats = buildTeamStats(battleDetailVO.getVestEntityList(), "blue");
    data.put("蓝队统计", blueTeamStats);

    // 演习统计
    // 初始化红队统计对象
    JSONObject redbattleStats = new JSONObject();
    redbattleStats.put("color", "红队");
    redbattleStats.put("normal", 0);
    redbattleStats.put("qs", 0);
    redbattleStats.put("zs", 0);
    redbattleStats.put("zhs", 0);
    redbattleStats.put("zw", 0);
    // 初始化蓝队统计对象
    JSONObject bluebattleStats = new JSONObject();
    bluebattleStats.put("color", "蓝队");
    bluebattleStats.put("normal", 0);
    bluebattleStats.put("qs", 0);
    bluebattleStats.put("zs", 0);
    bluebattleStats.put("zhs", 0);
    bluebattleStats.put("zw", 0);

// 遍历红队所有队员统计状态
    for (Object obj : redTeamStats) {
        JSONObject member = (JSONObject) obj;
        String healthValueStr = member.getString("healthValue");
        int hp;
        try {
            hp = Integer.parseInt(healthValueStr);
        } catch (NumberFormatException e) {
            hp = 100;  // 或者设置默认值
            log.error(e.getMessage());
        }

        if (hp == 100) {
            redbattleStats.put("normal", Integer.parseInt(redbattleStats.getString("normal"))  + 1);
        } else if (hp == 75) {
            redbattleStats.put("qs", Integer.parseInt(redbattleStats.getString("qs")) + 1);
        } else if (hp == 50) {
            redbattleStats.put("zs", Integer.parseInt(redbattleStats.getString("zs")) + 1);
        } else if (hp ==25) {
            redbattleStats.put("zhs", Integer.parseInt(redbattleStats.getString("zhs")) + 1);
        } else {
            redbattleStats.put("zw", Integer.parseInt(redbattleStats.getString("zw")) + 1);
        }
    }
    // 遍历蓝队所有队员统计状态
    for (Object obj : blueTeamStats) {
        JSONObject member = (JSONObject) obj;
        String healthValueStr = member.getString("healthValue");
        int hp;
        try {
            hp = Integer.parseInt(healthValueStr);
        } catch (NumberFormatException e) {
            hp = 100;  // 或者设置默认值
            log.error(e.getMessage());
        }

        if (hp == 100) {
            bluebattleStats.put("normal", Integer.parseInt(bluebattleStats.getString("normal")) + 1);
        } else if (hp == 75) {
            bluebattleStats.put("qs", Integer.parseInt(bluebattleStats.getString("qs"))  + 1);
        } else if (hp == 50) {
            bluebattleStats.put("zs", Integer.parseInt(bluebattleStats.getString("zs")) + 1);
        } else if (hp ==25) {
            bluebattleStats.put("zhs", Integer.parseInt(bluebattleStats.getString("zhs")) + 1);
        } else {
            bluebattleStats.put("zw", Integer.parseInt(bluebattleStats.getString("zw")) + 1);
        }
    }
    data.put("红队演习统计", redbattleStats);
    data.put("蓝队演习统计", bluebattleStats);


//    // SIM 统计
//    JSONArray simStats = new JSONArray();
//    for (ShootingVestModel vest : battleDetailVO.getVestEntityList()) {
//        JSONObject simObj = new JSONObject();
//        simObj.put("id", vest.getId());//ID
//        simObj.put("name", vest.getName());//名字
//        simObj.put("ammunition1", vest.getAmmo1());//弹药量1
//        simObj.put("ammunition2", vest.getAmmo2());//弹药量2
//        simObj.put("healthValue", vest.getHp());//生命值
//        simObj.put("mainWeapon", vest.getWeapon1());//武器1
//        simObj.put("secondaryWeapon", vest.getWeapon2());//武器2
//        simObj.put("sim", "xxx");
//        simStats.add(simObj);
//    }
//    data.put("SIM", simStats);

    JSONArray orangeTeamStats = buildTeamStats(battleDetailVO.getVestEntityList(), "orange");
    data.put("人质统计", orangeTeamStats);
    JSONArray yellowTeamStats = buildTeamStats(battleDetailVO.getVestEntityList(), "yellow");
    data.put("劫匪统计", yellowTeamStats);
    result.put("Data", data);
    // 封装成 <p> 标签内的字符串
    byte[] utf8Bytes = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
    String utf8EncodedString = new String(utf8Bytes, StandardCharsets.UTF_8);
    return "<p>"
            + utf8EncodedString +
            "</p>";
}


//遍历vestEntities集合中的每个ShootingVestModel对象。
//检查对象所属队伍名称是否与teamName相同。
//如果匹配，则创建一个包含编号、姓名、两种弹药数量、生命值及两种武器信息的JSON对象，并添加到teamStats数组中。
//最终返回构建好的teamStats数组。
private JSONArray buildTeamStats(Collection<ShootingVestModel> vestEntities, String teamName) {
    JSONArray teamStats = new JSONArray();//创建一个JSON数组，用于存储队伍统计信息
    log.info("开始统计信息："+teamName+" 列表："+vestEntities);
    for (ShootingVestModel vest : vestEntities) {
        if (vest.getTeam().equals(teamName)) {
                if (vest.getStatus() ==  0){
                    continue;
                }
            JSONObject teamStat = new JSONObject();//创建一个JSON对象，用于存储当前队伍的统计信息
            teamStat.put("code", vest.getNum());//编号
            teamStat.put("name", vest.getName());//姓名
            teamStat.put("ammunition1", vest.getAmmo1());//弹药量1
            teamStat.put("ammunition2", vest.getAmmo2());//弹药量2
            teamStat.put("healthValue", vest.getHp());//生命值
            teamStat.put("mainWeapon", vest.getWeapon1());//主武器
            teamStat.put("secondaryWeapon", vest.getWeapon2());//副武器
            teamStats.add(teamStat);
        }
    }
    return teamStats;
}


//---------------------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public PageInfo<BattleEntity> listBattleByQuery(BattleQueryDO battleQueryDO) {
        LambdaQueryWrapper<BattleEntity> query = Wrappers.lambdaQuery();
        query.like(StrUtil.isNotBlank(battleQueryDO.getName()), BattleEntity::getName, battleQueryDO.getName());
        query.orderByDesc(BattleEntity::getBeginTime);
        PageHelper.startPage(battleQueryDO.getPage(), battleQueryDO.getPageSize());
        return new PageInfo<>(battleMapper.selectList(query));
    }

    @Cacheable(value = "default", key = "#battleId + '-' + #index")
    @Transactional(readOnly = true)
    public BattleRecordModelVO getBattleRecord(Long battleId, int index) throws IOException {
        BattleEntity battleEntity = battleMapper.selectById(battleId);

        LambdaQueryWrapper<BattleRecordEntity> recordQuery = Wrappers.lambdaQuery();
        recordQuery.eq(BattleRecordEntity::getBattleId, battleId);
        Long count = battleRecordMapper.selectCount(recordQuery);

        recordQuery.eq(BattleRecordEntity::getIndex, index > count ? count : index);
        BattleRecordEntity battleRecordEntity = battleRecordMapper.selectOne(recordQuery);

        LambdaQueryWrapper<BattleRecordDetailEntity> detailQuery = Wrappers.lambdaQuery();
        detailQuery.eq(BattleRecordDetailEntity::getRecordId, battleRecordEntity.getId());
        List<BattleRecordDetailEntity> battleRecordDetailEntityList = battleRecordDetailMapper.selectList(detailQuery);

        BattleRecordVO battleRecordVO = BeanUtil.copyProperties(battleRecordEntity, BattleRecordVO.class);
        battleRecordVO.setVestData(battleRecordDetailEntityList);

        MapEntity mapEntity = mapEntityMapper.selectById(battleEntity.getMapId());

        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, battleId).le(BattleLogEntity::getTime, battleRecordEntity.getTime());
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);

        BattleRecordModelVO battleRecordModelVO = BattleRecordModelVO.BattleRecordModelVOBuilder.aBattleRecordModelVO()
                .logList(battleLogEntityList).battleRecordVO(battleRecordVO).mapEntity(mapEntity).nowIndex(index)
                .totIndex(count).build();

        if (Constant.INDOOR_TYPE == mapEntity.getType()) {
            double width = Double.parseDouble(mapEntity.getLng());
            double highth = Double.parseDouble(mapEntity.getLat());
            byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
            String originMapImgBase64 = new String(Base64.getEncoder().encode(data));

            Map<Integer, Point> vestPointMap = new HashMap<>(battleRecordDetailEntityList.size());
            Map<Integer, Color> vestColorMap = new HashMap<>(battleRecordDetailEntityList.size());
            ListMultimap<Integer, Point> pointMap = ArrayListMultimap.create();
            List<BattleRecordDetailEntity> detailTrailList = battleRecordDetailMapper.detailTrail(battleId, battleRecordEntity.getTime());
            for (ShootingVestModel vestEntity : detailTrailList) {
                if (vestEntity.getLng().doubleValue() > 0 && vestEntity.getLng().doubleValue() <= width
                        && vestEntity.getLat().doubleValue() > 0 && vestEntity.getLat().doubleValue() <= highth) {
                    vestPointMap.put(vestEntity.getNum(),
                            new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
                    pointMap.put(vestEntity.getNum(),
                            new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
                    if (vestEntity.getHp() > 0) {
                        switch (vestEntity.getTeam()) {
                            case "red":
                                vestColorMap.put(vestEntity.getNum(), Color.red);
                                break;
                            case "blue":
                                vestColorMap.put(vestEntity.getNum(), Color.blue);
                                break;
                            case "orange":
                                vestColorMap.put(vestEntity.getNum(), Color.orange);
                                break;
                            case "yellow":
                                vestColorMap.put(vestEntity.getNum(), new Color(165, 42, 42));
                                break;
                            default:
                                break;
                        }
                    } else {
                        vestColorMap.put(vestEntity.getNum(), Color.black);
                    }
                }
            }
            StringBuilder sb = new StringBuilder("data:image/jpg;base64,");
            sb.append(ImageUtils.pressBatchBackGroudText(vestPointMap, pointMap, originMapImgBase64, "宋体", Font.BOLD,
                    40, Color.white, vestColorMap));
            battleRecordModelVO.setMapBase64(sb.toString());
        }

        if (Constant.AOWEI_TYPE == mapEntity.getType()) {
            AoweiMapEntity mapEntity0 = aoweiMapEntityMapper.selectById(battleEntity.getMapId());
            double width = Double.parseDouble(mapEntity0.getRightDownLng()) - Double.parseDouble(mapEntity0.getLeftTopLng());
            double height = Double.parseDouble(mapEntity0.getLeftTopLat()) - Double.parseDouble(mapEntity0.getRightDownLat());
            byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
            String originMapImgBase64 = new String(Base64.getEncoder().encode(data));
            InputStream inputStream = new ByteArrayInputStream(data);
            Image src = ImageIO.read(inputStream);
            int imgWidth = src.getWidth(null);
            int imgHeight = src.getHeight(null);

            Map<Integer, Point> vestPointMap = new HashMap<>(battleRecordDetailEntityList.size());
            Map<Integer, Color> vestColorMap = new HashMap<>(battleRecordDetailEntityList.size());
            List<BattleRecordDetailEntity> detailTrailList = battleRecordDetailMapper.detailTrail(battleId, battleRecordEntity.getTime());
            for (ShootingVestModel vestEntity : detailTrailList) {
                if (vestEntity.getWgs84Lat().compareTo(new BigDecimal(mapEntity0.getLeftTopLat())) > 0
                        || vestEntity.getWgs84Lat().compareTo(new BigDecimal(mapEntity0.getRightDownLat())) < 0
                        || vestEntity.getWgs84Lng().compareTo(new BigDecimal(mapEntity0.getLeftTopLng())) < 0
                        || vestEntity.getWgs84Lng().compareTo(new BigDecimal(mapEntity0.getRightDownLng())) > 0) {
                    continue;
                }
                double x = (vestEntity.getWgs84Lng().doubleValue() - Double.parseDouble(mapEntity0.getLeftTopLng())) / width * imgWidth;
                double y = (Double.parseDouble(mapEntity0.getLeftTopLat()) - vestEntity.getWgs84Lat().doubleValue()) / height * imgHeight;
                log.info("[绘图线程]:{}号绘制于点{},{}", vestEntity.getNum(), x, y);
                vestPointMap.put(vestEntity.getNum(), new Point(x, y));
                if (vestEntity.getHp() > 0) {
                    switch (vestEntity.getTeam()) {
                        case "red":
                            vestColorMap.put(vestEntity.getNum(), Color.red);
                            break;
                        case "blue":
                            vestColorMap.put(vestEntity.getNum(), Color.blue);
                            break;
                        case "orange":
                            vestColorMap.put(vestEntity.getNum(), Color.orange);
                            break;
                        case "yellow":
                            vestColorMap.put(vestEntity.getNum(), new Color(165, 42, 42));
                            break;
                        default:
                            break;
                    }
                } else {
                    vestColorMap.put(vestEntity.getNum(), Color.black);
                }
            }
            String mapImgBase64 = "data:image/jpg;base64," + ImageUtils.pressBatchBackGroudText(vestPointMap, originMapImgBase64, "宋体", Font.BOLD, 40, Color.white, vestColorMap);
            battleRecordModelVO.setMapBase64(mapImgBase64);
            IoUtil.close(inputStream);
        }

        return battleRecordModelVO;
    }

    @Transactional
    public void deleteBattle(Long id) {
        battleMapper.deleteById(id);

        LambdaQueryWrapper<BattleRecordEntity> recordQuery = Wrappers.lambdaQuery();
        recordQuery.eq(BattleRecordEntity::getBattleId, id);
        List<BattleRecordEntity> battleRecordEntityList = battleRecordMapper.selectList(recordQuery);

        battleRecordDetailMapper.deleteBatchIds(
                battleRecordEntityList.stream().map(BattleRecordEntity::getId).collect(Collectors.toList()));

        battleRecordMapper.delete(recordQuery);
    }

    @Transactional
    public void deleteAllBattleLog() {
        battleRecordMapper.delete(null);
        battleLogMapper.delete(null);
        battleMapper.delete(null);
    }

    @Transactional(readOnly = true)
    public void exportExcel(Long battleId, BattleStatisticsDO battleStatisticsDO, HttpServletResponse response)
            throws UnsupportedEncodingException {
        ExcelWriter writer = ExcelUtil.getWriter(true);

        LambdaQueryWrapper<BattleLogEntity> logQuery = Wrappers.lambdaQuery();
        logQuery.eq(BattleLogEntity::getBattleId, battleId);
        logQuery.orderByAsc(BattleLogEntity::getTime);
        List<BattleLogEntity> battleLogEntityList = battleLogMapper.selectList(logQuery);

        writer.addHeaderAlias("time", "时间");
        writer.addHeaderAlias("log", "演习日志");
        writer.setOnlyAlias(true);
        writer.autoSizeColumnAll();
        writer.write(battleLogEntityList, true);
        writer.renameSheet(0, "演习日志");

        writer.setSheet(1);
        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getRed())) {
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getRed(), true);
        }
        writer.renameSheet(1, "红队统计");

        writer.setSheet(2);
        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getBlue())) {
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getBlue(), true);
        }
        writer.renameSheet(2, "蓝队统计");

        writer.setSheet(3);
        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getOrange())) {
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("hp", "生命值");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getOrange(), true);
        }
        writer.renameSheet(3, "人质统计");

        writer.setSheet(4);
        if (CollectionUtils.isNotEmpty(battleStatisticsDO.getYellow())) {
            writer.addHeaderAlias("num", "编号");
            writer.addHeaderAlias("name", "姓名");
            writer.addHeaderAlias("ammo1", "弹药量1");
            writer.addHeaderAlias("ammo2", "弹药量2");
            writer.addHeaderAlias("hp", "生命值");
            writer.addHeaderAlias("weapon1", "主武器");
            writer.addHeaderAlias("weapon2", "副武器");
            writer.setOnlyAlias(true);
            writer.write(battleStatisticsDO.getYellow(), true);
        }
        writer.renameSheet(4, "劫匪统计");

        List<BattleTeamStatisticsDO> teamStatisticsEntityList = new ArrayList<>(4);
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("红队", battleStatisticsDO.getRed()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("蓝队", battleStatisticsDO.getBlue()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("人质", battleStatisticsDO.getOrange()));
        teamStatisticsEntityList.add(new BattleTeamStatisticsDO("劫匪", battleStatisticsDO.getYellow()));
        writer.setSheet(5);
        writer.addHeaderAlias("teamName", "队伍");
        writer.addHeaderAlias("normal", "正常");
        writer.addHeaderAlias("slightWound", "轻伤");
        writer.addHeaderAlias("middleWound", "中伤");
        writer.addHeaderAlias("heavyWound", "重伤");
        writer.addHeaderAlias("die", "阵亡");
        writer.setOnlyAlias(true);
        writer.write(teamStatisticsEntityList, true);
        writer.renameSheet(5, "演习统计");

        List<ShootingVestModel> all = new ArrayList<>();
        all.addAll(battleStatisticsDO.getRed());
        all.addAll(battleStatisticsDO.getBlue());
        all.addAll(battleStatisticsDO.getOrange());
        all.addAll(battleStatisticsDO.getYellow());
        writer.setSheet(6);
        writer.addHeaderAlias("num", "马甲ID");
        writer.addHeaderAlias("simNo", "SIM");
        writer.setOnlyAlias(true);
        writer.write(all, true);
        writer.renameSheet(6, "SIM");

        if (Constant.TRUE.equals(SpringUtil.getProperty(Constant.FORCE_UP))) {
            List<ShootingVestModel> upList = all.stream()
                    .filter(vestModel -> vestModel.getRealStatus() != Constant.OFF_LINE_STATUS)
                    .collect(Collectors.toList());
            writer.setSheet(7);
            writer.write(upList, true);
            writer.renameSheet(7, "上线马甲列表");

            List<ShootingVestModel> downList = all.stream()
                    .filter(vestModel -> vestModel.getRealStatus() == Constant.OFF_LINE_STATUS)
                    .collect(Collectors.toList());
            writer.setSheet(8);
            writer.write(downList, true);
            writer.renameSheet(8, "未上线马甲列表");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("演习实况", "utf-8") + ".xlsx");
        try (OutputStream os = response.getOutputStream()) {
            writer.flush(os, true);
        } catch (Exception e) {
            log.error("导出演习实况时出现异常", e);
        }
    }
}
