package com.ruoyi.battle.map.controller;

import com.ruoyi.battle.map.domain.IndoorMapDO;
import com.ruoyi.battle.map.domain.MapEntity;
import com.ruoyi.battle.map.domain.MapQueryDO;
import  com.ruoyi.battle.map.service.MapService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongjiasen
 */
@Api(value = "地图controller", tags = {"实兵演习地图操作接口"})
@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @ApiOperation(value = "新增室内地图")
    @PostMapping("/indoor")
    public BaseResponse addMap(@RequestParam("file") MultipartFile file, IndoorMapDO indoorMapDO) throws IOException {
        mapService.addIndoorMap(file, indoorMapDO);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "新增室外地图")
    @PostMapping("")
    public BaseResponse addMap(@RequestBody MapEntity mapEntity) {
        mapService.addMap(mapEntity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .msg("地图创建成功")
                .build();
    }

    @ApiOperation(value = "删除地图")
    @DeleteMapping("/{id}")
    public BaseResponse deleteMap(@PathVariable("id") long id) throws IOException {
        mapService.deleteMap(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "获取地图信息")
    @GetMapping("/{id}")
    public BaseResponse getMap(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(mapService.getMap(id))
                .build();
    }

    @ApiOperation(value = "查询地图列表")
    @GetMapping("/query")
    public BaseResponse query(MapQueryDO mapQueryDO){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(mapService.listMapByQuery(mapQueryDO))
                .build();
    }

}
