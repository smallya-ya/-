package com.ruoyi.battle.download.controller;

import  com.ruoyi.battle.download.service.DownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Api(value = "下载controller", tags = {"下载各类模板的接口"})
@Controller
@RequestMapping("download")
public class DownloadController {

    private DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @ApiOperation(value = "下载实兵演习导入模板")
    @GetMapping("shootingTemplate")
    public void downloadShootingTemplate(HttpServletResponse response) throws Exception {
        downloadService.downloadXls("static/shooting-import-template.xls", "实兵演习导入模板", response);
    }
}
