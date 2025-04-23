package com.ruoyi.battle.bayonet.context;

import  com.ruoyi.battle.bayonet.domain.BayonetBattleEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionConfigEntity;
import  com.ruoyi.battle.bayonet.domain.BayonetCompetitionGroupVO;
import  com.ruoyi.battle.bayonet.domain.BayonetVestModel;

import java.util.List;
import java.util.Map;

/**
 * @author hongjiasen
 */
public interface BayonetBattleContext {

    /**
     * 刺杀演习初始化
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 刺杀演习开始
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 刺杀演习结束
     * @throws Exception
     */
    void stop(boolean exist) throws Exception;

    /**
     * 刺杀演习是否结束
     * @return
     */
    boolean isStop();

    /**
     * 返回刺杀演习中的马甲列表信息
     * @return
     */
    Map<Integer, BayonetVestModel> getVestEntityMap();

    /**
     * 返回演习信息
     * @return
     */
    BayonetBattleEntity getBayonetBattle();

    /**
     * 返回对抗组信息
     * @return
     */
    List<BayonetCompetitionGroupVO> getGroup();

    /**
     * 返回方案信息
     * @return 方案信息
     */
    BayonetCompetitionConfigEntity getConfig();
}
