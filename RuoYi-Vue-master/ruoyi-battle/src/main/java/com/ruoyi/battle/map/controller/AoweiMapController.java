package com.ruoyi.battle.map.controller;

import com.ruoyi.battle.map.domain.AoweiMapDO;
import com.ruoyi.battle.map.domain.MapQueryDO;
import  com.ruoyi.battle.map.service.AoweiMapService;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author hongjiasen
 */
@Api(value = "奥维地图controller", tags = {"实兵演习奥维地图操作接口"})
@RestController
@RequestMapping("/aoweimap")
public class AoweiMapController {

    @Autowired
    private AoweiMapService aoweiMapService;

    @ApiOperation(value = "新增奥维地图")
    @PostMapping
    public BaseResponse addMap(@RequestParam("file") MultipartFile file, @Valid AoweiMapDO entity) throws IOException {
        aoweiMapService.save(file, entity);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "删除奥维地图")
    @DeleteMapping("/{id}")
    public BaseResponse deleteMap(@PathVariable("id") long id) throws IOException {
        aoweiMapService.deleteMap(id);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .build();
    }

    @ApiOperation(value = "获取奥维地图信息")
    @GetMapping("/{id}")
    public BaseResponse getMap(@PathVariable("id") long id) {
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(aoweiMapService.getMap(id))
                .build();
    }

    @ApiOperation(value = "查询奥维地图列表")
    @GetMapping("/query")
    public BaseResponse query(MapQueryDO mapQueryDO){
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(HttpServletResponse.SC_OK)
                .status(Constant.SUCCESS_STATUS)
                .data(aoweiMapService.listMapByQuery(mapQueryDO))
                .build();
    }
}
