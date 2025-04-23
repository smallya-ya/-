package com.ruoyi.common.battle.domain;

import java.io.Serializable;

/**
 * 用于包装MQTT消息
 *
 * @author hongjiasen
 */
public class MqttMsg implements Serializable {

    private String data;

    public MqttMsg() {
    }

    public MqttMsg(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MqttMessage{" +
                "data='" + data + '\'' +
                '}';
    }
}
