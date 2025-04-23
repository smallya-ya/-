package com.ruoyi.battle.battle.controller;

import  com.ruoyi.battle.battle.service.ShootingVestService;
import  com.ruoyi.battle.license.service.LicenseService;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hongjiasen
 */
@Api(value = "射击演习马甲操作controller", tags = {"实兵演习马甲操作操作接口"})
@RestController
@RequestMapping(value = "/vest")
public class ShootingVestController {

    @Autowired
    private LicenseService licenseService;
    @Autowired
    private ShootingVestService shootingVestService;

    @ApiOperation(value = "获取所有马甲信息")
    @GetMapping
    public BaseResponse getAllVest() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(shootingVestService.getAllVest())
                .build();
    }

    @ApiOperation(value = "设置所有马甲的姓名信息")
    @PostMapping(value = "/reNameAll")
    public BaseResponse reNmaeAllVest(@RequestBody List<ShootingVestModel> nameList) {

        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(shootingVestService.setAllVestNanme(nameList))
                .build();
    }

    @ApiOperation(value = "马甲判死")
    @GetMapping(value = "/die/{vestNum}")
    public BaseResponse sendVestDie(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendDieData(vestNum, licenseService.getMachineName());
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判死成功")
                .build();
    }

    @ApiOperation(value = "马甲判活")
    @GetMapping(value = "/live/{vestNum}")
    public BaseResponse sendVestLive(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendLiveData(vestNum);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判活成功")
                .build();
    }

    @ApiOperation(value = "马甲判伤")
    @GetMapping(value = "/injure/{vestNum}")
    public BaseResponse sendVestInjure(@PathVariable("vestNum") int vestNum) throws InterruptedException {
        shootingVestService.sendInjureData(vestNum);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("判伤成功")
                .build();
    }

    @ApiOperation(value = "马甲全体充弹")
    @GetMapping(value = "/reloadAll")
    public BaseResponse sendReloadAll(@ApiParam(value = "子弹数") @RequestParam(value = "ammo") int ammo
            , @ApiParam(value = "武器类型：主武器-2；副武器-3") @RequestParam(value = "weapon") int weapon) throws InterruptedException {
        if (weapon != (byte) Constant.PRIMARY_WEAPON && weapon != (byte) Constant.SECONDARY_WEAPON) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_BAD_REQUEST)
                    .status(Constant.FAIL_STATUS)
                    .msg("武器代码必须是0x02或者0x03")
                    .build();
        }
        shootingVestService.reloadAllVest(ammo, weapon);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("全体充弹成功")
                .build();
    }

    @ApiOperation(value = "指定马甲充弹")
    @GetMapping(value = "/reload")
    public BaseResponse reload(@ApiParam(value = "马甲编号") @RequestParam(value = "vestNum") int vestNum
            , @ApiParam(value = "子弹数") @RequestParam(value = "ammo") int ammo
            , @ApiParam(value = "武器类型：主武器-2；副武器-3") @RequestParam(value = "weapon") int weapon) throws InterruptedException {
        if (weapon != (byte) Constant.PRIMARY_WEAPON && weapon != (byte) Constant.SECONDARY_WEAPON) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(HttpServletResponse.SC_BAD_REQUEST)
                    .status(Constant.FAIL_STATUS)
                    .msg("武器代码必须是0x02或者0x03")
                    .build();
        }
        shootingVestService.reloadVestAmmo(vestNum, ammo, weapon);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg(vestNum + "号充弹成功")
                .build();
    }
}
