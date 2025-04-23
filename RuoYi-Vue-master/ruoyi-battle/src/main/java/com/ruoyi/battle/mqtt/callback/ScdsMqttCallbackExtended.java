package com.ruoyi.battle.mqtt.callback;

import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import  com.ruoyi.battle.mqtt.domain.MqttProperty;
import com.ruoyi.common.battle.InstructionProcessQueue;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;

/**
 * MQTT回调
 * @author hongjiasen
 */
public class ScdsMqttCallbackExtended implements MqttCallbackExtended {

    private Logger log = LoggerFactory.getLogger(getClass());

    private MqttProperty mqttProperty;
    private MqttConnectOptions connOpts;
    private MqttClient mqttClient;
    private InstructionProcessQueue instructionProcessThread;

    public ScdsMqttCallbackExtended(MqttProperty mqttProperty, MqttConnectOptions connOpts
            , MqttClient mqttClient, InstructionProcessQueue instructionProcessThread) {
        this.mqttProperty = mqttProperty;
        this.connOpts = connOpts;
        this.mqttClient = mqttClient;
        this.instructionProcessThread = instructionProcessThread;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        // 连接成功时进行MQTT订阅
        try {
            mqttClient.subscribe(mqttProperty.getConsumerTopic());
            log.info("[指令处理]：MQTT订阅主题{}成功", mqttProperty.getConsumerTopic());
        } catch (MqttException e) {
            log.error("[指令处理]：MQTT订阅主题{}失败", mqttProperty.getConsumerTopic());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("[指令处理]：MQTT断开连接，开始进行重连");
        RetryTemplate retryTemplate = SpringUtil.getBean(RetryTemplate.class);
        try {
            retryTemplate.execute(retryContext -> {
                log.info("[指令处理]：当前第{}次重连", retryContext.getRetryCount());
                mqttClient.connect(connOpts);
                log.info("[指令处理]：MQTT服务重连成功！");
                return mqttClient.isConnected();
            }, retryContext -> {
                log.error("[指令处理]：已达到最大重试次数{}，请检查网络或MQTT服务器", retryContext.getRetryCount(), retryContext.getLastThrowable());
                return false;
            });
        } catch (MqttException e) {
            log.error("[指令处理]：MQTT重连失败！", e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("[指令处理]：收到的数据长度[{}]", message.getPayload().length);
        log.info("[指令处理]：收到的数据[{}]", HexUtil.encodeHexStr(message.getPayload()));
        instructionProcessThread.getDataQueue().put(message.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // 消息传送完成，不做任何操作
    }
}
