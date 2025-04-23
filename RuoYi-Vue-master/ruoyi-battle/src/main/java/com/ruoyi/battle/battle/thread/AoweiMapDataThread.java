package com.ruoyi.battle.battle.thread;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.battle.battle.context.BattleContext;
import com.ruoyi.battle.battle.controller.ShootingBattleWebSocketController;
import com.ruoyi.battle.map.domain.AoweiMapEntity;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.battle.shootingvest.domian.Point;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.battle.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hongjiasen
 */
public class AoweiMapDataThread implements Runnable, BaseMapDataThread {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BattleContext context;
    private final BaseMapEntity mapEntity0;
    private final Collection<ShootingVestModel> vestModelList;
    private final String originMapImgBase64;
    private String mapImgBase64;
    private final InputStream inputStream;
    private final int imgWidth;
    private final int imgHeight;

    public AoweiMapDataThread(BattleContext context, BaseMapEntity mapEntity
            , Collection<ShootingVestModel> vestModelList) throws IOException {
        this.context = context;
        this.mapEntity0 = mapEntity;
        this.vestModelList = vestModelList;

        byte[] data = Files.readAllBytes(Paths.get(mapEntity.getPath()));
        this.originMapImgBase64 = new String(Base64.getEncoder().encode(data));

        this.inputStream = new ByteArrayInputStream(data);
        Image src = ImageIO.read(inputStream);
        this.imgWidth = src.getWidth(null);
        this.imgHeight = src.getHeight(null);
    }

    @Override
    public void run() {
        AoweiMapEntity mapEntity = (AoweiMapEntity) mapEntity0;
        double width = Double.parseDouble(mapEntity.getRightDownLng()) - Double.parseDouble(mapEntity.getLeftTopLng());
        double height = Double.parseDouble(mapEntity.getLeftTopLat()) - Double.parseDouble(mapEntity.getRightDownLat());

        while (!this.context.isStop()) {
            Map<Integer, Point> vestPointMap = new HashMap<>(vestModelList.size());
            Map<Integer, Color> vestColorMap = new HashMap<>(vestModelList.size());

            for (ShootingVestModel vestEntity : vestModelList) {
                if (vestEntity.getWgs84Lat().compareTo(new BigDecimal(mapEntity.getLeftTopLat())) > 0
                        || vestEntity.getWgs84Lat().compareTo(new BigDecimal(mapEntity.getRightDownLat())) < 0
                        || vestEntity.getWgs84Lng().compareTo(new BigDecimal(mapEntity.getLeftTopLng())) < 0
                        || vestEntity.getWgs84Lng().compareTo(new BigDecimal(mapEntity.getRightDownLng())) > 0) {
                    log.error("[绘图线程]:{}号当前WGS-84坐标[{},{}]超出画图", vestEntity.getNum(), vestEntity.getWgs84Lng(), vestEntity.getWgs84Lat());
                    continue;
                }

                double x = (vestEntity.getWgs84Lng().doubleValue() - Double.parseDouble(mapEntity.getLeftTopLng())) / width * imgWidth;
                double y = (Double.parseDouble(mapEntity.getLeftTopLat()) - vestEntity.getWgs84Lat().doubleValue()) / height * imgHeight;
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
            mapImgBase64 = "data:image/jpg;base64," + ImageUtils.pressBatchBackGroudText(vestPointMap, originMapImgBase64, "宋体", Font.BOLD, 40, Color.white, vestColorMap);
            try {
                ShootingBattleWebSocketController.notice();
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        IoUtil.close(inputStream);
        log.info("[绘图线程]：结束");
    }

    @Override
    public String getMapImgBase64() {
        return StrUtil.isNotBlank(mapImgBase64) ? mapImgBase64 : "data:image/jpg;base64," + originMapImgBase64;
    }
}
