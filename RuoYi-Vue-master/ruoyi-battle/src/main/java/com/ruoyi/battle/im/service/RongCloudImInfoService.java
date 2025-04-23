package com.ruoyi.battle.im.service;

import cn.hutool.system.oshi.OshiUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import  com.ruoyi.battle.im.domain.RongCloudImInfoEntity;
import  com.ruoyi.battle.im.domain.RongCloudProp;
import  com.ruoyi.battle.im.mapper.RongCloudImInfoMapper;
import io.rong.RongCloud;
import io.rong.methods.user.User;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author hongjiasen
 */
@Service
public class RongCloudImInfoService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RongCloudProp rongCloudProp;
    @Autowired
    private RongCloudImInfoMapper rongCloudImInfoMapper;

    @Retryable(maxAttempts = 4, backoff = @Backoff(value = 2000))
    public void register(int num) throws Exception {
        LambdaQueryWrapper<RongCloudImInfoEntity> query = Wrappers.lambdaQuery();
        query.eq(RongCloudImInfoEntity::getNum, num);
        if (rongCloudImInfoMapper.selectCount(query) > 0) {
            log.info("当前{}号马甲已经向融云服务器注册过", num);
        } else {
            log.info("开始向融云服务器注册{}号马甲", num);
            String userId = OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID() + "-" + num;
            RongCloud rongCloud = RongCloud.getInstance(rongCloudProp.getAppKey(), rongCloudProp.getAppSecret());
            User user = rongCloud.user;
            UserModel userModel = new UserModel()
                    .setId(userId)
                    .setName(num + "号")
                    .setPortrait("http://touxiang.yeree.com/pics/b2/33126.jpg");
            TokenResult result = user.register(userModel);

            log.info("向融云服务器注册结果{}", result.toString());
            RongCloudImInfoEntity info = new RongCloudImInfoEntity(num, result.getToken(), userId);
            rongCloudImInfoMapper.insert(info);
            log.info("结束向融云服务器注册{}号马甲", num);
        }
    }

    @Async("taskExecutor")
    public void batchRegister(int start, int end) {
        for (int num = start; num <= end; num++) {
            try {
                log.info("向融云服务器注册{}号马甲", num);
                this.register(num);
            } catch (Exception e) {
                log.error("向融云服务器注册{}号马甲出现异常", num, e);
            }
        }
    }
}
