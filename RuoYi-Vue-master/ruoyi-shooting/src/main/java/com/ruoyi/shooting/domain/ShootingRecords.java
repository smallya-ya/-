package com.ruoyi.shooting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 射击打靶对象 shooting_records
 * 
 * @author 司小雅
 * @date 2025-03-07
 */
public class ShootingRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 射击用户id，连sys_user */
    @Excel(name = "射击用户id，连sys_user")
    private Long uId;

    /** 射击用户民 */
    @Excel(name = "射击用户民")
    private String uName;

    /** 第几发子弹 */
    @Excel(name = "第几发子弹")
    private Long bulletNumber;

    /** 射击轮次名 */
    @Excel(name = "射击轮次名")
    private String roundName;

    /** 环数 */
    @Excel(name = "环数")
    private String ring;

    /** 偏向 */
    @Excel(name = "偏向")
    private String direction;

    /** 射击时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "射击时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date shootingTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setuId(Long uId) 
    {
        this.uId = uId;
    }

    public Long getuId() 
    {
        return uId;
    }
    public void setuName(String uName) 
    {
        this.uName = uName;
    }

    public String getuName() 
    {
        return uName;
    }
    public void setBulletNumber(Long bulletNumber) 
    {
        this.bulletNumber = bulletNumber;
    }

    public Long getBulletNumber() 
    {
        return bulletNumber;
    }
    public void setRoundName(String roundName) 
    {
        this.roundName = roundName;
    }

    public String getRoundName() 
    {
        return roundName;
    }
    public void setRing(String ring) 
    {
        this.ring = ring;
    }

    public String getRing() 
    {
        return ring;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setShootingTime(Date shootingTime) 
    {
        this.shootingTime = shootingTime;
    }

    public Date getShootingTime() 
    {
        return shootingTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uId", getuId())
            .append("uName", getuName())
            .append("bulletNumber", getBulletNumber())
            .append("roundName", getRoundName())
            .append("ring", getRing())
            .append("direction", getDirection())
            .append("shootingTime", getShootingTime())
            .toString();
    }
}
