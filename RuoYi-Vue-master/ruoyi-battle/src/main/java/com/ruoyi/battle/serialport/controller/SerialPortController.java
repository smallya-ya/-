package com.ruoyi.battle.serialport.controller;

import  com.ruoyi.battle.serialport.service.SerialIndoorPortService;
import  com.ruoyi.battle.serialport.service.SerialPortService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hongjiasen
 */
@Api(value = "串口controller", tags = {"串口操作接口"})
@RestController
@RequestMapping(value = "/serialPort")
public class SerialPortController {

    @Autowired
    private SerialPortService serialPortService;

    @Autowired
    private SerialIndoorPortService serialIndoorPortService;

    @ApiOperation(value = "开启串口", notes = "主要使用的串口，使用串口模式时必须使用")
    @GetMapping(value = "/open")
    public BaseResponse openPort(@ApiParam("串口名") @RequestParam("portName") String portName
            , @ApiParam("波特率") @RequestParam("baudRate") int baudRate) {
        return serialPortService.openSerialPort(portName,baudRate);
    }

    @ApiOperation(value = "开启虚拟串口", notes = "主要用于室内演习，非室内演习可不使用")
    @GetMapping(value = "/indoor/open")
    public BaseResponse openIndoorPort(@ApiParam("串口名") @RequestParam("portName")String portName
            , @ApiParam("波特率") @RequestParam("baudRate")int baudRate){
        return serialIndoorPortService.openSerialPort(portName,baudRate);
    }

    @ApiOperation(value = "关闭串口")
    @GetMapping(value = "/close")
    public BaseResponse closePort() {
        serialPortService.closePort("主动关闭");
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "关闭虚拟串口")
    @GetMapping(value = "/indoor/close")
    public BaseResponse closeIndoorPort() {
        serialIndoorPortService.closePort("主动关闭");
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "查询串口列表")
    @GetMapping(value = "/list")
    public BaseResponse listPort() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialPortService.findPorts())
                .build();
    }

    @ApiOperation(value = "获取当前使用的串口")
    @GetMapping(value = "")
    public BaseResponse getCom(){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialPortService.getNowComStatus())
                .build();
    }

    @ApiOperation(value = "获取当前使用的虚拟串口")
    @GetMapping(value = "/indoor")
    public BaseResponse getVirtualCom(){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(serialIndoorPortService.getNowComStatus())
                .build();
    }
}
