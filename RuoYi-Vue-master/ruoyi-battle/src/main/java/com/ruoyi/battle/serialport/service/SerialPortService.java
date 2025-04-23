package com.ruoyi.battle.serialport.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.battle.service.BattleService;
import  com.ruoyi.battle.bayonet.service.BayonetService;
import  com.ruoyi.battle.serialport.controller.SerialPortWebSocketController;
import  com.ruoyi.battle.serialport.domain.SerialPortEntity;
import  com.ruoyi.battle.serialport.mapper.SerialPortMapper;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author hongjiasen
 */
@DependsOn("flywayConfig")
@Service
public class SerialPortService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SerialPortMapper serialPortMapper;

    private SerialPort serialPort;

    @PostConstruct
    public void clearComTable() {
        LambdaQueryWrapper<SerialPortEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialPortEntity::getType, 0);
        serialPortMapper.delete(query);
    }

    public SerialPortEntity getNowComStatus() {
        LambdaQueryWrapper<SerialPortEntity> query = Wrappers.lambdaQuery();
        query.eq(SerialPortEntity::getType, 0);
        return serialPortMapper.selectOne(query);
    }

    public List<String> findPorts() {
        return Arrays.asList(SerialPortList.getPortNames());
    }

    @Transactional
    public BaseResponse openSerialPort(String serialPortName, int baudRate) {
        try {
            log.info("[串口管理]：准备开启{}串口，波特率{}", serialPortName, baudRate);
            serialPort = new SerialPort(serialPortName);
            serialPort.openPort();
            serialPort.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            log.info("[串口管理]：开启{}串口成功，波特率{}", serialPortName, baudRate);
            this.clearComTable();
            SerialPortEntity serialPortEntity = SerialPortEntity.SerialPortEntityBuilder
                    .aSerialPortEntity()
                    .portName(serialPortName)
                    .baudRate(baudRate)
                    .status(Constant.NORMAL_STATUS)
                    .type(0)
                    .build();
            serialPortMapper.insert(serialPortEntity);
            SerialPortWebSocketController.noticeComStatus(serialPortEntity);
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_OK)
                    .msg("成功连接指定端口设备")
                    .build();
        } catch (SerialPortException e) {
            log.error("[串口管理]：连接串口时发生异常");
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .msg("连接串口时发生异常：" + e.getExceptionType())
                    .build();
        } catch (IOException e) {
            log.error("[串口管理]：WebSocket通信发生异常");
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .msg("WebSocket通信发生异常")
                    .build();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void closePort(String msg) {
        if (null == serialPort) {
            log.info("[串口管理]：串口目前未连接，不需要关闭");
            return;
        }
        if (null != BattleService.context || null != BayonetService.context) {
            log.info("[串口管理]：仍有演习在进行，请等演习结束再关闭串口");
            throw new RuntimeException("仍有演习在进行，请等演习结束再关闭串口");
        }
        log.info("[串口管理]：关闭串口{} {}", serialPort.getPortName(), msg);
        if (null != serialPort && serialPort.isOpened()) {
            try {
                SerialPortEntity serialPortEntity = SerialPortEntity.SerialPortEntityBuilder
                        .aSerialPortEntity()
                        .portName(serialPort.getPortName())
                        .status(Constant.UNNORMAL_STATUS)
                        .type(0)
                        .build();
                SerialPortWebSocketController.noticeComStatus(serialPortEntity);
                serialPort.closePort();
                this.clearComTable();
            } catch (SerialPortException | IOException e) {
                log.error("[串口管理]：关闭串口时发生异常", e);
            }
        }
    }

    public SerialPort getSerialPort() {
        Objects.requireNonNull(serialPort, "[串口管理]：通讯配置-普通模式请先配置室外天线接口");
        Assert.state(serialPort.isOpened(), "[串口管理]：通讯配置-普通模式-室外天线接口未打开，请重新配置");
        return serialPort;
    }
}
