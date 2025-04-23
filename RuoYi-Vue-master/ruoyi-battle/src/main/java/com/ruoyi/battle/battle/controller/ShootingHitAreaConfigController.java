package com.ruoyi.battle.battle.controller;

import com.ruoyi.battle.battle.domain.ShootingHitAreaConfigEntity;
import com.ruoyi.battle.battle.domain.ShootingHitAreaConfigQueryDO;
import  com.ruoyi.battle.battle.service.ShootingHitAreaConfigService;
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
@Api(value = "实兵对抗系统击中部位配置controller", tags = {"实兵对抗系统击中部位配置操作接口"})
@RestController
@RequestMapping("/shooting/hitAreaConfig")
public class ShootingHitAreaConfigController {

    @Autowired
    private ShootingHitAreaConfigService shootingHitAreaConfigService;

    @ApiOperation(value = "保存击中部位配置信息")
    @PostMapping
    public BaseResponse save(@RequestBody ShootingHitAreaConfigEntity entity) {
        shootingHitAreaConfigService.save(entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("保存成功")
                .build();
    }

    @ApiOperation(value = "更新击中部位配置信息")
    @PutMapping
    public BaseResponse update(@RequestBody ShootingHitAreaConfigEntity entity) {
        shootingHitAreaConfigService.update(entity);
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
        shootingHitAreaConfigService.delete(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("删除成功")
                .build();
    }

    @ApiOperation(value = "查询击中部位配置列表")
    @GetMapping
    public BaseResponse query(ShootingHitAreaConfigQueryDO queryDO) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(shootingHitAreaConfigService.listByQuery(queryDO))
                .build();
    }

    @ApiOperation(value = "获取击中部位配置信息")
    @GetMapping("/{id}")
    public BaseResponse getById(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(shootingHitAreaConfigService.getById(id))
                .build();
    }
}
