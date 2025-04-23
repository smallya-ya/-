package com.ruoyi.battle.battle.controller;

import com.ruoyi.battle.battle.domain.*;
import com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */
@Api(value = "射击演习controller", tags = {"实兵演习操作接口"})
@RestController
@RequestMapping("/battle")
public class ShootingBattleController {

    @Autowired
    private BattleService battleService;

    @ApiOperation(value = "保存演习方案")
    @PostMapping(value = "/set")
    public BaseResponse createBattleSetting(@RequestBody BattleSettingDO battleSettingDO) {
        battleService.createBattleSetting(battleSettingDO);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("创建成功")
                .build();
    }

    @ApiOperation(value = "保存演习方案2")
    @PostMapping(value = "/set2")
    public BaseResponse createBattleSetting2(@RequestBody BattleSettingDO2 battleSettingDO) {
        battleService.createBattleSetting2(battleSettingDO);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("创建成功")
                .build();
    }

    @ApiOperation(value = "演习方案查询")
    @GetMapping(value = "/set/query")
    public BaseResponse queryBattleSetting(BattleSettingQueryDO battleSettingQueryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.listBattleSettingByQuery(battleSettingQueryDO))
                .build();
    }

    @ApiOperation(value = "演习方案查询")
    @GetMapping(value = "/set/query2")
    public BaseResponse queryBattleSetting2(BattleSettingQueryDO battleSettingQueryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.listBattleSettingByQuery2(battleSettingQueryDO))
                .build();
    }

    @ApiOperation(value = "演习方案删除")
    @DeleteMapping(value = "/set/{id}")
    public BaseResponse deleteBattleSetting(@PathVariable("id") Long id) {
        battleService.deleteBattleSetting(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "开始演习")
    @GetMapping(value = "/start/{id}/{mode}")
    public BaseResponse startBattle(@PathVariable("id") Long id
            , @ApiParam(name = "mode", value = "通信模式：串口-1，MQTT-2") @PathVariable("mode") int mode) throws Exception {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.startBattle(id, mode))
                .msg("开始演习")
                .build();
    }

    @ApiOperation(value = "开始演习")
    @GetMapping(value = "/start2/{id}/{mode}")
    public BaseResponse startBattle2(@PathVariable("id") Long id
            , @ApiParam(name = "mode", value = "通信模式：串口-1，MQTT-2") @PathVariable("mode") int mode) throws Exception {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.startBattle2(id, mode))
                .msg("开始演习")
                .build();
    }

    @ApiOperation(value = "结束演习")
    @GetMapping(value = "/end")
    public BaseResponse endBattle() throws Exception {
        battleService.endBattle(false);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("结束演习")
                .build();
    }

    @ApiOperation(value = "查询当前演习")
    @GetMapping(value = "/now")
    public BaseResponse battleNow(@RequestParam(value = "last", required = false) LocalDateTime time) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.getNowBattleDetail(time))
                .build();
    }

    @ApiOperation(value = "查询历史演习")
    @GetMapping(value = "/query")
    public BaseResponse queryBattle(BattleQueryDO battleQueryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.listBattleByQuery(battleQueryDO))
                .build();
    }

    @ApiOperation(value = "查询指定演习记录")
    @GetMapping(value = "/record/{id}/{index}")
    public BaseResponse getBattleRecord(@PathVariable("id") Long battleId, @PathVariable("index") int index) throws IOException {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(battleService.getBattleRecord(battleId, index))
                .build();
    }

    @ApiOperation(value = "删除历史演习")
    @DeleteMapping(value = "/{id}")
    public BaseResponse deleteBattle(@PathVariable("id") Long id) {
        battleService.deleteBattle(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "删除所有历史演习")
    @DeleteMapping(value = "/battles")
    public BaseResponse deleteAllBattleLog() {
        battleService.deleteAllBattleLog();
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "演习结果导出")
    @PostMapping(value = "/export/excel/{id}")
    public void getBattleRecord(@PathVariable("id") Long id, @RequestBody BattleStatisticsDO battleStatisticsDO, HttpServletResponse response) throws UnsupportedEncodingException {
        battleService.exportExcel(id, battleStatisticsDO, response);
    }
}
