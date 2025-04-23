package com.ruoyi.battle.bayonet.controller;

import  com.ruoyi.battle.bayonet.domain.BayonetHitAreaConfigEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetHitAreaConfigQueryDO;
import  com.ruoyi.battle.bayonet.service.BayonetHitAreaConfigService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hongj
 */
@Api(value = "刺杀系统击中部位配置controller", tags = {"刺杀系统击中部位配置操作接口"})
@RestController
@RequestMapping("/bayonet/hitAreaConfig")
public class BayonetHitAreaConfigController {

    @Autowired
    private BayonetHitAreaConfigService bayonetHitAreaConfigService;

    @ApiOperation(value = "保存击中部位配置信息")
    @PostMapping
    public BaseResponse save(@RequestBody BayonetHitAreaConfigEntity entity) {
        bayonetHitAreaConfigService.save(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新击中部位配置信息")
    @PutMapping
    public BaseResponse update(@RequestBody BayonetHitAreaConfigEntity entity) {
        bayonetHitAreaConfigService.update(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("更新成功")
                .build();
    }

    @ApiOperation(value = "根据id删除击中部位配置信息")
    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        bayonetHitAreaConfigService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "查询击中部位配置列表")
    @GetMapping
    public BaseResponse query(BayonetHitAreaConfigQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetHitAreaConfigService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "获取击中部位配置信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(bayonetHitAreaConfigService.getById(id))
                .build();
    }
}
