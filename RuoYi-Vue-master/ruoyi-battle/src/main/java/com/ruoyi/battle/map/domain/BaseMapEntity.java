package com.ruoyi.battle.map.domain;

import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BaseMapEntity extends CommonEntity {

    /**
     * 地图类型
     * 0，室外地图，使用百度地图显示
     * 1，室内地图，使用上传图片显示
     * 2. 奥维地图
     */
    @ApiModelProperty(value = "地图类型：0-室外地图；1-室内地图；2-奥维地图")
    protected int type = 0;

    protected String lng;
    protected String lat;

    protected String zoom;

    protected String path;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
