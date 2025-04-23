package com.ruoyi.wechat.service;

import java.util.List;
import com.ruoyi.wechat.domain.WxSysUser;
import com.ruoyi.wechat.domain.WxLoginRequest;
import com.ruoyi.wechat.domain.WxLoginResponse;

/**
 * 微信用户信息Service接口
 * 
 * @author 司小雅
 * @date 2025-03-03
 */
public interface IWxSysUserService 
{
    /**
     * 查询微信用户信息
     * 
     * @param userId 微信用户信息主键
     * @return 微信用户信息
     */
    public WxSysUser selectWxSysUserByUserId(Long userId);

    /**
     * 查询微信用户信息列表
     * 
     * @param wxSysUser 微信用户信息
     * @return 微信用户信息集合
     */
    public List<WxSysUser> selectWxSysUserList(WxSysUser wxSysUser);

    /**
     * 新增微信用户信息
     * 
     * @param wxSysUser 微信用户信息
     * @return 结果
     */
    public int insertWxSysUser(WxSysUser wxSysUser);

    /**
     * 修改微信用户信息
     * 
     * @param wxSysUser 微信用户信息
     * @return 结果
     */
    public int updateWxSysUser(WxSysUser wxSysUser);

    /**
     * 删除微信用户信息
     * 
     * @param userId 微信用户信息主键
     * @return 结果
     */
    public int deleteWxSysUserByUserId(Long userId);

    /**
     * 批量删除微信用户信息
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxSysUserByUserIds(Long[] userIds);

    /**
     * 微信登录
     * 
     * @param loginRequest 微信登录请求参数
     * @return 微信登录响应结果
     */
    public WxLoginResponse wxLogin(WxLoginRequest loginRequest);
}