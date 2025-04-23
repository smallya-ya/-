package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetScorePlanQueryDO;
import  com.ruoyi.battle.bayonet.service.BayonetHitScoreService;
import  com.ruoyi.battle.bayonet.service.BayonetScorePlanService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hongjiasen
 */
@Api(value = "刺杀系统击中部位分值controller", tags = {"刺杀系统得分方案操作接口"})
@RestController
@RequestMapping("/bayonet/scorePlan")
public class BayonetScorePlanController {

    @Autowired
    private BayonetScorePlanService bayonetScorePlanService;
    @Autowired
    private BayonetHitScoreService bayonetHitScoreService;

    @ApiOperation(value = "保存得分方案信息")
    @PostMapping
    public BaseResponse save(@RequestBody BayonetScorePlanEntity entity) {
        bayonetScorePlanService.save(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新得分方案信息")
    @PutMapping
    public BaseResponse update(@RequestBody BayonetScorePlanEntity entity) {
        bayonetScorePlanService.update(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("更新成功")
                .build();
    }

    @ApiOperation(value = "根据得分项id删除得分项")
    @DeleteMapping("plan/{scoreId}")
    public BaseResponse deleteItem(@ApiParam(name = "scoreId", value = "得分项id") @PathVariable("scoreId") Long scoreId) {
        int success = bayonetScorePlanService.deleteItem(scoreId);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(success > 0 ? Constant.SUCCESS_STATUS : Constant.FAIL_STATUS)
                .msg(success > 0 ? "删除成功" : "删除失败")
                .build();
    }

    @ApiOperation(value = "根据id删除得分方案信息")
    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        bayonetScorePlanService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "查询得分方案列表")
    @GetMapping
    public BaseResponse query(BayonetScorePlanQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetScorePlanService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "根据得分方案ID查询得分列表")
    @GetMapping("/hitScores/{id}")
    public BaseResponse queryByPlanId(@PathVariable("id") Long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetHitScoreService.queryVoByPlanId(id))
                .build();
    }

    @ApiOperation(value = "获取得分方案信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetScorePlanService.getById(id))
                .build();
    }
}
