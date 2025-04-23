package com.ruoyi.common.battle.domain;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class HeartbeatResponse {

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "响应状态")
    private String status;

    @ApiModelProperty(value = "响应信息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private Object data;

    @ApiModelProperty(value = "心跳响应")
    private String heartTag;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getHeartTag() {
        return heartTag;
    }

    public void setHeartTag(String heartTag) {
        this.heartTag = heartTag;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = SpringUtil.getBean(ObjectMapper.class);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    public static final class HeartbeatResponseBuilder {
        private int code;
        private String status;
        private String msg;
        private Object data;
        private String heartTag;

        private HeartbeatResponseBuilder() {
        }

        public static HeartbeatResponseBuilder aHeartbeatResponse() {
            return new HeartbeatResponseBuilder();
        }

        public HeartbeatResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public HeartbeatResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public HeartbeatResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public HeartbeatResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public HeartbeatResponseBuilder heartTag(String heartTag) {
            this.heartTag = heartTag;
            return this;
        }

        public HeartbeatResponse build() {
            HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
            heartbeatResponse.setCode(code);
            heartbeatResponse.setStatus(status);
            heartbeatResponse.setMsg(msg);
            heartbeatResponse.setData(data);
            heartbeatResponse.setHeartTag(heartTag);
            return heartbeatResponse;
        }
    }
}
