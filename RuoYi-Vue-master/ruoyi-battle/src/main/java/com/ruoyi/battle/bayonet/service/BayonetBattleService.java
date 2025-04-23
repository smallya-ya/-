package com.ruoyi.battle.bayonet.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import  com.ruoyi.battle.bayonet.BayonetModeEnum;
import  com.ruoyi.battle.bayonet.domain.*;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleAchievementMapper;
import  com.ruoyi.battle.bayonet.mapper.BayonetBattleMapper;
import  com.ruoyi.battle.bayonet.mapper.BayonetCompetitionGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongjiasen
 */
@Service
public class BayonetBattleService {

    @Autowired
    private BayonetBattleMapper bayonetBattleMapper;
    @Autowired
    private BayonetCompetitionGroupMapper bayonetCompetitionGroupMapper;
    @Autowired
    private BayonetBattleAchievementMapper bayonetBattleAchievementMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(BayonetBattleEntity entity) {
        bayonetBattleMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(BayonetBattleEntity entity) {
        bayonetBattleMapper.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bayonetBattleMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByConfigId(Long id) {
        LambdaQueryWrapper<BayonetBattleEntity> query = Wrappers.lambdaQuery();
        query.eq(BayonetBattleEntity::getConfigId, id);
        bayonetBattleMapper.delete(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        bayonetBattleMapper.deleteBatchIds(ids);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetBattleEntity> listByQuery(BayonetBattleQueryDO queryDO) {
        LambdaQueryWrapper<BayonetBattleEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(BayonetBattleEntity::getCreateTime);
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetBattleMapper.selectList(query));
    }

    @Transactional(readOnly = true)
    public BayonetBattleEntity getById(long id) {
        return bayonetBattleMapper.selectById(id);
    }

    @Transactional(readOnly = true)
    public PageInfo<BayonetCompetitionGroupRecordVO> listBattleByType(BayonetBattleQueryDO queryDO) {
        PageHelper.startPage(queryDO.getPage(), queryDO.getPageSize());
        return new PageInfo<>(bayonetCompetitionGroupMapper.getByQuery(queryDO.getType()));
    }

    public List<BayonetCompetitionGroupRecordVO> getBattleResult() {
        LambdaQueryWrapper<BayonetBattleEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(BayonetBattleEntity::getEndTime).last(" limit 1");
        BayonetBattleEntity last = bayonetBattleMapper.selectOne(wrapper);
        return bayonetCompetitionGroupMapper.getBattleResultByBattleId(last.getId());
    }

    @Transactional(readOnly = true)
    public List<BayonetCompetitionGroupRecordDetailVO> getDetailByRecordId(Long recordId) {
        return bayonetCompetitionGroupMapper.getDetailByRecordId(recordId);
    }

    @Transactional(readOnly = true)
    public List<BayonetVestLogVO> getGroupRecordDetailByRecordId(Long recordId) {
        return bayonetBattleAchievementMapper.queryRecordDetailByRecordId(recordId)
                .stream()
                .map(detail -> new BayonetVestLogVO(detail.getId(), String.format("%d秒%s刺中%s%s力量%d公斤"
                        , detail.getHappenTime(), detail.getAttacker(), detail.getOpponent(), detail.getHitArea(), detail.getHitStrength())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    List<Long> queryByConfigId(Long configId) {
        return bayonetBattleMapper.queryByConfigId(configId);
    }

    public void exportGroupRecord(BayonetBattleQueryDO queryDO, HttpServletResponse response) throws IOException {
        List<BayonetCompetitionGroupRecordVO> list = bayonetCompetitionGroupMapper.getByQuery(queryDO.getType());

        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("happenTime", "对战日期");
        writer.addHeaderAlias("battle", "演习名称");
        writer.addHeaderAlias("m1", "红方姓名");
        writer.addHeaderAlias("m2", "蓝方姓名");
        writer.addHeaderAlias("winner", "获胜者");
        writer.addHeaderAlias("useTime", "对战时长(秒)");
        writer.setOnlyAlias(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String fileName = BayonetModeEnum.getModeName(queryDO.getType()) + "历史记录";
        response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1) + ".xlsx");
        writer.flush(response.getOutputStream());
        writer.close();
        IoUtil.close(response.getOutputStream());
    }

    public void exportGroupRecordDetailByRecordId(Long recordId, HttpServletResponse response) throws IOException {
        List<BayonetCompetitionGroupRecordDetailVO> list = bayonetCompetitionGroupMapper.getDetailByRecordId(recordId);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("name", "人员");
        writer.addHeaderAlias("head", "头部");
        writer.addHeaderAlias("leftChest", "左胸");
        writer.addHeaderAlias("rightChest", "右胸");
        writer.addHeaderAlias("leftArm", "左臂");
        writer.addHeaderAlias("rightArm", "右臂");
        writer.addHeaderAlias("leftAbdomen", "左腹");
        writer.addHeaderAlias("rightAbdomen", "右腹");
        writer.addHeaderAlias("leftRib", "左肋");
        writer.addHeaderAlias("rightRib", "右肋");
        writer.addHeaderAlias("totalHitTimes", "总次数");
        writer.addHeaderAlias("totalHitScore", "总分数");
        writer.addHeaderAlias("totalHitStrength", "总力量");
        writer.setOnlyAlias(true);
        writer.write(list, true);

        List<BayonetGroupRecordDetailVO> details = bayonetBattleAchievementMapper.queryRecordDetailByRecordId(recordId);
        writer.passCurrentRow();
        writer.merge(10, "对战过程");
        for (BayonetGroupRecordDetailVO detail : details) {
            String msg = String.format("%d秒%s刺中%s%s力量%d公斤"
                    , detail.getHappenTime(), detail.getAttacker(), detail.getOpponent(), detail.getHitArea(), detail.getHitStrength());
            writer.merge(10, msg, false);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String fileName = BayonetModeEnum.getModeName(bayonetBattleAchievementMapper.getTypeByRecordId(recordId)) + "详细历史记录";
        response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1) + ".xlsx");
        writer.flush(response.getOutputStream());
        writer.close();
        IoUtil.close(response.getOutputStream());
    }
}
