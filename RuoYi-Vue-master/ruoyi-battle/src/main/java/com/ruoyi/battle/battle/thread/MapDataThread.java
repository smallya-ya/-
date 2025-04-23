package com.ruoyi.battle.battle.thread;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import  com.ruoyi.battle.battle.context.ShootingBattleContext;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.Point;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import com.ruoyi.battle.utils.DataFrameUtils;
import com.ruoyi.battle.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * @author hongjiasen
 */
public class MapDataThread implements Runnable, BaseMapDataThread {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ShootingBattleContext context;
    private BaseMapEntity mapEntity;
    private Collection<ShootingVestModel> vestModelList;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;
    private String originMapImgBase64;
    private String mapImgBase64;
    private ListMultimap<Integer, Point> pointMap;

    public MapDataThread(ShootingBattleContext context, BaseMapEntity mapEntity
            , Collection<ShootingVestModel> vestModelList
            , BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) throws IOException {
        this.context = context;
        this.mapEntity = mapEntity;
        this.vestModelList = vestModelList;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;

        byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
        this.originMapImgBase64 = new String(Base64.getEncoder().encode(data));
        pointMap = ArrayListMultimap.create();
    }

    @Override
    public void run() {
        double width = Double.parseDouble(mapEntity.getLng());
        double height = Double.parseDouble(mapEntity.getLat());

        while (!this.context.isStop()) {
            Map<Integer, Point> vestPointMap = new HashMap<>(vestModelList.size());
            Map<Integer, Color> vestColorMap = new HashMap<>(vestModelList.size());

            for(ShootingVestModel vestEntity : vestModelList) {
                if (vestEntity.getLng().doubleValue() > 0 && vestEntity.getLng().doubleValue() <= width && vestEntity.getLat().doubleValue() > 0 && vestEntity.getLat().doubleValue() <= height) {
                    log.info("[绘图线程]:{}号绘制于点{},{}", vestEntity.getNum(), vestEntity.getLng(), vestEntity.getLat());
                    vestPointMap.put(vestEntity.getNum(), new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
                    pointMap.put(vestEntity.getNum(), new Point(vestEntity.getLng().doubleValue(), vestEntity.getLat().doubleValue()));
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
                } else {
                    if (vestEntity.getLat().compareTo(BigDecimal.ZERO) != 0 || vestEntity.getLng().compareTo(BigDecimal.ZERO) != 0) {
                        log.error("[绘图线程]:{}号当前坐标[{},{}]超出画图 下发超出范围预警", vestEntity.getNum(), vestEntity.getLng(), vestEntity.getLat());
                        vestEntity.setLat(BigDecimal.ZERO);
                        vestEntity.setLng(BigDecimal.ZERO);
                        SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                                .aSendDataMissionModel()
                                .vestNum(0)
                                .isWait(false)
                                .priority(Constant.HIGH_PRIORITY)
                                .data(DataFrameUtils.createOutOfRangeData(vestEntity.getNum()))
                                .dateTime(LocalDateTime.now())
                                .build();
                        try {
                            rollingMissionBlockingQueue.put(data);
                        } catch (InterruptedException e) {
                            log.error("[绘图线程]:下发超出范围预警发生异常", e);
                            continue;
                        }
                    }
                }
            }
            mapImgBase64 = "data:image/jpg;base64," + ImageUtils.pressBatchBackGroudText(vestPointMap, pointMap, originMapImgBase64, "宋体", Font.BOLD, 40, Color.white, vestColorMap);
        }
        log.info("[绘图线程]：结束");
    }

    @Override
    public String getMapImgBase64() {
        return StrUtil.isNotBlank(mapImgBase64) ? mapImgBase64 : "data:image/jpg;base64," + originMapImgBase64;
    }
}
