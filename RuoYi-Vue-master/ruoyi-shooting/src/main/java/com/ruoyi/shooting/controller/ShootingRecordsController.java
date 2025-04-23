package com.ruoyi.shooting.controller;

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
import com.ruoyi.shooting.domain.ShootingRecords;
import com.ruoyi.shooting.service.IShootingRecordsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 射击打靶Controller
 *
 * @author 司小雅
 * @date 2025-03-07
 */
@RestController
@RequestMapping("/shooting/shooting")
public class ShootingRecordsController extends BaseController
{
    @Autowired
    private IShootingRecordsService shootingRecordsService;

    /**
     * 查询射击打靶列表
     */
//    @PreAuthorize("@ss.hasPermi('shooting:shooting:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShootingRecords shootingRecords)
    {
        startPage();
        List<ShootingRecords> list = shootingRecordsService.selectShootingRecordsList(shootingRecords);
        return getDataTable(list);
    }

    /**
     * 导出射击打靶列表
     */
    @PreAuthorize("@ss.hasPermi('shooting:shooting:export')")
    @Log(title = "射击打靶", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShootingRecords shootingRecords)
    {
        List<ShootingRecords> list = shootingRecordsService.selectShootingRecordsList(shootingRecords);
        ExcelUtil<ShootingRecords> util = new ExcelUtil<ShootingRecords>(ShootingRecords.class);
        util.exportExcel(response, list, "射击打靶数据");
    }

    /**
     * 获取射击打靶详细信息
     */
//    @PreAuthorize("@ss.hasPermi('shooting:shooting:query')")
    @GetMapping(value = "/user/{username}")
    public AjaxResult getInfo(@PathVariable("username") String username)
    {
        return success(shootingRecordsService.selectShootingRecordsListByUsername(username));
    }

    /**
     * 新增射击打靶
     */
//    @PreAuthorize("@ss.hasPermi('shooting:shooting:add')")
    @Log(title = "射击打靶", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShootingRecords shootingRecords)
    {
        return toAjax(shootingRecordsService.insertShootingRecords(shootingRecords));
    }

    /**
     * 修改射击打靶
     */
    @PreAuthorize("@ss.hasPermi('shooting:shooting:edit')")
    @Log(title = "射击打靶", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShootingRecords shootingRecords)
    {
        return toAjax(shootingRecordsService.updateShootingRecords(shootingRecords));
    }

    /**
     * 删除射击打靶
     */
    @PreAuthorize("@ss.hasPermi('shooting:shooting:remove')")
    @Log(title = "射击打靶", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shootingRecordsService.deleteShootingRecordsByIds(ids));
    }
}
