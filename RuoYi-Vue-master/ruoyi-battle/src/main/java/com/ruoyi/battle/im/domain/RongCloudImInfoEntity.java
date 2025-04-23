package com.ruoyi.battle.im.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.battle.domain.CommonEntity;

/**
 * @author hongjiasen
 */
@TableName("t_rongcloud_token")
public class RongCloudImInfoEntity extends CommonEntity {

    protected int num;

    protected String token;

    protected String userId;

    public RongCloudImInfoEntity() {
    }

    public RongCloudImInfoEntity(int num, String token, String userId) {
        this.num = num;
        this.token = token;
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
