package com.ruoyi.battle.battle.controller;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.battle.battle.domain.BattleVestInfo;
import  com.ruoyi.battle.battle.service.BattleService;
import  com.ruoyi.battle.battle.service.ShootingAppInfoService;
import com.ruoyi.battle.map.domain.BaseMapEntity;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hongjiasen
 */
@Api(value = "实兵演习controller", tags = {"实兵演习app端使用接口"})
@RestController
@RequestMapping("/app")
public class ShootingAppInfoController {

    @Autowired
    private ShootingAppInfoService shootingAppInfoService;

    @ApiOperation(value = "获取演习是否进行的状态")
    @GetMapping("/battleStatus")
    public BaseResponse battleStatus() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(null != BattleService.context ? HttpServletResponse.SC_OK : 315)
                .status(Constant.SUCCESS_STATUS)
                .msg(null != BattleService.context ? "当前正在进行演习" : "当前没有演习")
                .data(null != BattleService.context ? 1 : 0)
                .build();
    }

    @ApiOperation(value = "获取马甲演习日志")
    @GetMapping("/battleInfo/{num}")
    public BaseResponse getBattleInfo(@ApiParam(name = "num", value = "马甲编号") @PathVariable("num") int num) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(null != BattleService.context ? HttpServletResponse.SC_OK : 315)
                .status(Constant.SUCCESS_STATUS)
                .msg(null != BattleService.context ? "获取成功" : "当前没有演习")
                .data(null != BattleService.context ? shootingAppInfoService.getVestLog(num) : null)
                .build();
    }

    @ApiOperation(value = "获取马甲的信息")
    @GetMapping("/vest/{num}")
    public BaseResponse getVestInfo(@ApiParam(name = "num", value = "马甲编号") @PathVariable("num") int num) {
        BattleVestInfo info = shootingAppInfoService.getVestInfo(num);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(null != info ? HttpServletResponse.SC_OK : 315)
                .status(Constant.SUCCESS_STATUS)
                .data(info)
                .msg(null == info ? "当前没有演习" : "获取成功")
                .build();
    }

    @ApiOperation(value = "获取实兵演习百度地图信息")
    @GetMapping("/map")
    public BaseResponse getMapInfo() {
        BaseMapEntity map = shootingAppInfoService.getMapInfo();
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(null != map ? HttpServletResponse.SC_OK : 315)
                .status(Constant.SUCCESS_STATUS)
                .data(map)
                .msg(null == map ? "当前没有演习" : "获取成功")
                .build();
    }

    @ApiOperation(value = "获取马甲同一阵营的信息")
    @GetMapping("/teamInfo/{num}")
    public BaseResponse getVestListInfo(@ApiParam(name = "num", value = "马甲编号") @PathVariable("num") int num) {
        List<BattleVestInfo> list = shootingAppInfoService.getVestList(num);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(CollUtil.isNotEmpty(list) ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND)
                .status(Constant.SUCCESS_STATUS)
                .data(list)
                .msg(CollUtil.isNotEmpty(list) ? "获取成功" : "当前没有演习")
                .build();
    }
}
