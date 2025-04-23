package com.ruoyi.battle.map.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.battle.battle.domain.BattleBaseConfigEntity;
import com.ruoyi.battle.battle.domain.BattleEntity;
import  com.ruoyi.battle.battle.mapper.BattleBaseConfigMapper;
import  com.ruoyi.battle.battle.mapper.BattleMapper;
import com.ruoyi.battle.map.domain.AoweiMapDO;
import com.ruoyi.battle.map.domain.AoweiMapEntity;
import com.ruoyi.battle.map.domain.MapEntity;
import com.ruoyi.battle.map.domain.MapQueryDO;
import com.ruoyi.battle.map.mapper.AoweiMapEntityMapper;
import com.ruoyi.battle.map.mapper.MapEntityMapper;
import com.ruoyi.framework.config.battle.SnowflakeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hongjiasen
 */
@Service
public class AoweiMapService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MapEntityMapper mapEntityMapper;
    @Autowired
    private AoweiMapEntityMapper aoweiMapEntityMapper;
    @Autowired
    private BattleMapper battleMapper;
    @Autowired
    private BattleBaseConfigMapper battleBaseConfigMapper;
    @Autowired
    private SnowflakeConfig snowFlake;

    @Transactional(rollbackFor = IOException.class)
    public void save(MultipartFile file, AoweiMapDO entity) throws IOException {
        long id = snowFlake.snowflakeId();
        log.info("开始创建地图文件[{}]", entity.getName());
        long start = System.currentTimeMillis();
        String mapPath = "mapImg/" + id + "." + FileUtil.getSuffix(file.getOriginalFilename());
        File imgFile = new File(mapPath);
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        IoUtil.copy(file.getInputStream(), new FileOutputStream(imgFile));
        long end = System.currentTimeMillis();
        log.info("结束创建地图文件[{}]，用时[{}]ms", entity.getName(), end - start);

        String[] leftTopArr = entity.getLeftTop().split(",");
        String[] rightDownArr = entity.getRightDown().split(",");
        AoweiMapEntity map = AoweiMapEntity.AoweiMapEntityBuilder.anAoweiMapEntity()
                .id(id)
                .leftTopLng(leftTopArr[0])
                .leftTopLat(leftTopArr[1])
                .rightDownLng(rightDownArr[0])
                .rightDownLat(rightDownArr[1])
                .name(entity.getName())
                .remark(entity.getRemark())
                .type(2)
                .path(mapPath)
                .build();
        aoweiMapEntityMapper.insert(map);
        MapEntity map2 = MapEntity.MapEntityBuilder.aMapEntity()
                .id(id)
                .lat(entity.getRightDown())
                .lng(entity.getLeftTop())
                .zoom("0")
                .name(entity.getName())
                .remark(entity.getRemark())
                .type(2)
                .path(mapPath)
                .build();
        mapEntityMapper.insert(map2);
        long end2 = System.currentTimeMillis();
        log.info("创建地图记录[{}]，用时[{}]ms", entity.getName(), end2 - end);
    }

    @Transactional(rollbackFor = IOException.class)
    public void deleteMap(long id) throws IOException {
        AoweiMapEntity mapEntity = aoweiMapEntityMapper.selectById(id);
        log.info("开始删除地图文件[{}]", mapEntity.getName());
        long start = System.currentTimeMillis();
        Path mapFile = Paths.get(mapEntity.getPath());
        Files.deleteIfExists(mapFile);
        long end = System.currentTimeMillis();
        log.info("结束删除地图文件[{}]，用时[{}]ms", mapEntity.getName(), end - start);

        aoweiMapEntityMapper.deleteById(id);
        mapEntityMapper.deleteById(id);
        long end2 = System.currentTimeMillis();
        log.info("删除地图记录[{}]，用时[{}]ms", mapEntity.getName(), end2 - end);

        LambdaQueryWrapper<BattleEntity> battleEntityLambdaQueryWrapper = Wrappers.lambdaQuery();
        battleEntityLambdaQueryWrapper.eq(BattleEntity::getMapId, id);
        battleMapper.delete(battleEntityLambdaQueryWrapper);

        LambdaQueryWrapper<BattleBaseConfigEntity> battleBaseConfigMapperLambdaQueryWrapper = Wrappers.lambdaQuery();
        battleBaseConfigMapperLambdaQueryWrapper.eq(BattleBaseConfigEntity::getMapId, id);
        battleBaseConfigMapper.delete(battleBaseConfigMapperLambdaQueryWrapper);
        long end3 = System.currentTimeMillis();
        log.info("删除演习回放记录和演习配置[{}]，用时[{}]ms", mapEntity.getName(), end3 - end2);
    }

    @Transactional(readOnly = true)
    public PageInfo<AoweiMapEntity> listMapByQuery(MapQueryDO mapQueryDO) {
        LambdaQueryWrapper<AoweiMapEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(AoweiMapEntity::getCreateTime);
        query.like(StrUtil.isNotBlank(mapQueryDO.getName()), AoweiMapEntity::getName, mapQueryDO.getName());
        PageHelper.startPage(mapQueryDO.getPage(), mapQueryDO.getPageSize());
        return new PageInfo<>(aoweiMapEntityMapper.selectList(query));
    }

    @Transactional(readOnly = true)
    public AoweiMapEntity getMap(long id) {
        return aoweiMapEntityMapper.selectById(id);
    }

}
