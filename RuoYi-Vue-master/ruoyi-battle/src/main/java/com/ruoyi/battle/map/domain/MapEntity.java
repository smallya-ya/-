package com.ruoyi.battle.map.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_map")
public class MapEntity extends BaseMapEntity {

    @ApiModelProperty(value = "地图名")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static final class MapEntityBuilder {
        protected Long id;
        private String lng;
        private String lat;
        private String zoom;
        private String name;
        private String remark;
        private int type = 0;
        private String path;

        private MapEntityBuilder() {
        }

        public static MapEntityBuilder aMapEntity() {
            return new MapEntityBuilder();
        }

        public MapEntityBuilder lng(String lng) {
            this.lng = lng;
            return this;
        }

        public MapEntityBuilder lat(String lat) {
            this.lat = lat;
            return this;
        }

        public MapEntityBuilder zoom(String zoom) {
            this.zoom = zoom;
            return this;
        }

        public MapEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MapEntityBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public MapEntityBuilder type(int type) {
            this.type = type;
            return this;
        }

        public MapEntityBuilder path(String path) {
            this.path = path;
            return this;
        }

        public MapEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MapEntity build() {
            MapEntity mapEntity = new MapEntity();
            mapEntity.setLng(lng);
            mapEntity.setLat(lat);
            mapEntity.setZoom(zoom);
            mapEntity.setName(name);
            mapEntity.setRemark(remark);
            mapEntity.setType(type);
            mapEntity.setPath(path);
            mapEntity.setId(id);
            return mapEntity;
        }
    }
}
