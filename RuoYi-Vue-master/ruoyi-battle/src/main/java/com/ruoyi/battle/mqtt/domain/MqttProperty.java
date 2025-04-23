package com.ruoyi.battle.mqtt.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hongjiasen
 */
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperty {

    private String host;

    private String clientId;

    private String producerTopic;

    private String consumerTopic;

    private int qosLevel;

    private String username;

    private String password;

    private int timeout;

    private int keepalive;

    private boolean cleanSession;

    private boolean sslUse;

    private String sslCa;

    private String sslClientKey;

    private String sslClientCa;

    private String sslPassword;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProducerTopic() {
        return producerTopic;
    }

    public void setProducerTopic(String producerTopic) {
        this.producerTopic = producerTopic;
    }

    public String getConsumerTopic() {
        return consumerTopic;
    }

    public void setConsumerTopic(String consumerTopic) {
        this.consumerTopic = consumerTopic;
    }

    public int getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(int qosLevel) {
        this.qosLevel = qosLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean getSslUse() {
        return sslUse;
    }

    public void setSslUse(boolean sslUse) {
        this.sslUse = sslUse;
    }

    public String getSslCa() {
        return sslCa;
    }

    public void setSslCa(String sslCa) {
        this.sslCa = sslCa;
    }

    public String getSslClientKey() {
        return sslClientKey;
    }

    public void setSslClientKey(String sslClientKey) {
        this.sslClientKey = sslClientKey;
    }

    public String getSslClientCa() {
        return sslClientCa;
    }

    public void setSslClientCa(String sslClientCa) {
        this.sslClientCa = sslClientCa;
    }

    public String getSslPassword() {
        return null == this.sslPassword ? "" : this.sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }
}
