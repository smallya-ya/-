package com.ruoyi.battle.bayonet.controller;

import cn.hutool.json.JSONObject;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.service.BayonetBattleService;
import  com.ruoyi.battle.bayonet.service.BayonetService;
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
import java.io.IOException;

/**
 * @author hongjiasen
 */
@Api(value = "刺杀系统controller", tags = {"刺杀系统操作接口"})
@RestController
@RequestMapping("/bayonet")
public class BayonetBattleController {

    @Autowired
    private BayonetService bayonetService;
    @Autowired
    private BayonetBattleService bayonetBattleService;

    @ApiOperation(value = "开始刺杀演习")
    @GetMapping("/start/{id}/{mode}")
    public BaseResponse start(@ApiParam(name = "id", value = "演习方案id") @PathVariable("id") Long id
            , @ApiParam(name = "mode", value = "通信模式：串口-1，MQTT-2") @PathVariable("mode") int mode) throws Exception {
        BayonetBattleEntity battleEntity = bayonetService.start(id, mode);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("开始演习")
                .data(battleEntity)
                .build();
    }

    @ApiOperation(value = "结束刺杀演习")
    @GetMapping("/stop")
    public BaseResponse end() throws Exception {
        bayonetService.stop(false);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("结束演习")
                .build();
    }

    @ApiOperation(value = "获取演习是否在进行中")
    @GetMapping("/status")
    public BaseResponse isProgressing() {
        JSONObject json = new JSONObject();
        json.set("progressing", null != BayonetService.context);
        json.set("type", null != BayonetService.context ? BayonetService.context.getConfig().getType() : -1);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(json)
                .msg("获取演习状态成功")
                .build();
    }

//    @ApiOperation(value = "获取演习结果")
//    @GetMapping("/result")
//    public BaseResponse result(Long id) throws Exception {
//        return BaseResponse.BaseResponseBuilder
//                .aBaseResponse()
//                .code(HttpServletResponse.SC_OK)
//                .status(Constant.SUCCESS_STATUS)
//                .msg("获取演习结果成功")
//                .data(bayonetService.result(id))
//                .build();
//    }

    @ApiOperation(value = "获取所有对抗组信息")
    @GetMapping("/groups")
    public BaseResponse getGroups() {
        if (null != BayonetService.context) {
            return BaseResponse.BaseResponseBuilder
                    .aBaseResponse()
                    .code(200)
                    .status(Constant.SUCCESS_STATUS)
                    .msg("获取对抗组信息成功")
                    .data(BayonetService.context.getGroup())
                    .build();
        } else {
            return BaseResponse.BaseResponseBuilder.aBaseResponse()
                    .code(404)
                    .status(Constant.FAIL_STATUS)
                    .msg("当前没有在进行的刺杀演习")
                    .data(null)
                    .build();
        }
    }

    @ApiOperation(value = "根据刺杀演习类型获取演习记录")
    @GetMapping("/logs")
    public BaseResponse<BayonetCompetitionGroupRecordVO> listBattleByType(BayonetBattleQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(bayonetBattleService.listBattleByType(queryDO))
                .build();
    }

    @ApiOperation(value = "根据演习组id获取演习信息")
    @GetMapping("/logs/info/{recordId}")
    public BaseResponse<BayonetCompetitionGroupRecordDetailVO> getDetailByRecordId(@PathVariable("recordId") Long recordId) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(bayonetBattleService.getDetailByRecordId(recordId))
                .build();
    }

    @ApiOperation(value = "根据演习组id获取演习日志")
    @GetMapping("/logs/record/{recordId}")
    public BaseResponse<BayonetVestLogVO> getGroupRecordDetailByRecordId(@PathVariable("recordId") Long recordId) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(bayonetBattleService.getGroupRecordDetailByRecordId(recordId))
                .build();
    }

    @ApiOperation(value = "获取最新的演习的结果")
    @GetMapping("/result/last")
    public BaseResponse<BayonetCompetitionGroupRecordVO> getBattleResult() {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("")
                .data(bayonetBattleService.getBattleResult())
                .build();
    }

    @ApiOperation(value = "根据刺杀演习类型导出演习历史记录")
    @GetMapping("/logs/info/export")
    public void exportGroupRecord(BayonetBattleQueryDO queryDO, HttpServletResponse response) throws IOException {
        bayonetBattleService.exportGroupRecord(queryDO, response);
    }

    @ApiOperation(value = "根据演习记录id导出演习详细历史记录")
    @GetMapping("/logs/detail/export/{recordId}")
    public void exportGroupRecordDetailByRecordId(@PathVariable("recordId") Long recordId, HttpServletResponse response) throws IOException {
        bayonetBattleService.exportGroupRecordDetailByRecordId(recordId, response);
    }
}
