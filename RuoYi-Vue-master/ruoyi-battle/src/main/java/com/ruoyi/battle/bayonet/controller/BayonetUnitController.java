package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetUnitEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetUnitQueryDO;
import  com.ruoyi.battle.bayonet.service.BayonetUnitService;
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
@Api(value = "刺杀系统组织controller", tags = {"刺杀系统组织操作接口"})
@RestController
@RequestMapping("/bayonet/units")
public class BayonetUnitController {

    @Autowired
    private BayonetUnitService bayonetUnitService;

    @ApiOperation(value = "保存组织信息")
    @PostMapping
    public BaseResponse save(@RequestBody BayonetUnitEntity entity) {
        bayonetUnitService.save(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新组织信息")
    @PutMapping
    public BaseResponse update(@RequestBody BayonetUnitEntity entity) {
        bayonetUnitService.update(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("更新成功")
                .build();
    }

    @ApiOperation(value = "根据id删除组织信息")
    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        bayonetUnitService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "查询组织列表")
    @GetMapping
    public BaseResponse query(BayonetUnitQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetUnitService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "获取组织信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetUnitService.getById(id))
                .build();
    }
}
