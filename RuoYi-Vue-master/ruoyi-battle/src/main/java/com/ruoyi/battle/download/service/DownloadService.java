package com.ruoyi.battle.download.service;

import cn.hutool.core.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class DownloadService {

    private Logger log = LoggerFactory.getLogger(getClass());

    public void downloadXls(String location, String name, HttpServletResponse response) throws Exception {
        this.download(location, name, ".xls", "application/vnd.ms-excel", response);
    }

    public void downloadXlsx(String location, String name, HttpServletResponse response) throws Exception {
        this.download(location, name, ".xlsx"
                , "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", response);
    }

    private void download(String location, String name, String suffix
            , String contentType, HttpServletResponse response) throws UnsupportedEncodingException {
        ClassPathResource cpr = new ClassPathResource(location);

        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(name, "utf-8") + suffix);
        try (OutputStream os = response.getOutputStream()) {
            IoUtil.copy(cpr.getInputStream(), os);
        } catch (Exception e) {
            log.error("导出时出现异常", e);
        }
    }
}
