package com.ruoyi.battle.map.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author hongjiasen
 */
public class AoweiMapDO {

    @NotNull
    @ApiModelProperty(value = "左上角坐标，逗号分隔，按经度,纬度", required = true)
    private String leftTop;

    @NotNull
    @ApiModelProperty(value = "右下角坐标，逗号分隔，按经度,纬度", required = true)
    private String rightDown;

    @NotNull
    @ApiModelProperty(value = "地图名", required = true)
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(String leftTop) {
        this.leftTop = leftTop;
    }

    public String getRightDown() {
        return rightDown;
    }

    public void setRightDown(String rightDown) {
        this.rightDown = rightDown;
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
}
