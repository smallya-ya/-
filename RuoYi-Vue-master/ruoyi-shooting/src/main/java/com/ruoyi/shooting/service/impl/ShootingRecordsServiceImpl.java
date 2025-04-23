package com.ruoyi.shooting.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.shooting.mapper.ShootingRecordsMapper;
import com.ruoyi.shooting.domain.ShootingRecords;
import com.ruoyi.shooting.service.IShootingRecordsService;

/**
 * 射击打靶Service业务层处理
 * 
 * @author 司小雅
 * @date 2025-03-07
 */
@Service
public class ShootingRecordsServiceImpl implements IShootingRecordsService 
{
    @Autowired
    private ShootingRecordsMapper shootingRecordsMapper;

    /**
     * 查询射击打靶
     * 
     * @param id 射击打靶主键
     * @return 射击打靶
     */
    @Override
    public ShootingRecords selectShootingRecordsById(Long id)
    {
        return shootingRecordsMapper.selectShootingRecordsById(id);
    }

    /**
     * 查询射击打靶列表
     * 
     * @param shootingRecords 射击打靶
     * @return 射击打靶
     */
    @Override
    public List<ShootingRecords> selectShootingRecordsList(ShootingRecords shootingRecords)
    {
        return shootingRecordsMapper.selectShootingRecordsList(shootingRecords);
    }

    /**
     * 查询射击打靶通过用户名
     * 
     * @param username 用户名
     * @return 射击打靶集合
     */
    @Override
    public List<ShootingRecords> selectShootingRecordsListByUsername(String username)
    {
        return shootingRecordsMapper.selectShootingRecordsListByUsername(username);
    }

    /**
     * 新增射击打靶
     * 
     * @param shootingRecords 射击打靶
     * @return 结果
     */
    @Override
    public int insertShootingRecords(ShootingRecords shootingRecords)
    {
        return shootingRecordsMapper.insertShootingRecords(shootingRecords);
    }

    /**
     * 修改射击打靶
     * 
     * @param shootingRecords 射击打靶
     * @return 结果
     */
    @Override
    public int updateShootingRecords(ShootingRecords shootingRecords)
    {
        return shootingRecordsMapper.updateShootingRecords(shootingRecords);
    }

    /**
     * 批量删除射击打靶
     * 
     * @param ids 需要删除的射击打靶主键
     * @return 结果
     */
    @Override
    public int deleteShootingRecordsByIds(Long[] ids)
    {
        return shootingRecordsMapper.deleteShootingRecordsByIds(ids);
    }

    /**
     * 删除射击打靶信息
     * 
     * @param id 射击打靶主键
     * @return 结果
     */
    @Override
    public int deleteShootingRecordsById(Long id)
    {
        return shootingRecordsMapper.deleteShootingRecordsById(id);
    }
}
