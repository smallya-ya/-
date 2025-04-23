package com.ruoyi.battle.bayonet.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.battle.domain.CommonEntity;

/**
 * @author hongjiasen
 */
@TableName("t_bayonet_battle_group_record")
public class BayonetBattleGroupRecordEntity extends CommonEntity {

    private Long battleId;

    @TableField(value = "memberId1")
    private Long memberId1;

    @TableField(value = "memberId2")
    private Long memberId2;

    private Long winner;

    public Long getBattleId() {
        return battleId;
    }

    public void setBattleId(Long battleId) {
        this.battleId = battleId;
    }

    public Long getMemberId1() {
        return memberId1;
    }

    public void setMemberId1(Long memberId1) {
        this.memberId1 = memberId1;
    }

    public Long getMemberId2() {
        return memberId2;
    }

    public void setMemberId2(Long memberId2) {
        this.memberId2 = memberId2;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }
}
