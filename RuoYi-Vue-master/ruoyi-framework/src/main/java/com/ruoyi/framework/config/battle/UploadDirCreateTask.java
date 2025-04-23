package com.ruoyi.framework.config.battle;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author hongjiasen
 */
@Component
public class UploadDirCreateTask implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String dir = SpringUtil.getProperty("spring.servlet.multipart.location");
        if (StrUtil.isNotBlank(dir)) {
            FileUtil.mkdir(dir);
        }
    }
}
