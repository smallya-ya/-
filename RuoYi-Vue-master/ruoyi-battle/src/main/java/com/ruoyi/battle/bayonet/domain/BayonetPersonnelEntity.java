package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_personnel")
public class BayonetPersonnelEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "组织ID", required = true)
    private Long unitId;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "编号", required = true)
    private int num;

//    @ApiModelProperty(value = "性别:0-男；1-女", required = true)
//    private int gender;
//
//    @ApiModelProperty(value = "年龄", required = true)
//    private int age;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

//    public int getGender() {
//        return gender;
//    }
//
//    public void setGender(int gender) {
//        this.gender = gender;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
