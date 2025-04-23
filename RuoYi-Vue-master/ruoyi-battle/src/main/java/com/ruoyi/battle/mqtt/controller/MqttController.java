package com.ruoyi.battle.mqtt.controller;

import  com.ruoyi.battle.mqtt.domain.MqttProperty;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import  com.ruoyi.common.utils.battle.AliyunEmqxSSLFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletResponse;

/**
 * 主用用于测试mqtt连接是否可用
 * @author hongjiasen
 */
@Api(value = "mqtt controller", tags = {"MQTT连通性测试接口"})
@RestController
@RequestMapping("mqtt")
public class MqttController {

    private Logger log = LoggerFactory.getLogger(getClass());

    private MqttProperty mqttProperty;

    public MqttController(MqttProperty mqttProperty) {
        this.mqttProperty = mqttProperty;
    }

    @ApiOperation(value = "测试是否能连接MQTT服务器")
    @GetMapping("test")
    public BaseResponse test() throws Exception {
        log.info("[MQTT管理]：clientId -> {}", mqttProperty.getClientId());
        log.info("[MQTT管理]：上位机下发指令所使用的topic -> {}", mqttProperty.getProducerTopic());
        log.info("[MQTT管理]：下位机上报数据所使用的topic -> {}", mqttProperty.getConsumerTopic());
        log.info("[MQTT管理]：qos -> {}", mqttProperty.getQosLevel());
        log.info("[MQTT管理]：userName -> {}", mqttProperty.getUsername());
        log.info("[MQTT管理]：connectionTimeout -> {}", mqttProperty.getTimeout());
        log.info("[MQTT管理]：keepAliveInterval -> {}", mqttProperty.getKeepalive());
        log.info("[MQTT管理]：cleanSession -> {}", mqttProperty.getCleanSession());
        log.info("[MQTT管理]：是否使用ssl -> {}", mqttProperty.getSslUse());
        if (mqttProperty.getSslUse()) {
            log.info("[MQTT管理]：ca证书 -> {}", mqttProperty.getSslCa());
            log.info("[MQTT管理]：客户端秘钥 -> {}", mqttProperty.getSslClientKey());
            log.info("[MQTT管理]：客户端ca证书 -> {}", mqttProperty.getSslClientCa());
        }
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient mqttClient = new MqttClient(mqttProperty.getHost(), mqttProperty.getClientId(), persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        if (mqttProperty.getSslUse()) {
            SSLSocketFactory sslSocketFactory
                    = AliyunEmqxSSLFactory.getSSLSocktet(mqttProperty.getSslCa()
                    , mqttProperty.getSslClientCa(), mqttProperty.getSslClientKey(), mqttProperty.getSslPassword());
            connOpts.setSocketFactory(sslSocketFactory);
        }
        connOpts.setUserName(mqttProperty.getUsername());
        connOpts.setPassword(mqttProperty.getPassword().toCharArray());
        connOpts.setConnectionTimeout(mqttProperty.getTimeout());
        connOpts.setKeepAliveInterval(mqttProperty.getKeepalive());
        // 用于测试连通性，cleanSession为true，不使用配置文件中的属性
        connOpts.setCleanSession(true);
        try {
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    log.info("[MQTT管理]：连接成功！");
                }

                @Override
                public void connectionLost(Throwable cause) {
                    log.error("[MQTT管理]：连接失败！", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            mqttClient.connect(connOpts);
        } catch (Exception e) {
            log.error("[MQTT管理]：连接失败！", e);
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .status(Constant.FAIL_STATUS)
                    .msg("连接失败，" + e.getMessage())
                    .build();
        } finally {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            mqttClient.close();
            persistence.clear();
            persistence.close();
        }
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("连接成功！")
                .build();
    }
}
