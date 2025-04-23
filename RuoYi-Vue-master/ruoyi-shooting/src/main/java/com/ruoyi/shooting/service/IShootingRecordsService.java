package com.ruoyi.shooting.service;

import java.util.List;
import com.ruoyi.shooting.domain.ShootingRecords;

/**
 * 射击打靶Service接口
 * 
 * @author 司小雅
 * @date 2025-03-07
 */
public interface IShootingRecordsService 
{
    /**
     * 查询射击打靶
     * 
     * @param id 射击打靶主键
     * @return 射击打靶
     */
    public ShootingRecords selectShootingRecordsById(Long id);

    /**
     * 查询射击打靶列表
     * 
     * @param shootingRecords 射击打靶
     * @return 射击打靶集合
     */
    public List<ShootingRecords> selectShootingRecordsList(ShootingRecords shootingRecords);

    /**
     * 查询射击打靶通过用户名
     * 
     * @param username 用户名
     * @return 射击打靶集合
     */
    public List<ShootingRecords> selectShootingRecordsListByUsername(String username);

    /**
     * 新增射击打靶
     * 
     * @param shootingRecords 射击打靶
     * @return 结果
     */
    public int insertShootingRecords(ShootingRecords shootingRecords);

    /**
     * 修改射击打靶
     * 
     * @param shootingRecords 射击打靶
     * @return 结果
     */
    public int updateShootingRecords(ShootingRecords shootingRecords);

    /**
     * 批量删除射击打靶
     * 
     * @param ids 需要删除的射击打靶主键集合
     * @return 结果
     */
    public int deleteShootingRecordsByIds(Long[] ids);

    /**
     * 删除射击打靶信息
     * 
     * @param id 射击打靶主键
     * @return 结果
     */
    public int deleteShootingRecordsById(Long id);
}
