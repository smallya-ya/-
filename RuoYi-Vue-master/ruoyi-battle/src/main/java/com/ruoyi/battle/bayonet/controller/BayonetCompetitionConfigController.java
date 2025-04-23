package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigBO;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigQueryDO;
import  com.ruoyi.battle.bayonet.service.BayonetCompetitionConfigService;
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
@Api(value = "刺杀系统比赛方案controller", tags = {"刺杀系统比赛方案操作接口"})
@RestController
@RequestMapping("/bayonet/competitionConfig")
public class BayonetCompetitionConfigController {

    @Autowired
    private BayonetCompetitionConfigService bayonetCompetitionConfigService;

    @ApiOperation(value = "保存比赛方案信息")
    @PostMapping
    public BaseResponse save(@RequestBody BayonetCompetitionConfigBO bo) {
        bayonetCompetitionConfigService.save(bo);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新比赛方案信息")
    @PutMapping
    public BaseResponse update(@RequestBody BayonetCompetitionConfigBO bo) {
        bayonetCompetitionConfigService.update(bo);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("更新成功")
                .build();
    }

    @ApiOperation(value = "根据id删除比赛方案信息")
    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        bayonetCompetitionConfigService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "根据对抗组id删除对抗组")
    @DeleteMapping("/config/{groupId}")
    public BaseResponse deleteByConfigIdAndGroupId(@ApiParam(name = "groupId", value = "对抗组id") @PathVariable("groupId") Long groupId) {
        int success = bayonetCompetitionConfigService.deleteByGroupId(groupId);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(success > 0 ? Constant.SUCCESS_STATUS : Constant.FAIL_STATUS)
                .msg(success > 0 ? "删除成功" : "删除失败")
                .build();
    }

    @ApiOperation(value = "查询比赛方案列表")
    @GetMapping
    public BaseResponse query(BayonetCompetitionConfigQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetCompetitionConfigService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "根据比赛方案ID查找对抗组列表")
    @GetMapping("/groups/{id}")
    public BaseResponse query(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetCompetitionConfigService.listGroupById(id))
                .build();
    }

    @ApiOperation(value = "获取比赛方案信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetCompetitionConfigService.getById(id))
                .build();
    }
}
