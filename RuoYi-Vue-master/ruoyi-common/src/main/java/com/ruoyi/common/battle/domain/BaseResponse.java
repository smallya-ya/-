package com.ruoyi.common.battle.domain;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hongjiasen
 */
public class BaseResponse<T> {

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "响应状态")
    private String status;

    @ApiModelProperty(value = "响应信息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    public static final class BaseResponseBuilder {
        private int code;
        private String status;
        private String msg;
        private Object data;

        private BaseResponseBuilder() {
        }

        public static BaseResponseBuilder aBaseResponse() {
            return new BaseResponseBuilder();
        }

        public BaseResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public BaseResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BaseResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public BaseResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public BaseResponse build() {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode(code);
            baseResponse.setStatus(status);
            baseResponse.setMsg(msg);
            baseResponse.setData(data);
            return baseResponse;
        }
    }
}
