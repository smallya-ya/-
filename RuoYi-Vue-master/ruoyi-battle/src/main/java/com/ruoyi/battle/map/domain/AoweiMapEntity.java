package com.ruoyi.battle.map.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author hongjiasen
 */
@TableName("t_battle_aowei_map")
public class AoweiMapEntity extends BaseMapEntity {

    @NotNull
    @ApiModelProperty(value = "左上角经度", required = true)
    private String leftTopLng;

    @NotNull
    @ApiModelProperty(value = "左上角纬度", required = true)
    private String leftTopLat;

    @NotNull
    @ApiModelProperty(value = "右下角经度", required = true)
    private String rightDownLng;

    @NotNull
    @ApiModelProperty(value = "右下角纬度", required = true)
    private String rightDownLat;

    @NotNull
    @ApiModelProperty(value = "地图名", required = true)
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "地图保存路径")
    private String path;

    public String getLeftTopLng() {
        return leftTopLng;
    }

    public void setLeftTopLng(String leftTopLng) {
        this.leftTopLng = leftTopLng;
    }

    public String getLeftTopLat() {
        return leftTopLat;
    }

    public void setLeftTopLat(String leftTopLat) {
        this.leftTopLat = leftTopLat;
    }

    public String getRightDownLng() {
        return rightDownLng;
    }

    public void setRightDownLng(String rightDownLng) {
        this.rightDownLng = rightDownLng;
    }

    public String getRightDownLat() {
        return rightDownLat;
    }

    public void setRightDownLat(String rightDownLat) {
        this.rightDownLat = rightDownLat;
    }

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


    public static final class AoweiMapEntityBuilder {
        protected int type = 0;
        protected Long id;
        private String leftTopLng;
        private String leftTopLat;
        private String rightDownLng;
        private String rightDownLat;
        private String name;
        private String remark;
        private String path;

        private AoweiMapEntityBuilder() {
        }

        public static AoweiMapEntityBuilder anAoweiMapEntity() {
            return new AoweiMapEntityBuilder();
        }

        public AoweiMapEntityBuilder leftTopLng(String leftTopLng) {
            this.leftTopLng = leftTopLng;
            return this;
        }

        public AoweiMapEntityBuilder leftTopLat(String leftTopLat) {
            this.leftTopLat = leftTopLat;
            return this;
        }

        public AoweiMapEntityBuilder rightDownLng(String rightDownLng) {
            this.rightDownLng = rightDownLng;
            return this;
        }

        public AoweiMapEntityBuilder rightDownLat(String rightDownLat) {
            this.rightDownLat = rightDownLat;
            return this;
        }

        public AoweiMapEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AoweiMapEntityBuilder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public AoweiMapEntityBuilder path(String path) {
            this.path = path;
            return this;
        }

        public AoweiMapEntityBuilder type(int type) {
            this.type = type;
            return this;
        }

        public AoweiMapEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AoweiMapEntity build() {
            AoweiMapEntity aoweiMapEntity = new AoweiMapEntity();
            aoweiMapEntity.setLeftTopLng(leftTopLng);
            aoweiMapEntity.setLeftTopLat(leftTopLat);
            aoweiMapEntity.setRightDownLng(rightDownLng);
            aoweiMapEntity.setRightDownLat(rightDownLat);
            aoweiMapEntity.setName(name);
            aoweiMapEntity.setRemark(remark);
            aoweiMapEntity.setPath(path);
            aoweiMapEntity.setType(type);
            aoweiMapEntity.setId(id);
            return aoweiMapEntity;
        }
    }
}
