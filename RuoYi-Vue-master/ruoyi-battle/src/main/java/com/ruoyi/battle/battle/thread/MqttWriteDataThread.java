package com.ruoyi.battle.battle.thread;

import cn.hutool.core.util.HexUtil;
import  com.ruoyi.battle.battle.context.BattleContext;
import  com.ruoyi.battle.mqtt.domain.MqttProperty;
import com.ruoyi.common.battle.domain.SendDataMissionModel;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * @author hongjiasen
 */
public class MqttWriteDataThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private BattleContext context;
    private MqttProperty mqttProperty;
    private MqttClient mqttClient;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public MqttWriteDataThread(BattleContext context, MqttProperty mqttProperty
            , MqttClient mqttClient, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.context = context;
        this.mqttProperty = mqttProperty;
        this.mqttClient = mqttClient;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[MQTT任务控制线程]：启动");
        while (!this.context.isStop()) {
            try {
                SendDataMissionModel sendDataMissionModel = rollingMissionBlockingQueue.take();
                MqttMessage message = new MqttMessage(sendDataMissionModel.getData());
                message.setQos(mqttProperty.getQosLevel());
                log.info("[MQTT任务控制线程]：发送{}", HexUtil.encodeHexStr(sendDataMissionModel.getData()));
                mqttClient.publish(mqttProperty.getProducerTopic(), message);
            } catch (InterruptedException e) {
                log.error("[MQTT任务控制线程]：任务结束");
            } catch (Exception e) {
                log.error("[MQTT任务控制线程]：发送数据时出现异常", e);
            }
        }
        log.info("[MQTT任务控制线程]：结束");
    }
}
