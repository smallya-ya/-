package com.ruoyi.battle.license.controller;

import  com.ruoyi.battle.license.domain.RegisterRequestDO;
import  com.ruoyi.battle.license.service.LicenseService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hongjiasen
 */
@Api(value = "license controller", tags = {"license操作接口"})
@RestController
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @ApiOperation(value = "查询当前实例是否已注册")
    @GetMapping("/register")
    public BaseResponse isRegistered() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(licenseService.isRegistered() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_FORBIDDEN)
                .status(Constant.SUCCESS_STATUS)
                .msg(licenseService.isRegistered() ? "" : "未注册")
                .data(licenseService.isRegistered())
                .build();

    }

    @ApiOperation(value = "查询当前实例机器名")
    @GetMapping("/name")
    public BaseResponse getMachineName() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(licenseService.getMachineName())
                .build();

    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterRequestDO request){
        return licenseService.register(request);
    }

}
