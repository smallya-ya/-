package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetPersonnelQueryDO;
import  com.ruoyi.battle.bayonet.service.BayonetPersonnelService;
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
@Api(value = "刺杀系统人员controller", tags = {"刺杀系统人员操作接口"})
@RestController
@RequestMapping("/bayonet/personnel")
public class BayonetPersonnelController {

    @Autowired
    private BayonetPersonnelService bayonetPersonnelService;

    @ApiOperation(value = "保存人员信息")
    @PostMapping
    public BaseResponse save(@RequestBody BayonetPersonnelEntity entity) {
        bayonetPersonnelService.save(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新人员信息")
    @PutMapping
    public BaseResponse update(@RequestBody BayonetPersonnelEntity entity) {
        bayonetPersonnelService.update(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("更新成功")
                .build();
    }

    @ApiOperation(value = "根据id删除人员信息")
    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        bayonetPersonnelService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "查询人员列表")
    @GetMapping
    public BaseResponse query(BayonetPersonnelQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetPersonnelService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "获取人员信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetPersonnelService.getById(id))
                .build();
    }
}
