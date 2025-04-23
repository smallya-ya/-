package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.battle.domain.CommonEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_competition_group")
public class BayonetCompetitionGroupEntity extends CommonEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "刺杀比赛配置ID")
    private Long configId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "人员ID1", required = true)
    @TableField(value = "memberId1")
    private Long memberId1;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "人员ID2", required = true)
    @TableField(value = "memberId2")
    private Long memberId2;

    public BayonetCompetitionGroupEntity() {
    }

    public BayonetCompetitionGroupEntity(Long configId, Long memberId1, Long memberId2) {
        this.configId = configId;
        this.memberId1 = memberId1;
        this.memberId2 = memberId2;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getMemberId1() {
        return memberId1;
    }

    public void setMemberId1(Long memberId1) {
        this.memberId1 = memberId1;
    }

    public Long getMemberId2() {
        return memberId2;
    }

    public void setMemberId2(Long memberId2) {
        this.memberId2 = memberId2;
    }
}
