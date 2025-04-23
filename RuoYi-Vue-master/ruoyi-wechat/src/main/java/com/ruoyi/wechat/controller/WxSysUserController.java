package com.ruoyi.wechat.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wechat.domain.WxSysUser;
import com.ruoyi.wechat.domain.WxLoginRequest;
import com.ruoyi.wechat.domain.WxLoginResponse;
import com.ruoyi.wechat.service.IWxSysUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信用户信息Controller
 * 
 * @author 司小雅
 * @date 2025-03-03
 */
@RestController
@RequestMapping("/wechat/wxuser")
public class WxSysUserController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(WxSysUserController.class);

    @Autowired
    private IWxSysUserService wxSysUserService;

    /**
     * 查询微信用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:list')")
    @GetMapping("/list")
    public TableDataInfo list(WxSysUser wxSysUser)
    {
        startPage();
        List<WxSysUser> list = wxSysUserService.selectWxSysUserList(wxSysUser);
        return getDataTable(list);
    }

    /**
     * 导出微信用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:export')")
    @Log(title = "微信用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxSysUser wxSysUser)
    {
        List<WxSysUser> list = wxSysUserService.selectWxSysUserList(wxSysUser);
        ExcelUtil<WxSysUser> util = new ExcelUtil<WxSysUser>(WxSysUser.class);
        util.exportExcel(response, list, "微信用户信息数据");
    }

    /**
     * 获取微信用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(wxSysUserService.selectWxSysUserByUserId(userId));
    }

    /**
     * 新增微信用户信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:add')")
    @Log(title = "微信用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WxSysUser wxSysUser)
    {
        return toAjax(wxSysUserService.insertWxSysUser(wxSysUser));
    }

    /**
     * 修改微信用户信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:edit')")
    @Log(title = "微信用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WxSysUser wxSysUser)
    {
        return toAjax(wxSysUserService.updateWxSysUser(wxSysUser));
    }

    /**
     * 删除微信用户信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:wxuser:remove')")
    @Log(title = "微信用户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(wxSysUserService.deleteWxSysUserByUserIds(userIds));
    }

    /**
     * 微信登录
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody WxLoginRequest loginRequest) {
        logger.info("Received login request: {}", loginRequest); // 添加日志记录
        WxLoginResponse loginResponse = wxSysUserService.wxLogin(loginRequest);
        return success(loginResponse);
    }
}
