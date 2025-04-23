package com.ruoyi.battle.bayonet.domain;

/**
 * @author hongjiasen
 */
public class BayonetResultVO {

    private BayonetPersonnelResultVO member1;

    private BayonetPersonnelResultVO member2;

    public BayonetResultVO() {
    }

    public BayonetResultVO(BayonetPersonnelResultVO member1, BayonetPersonnelResultVO member2) {
        this.member1 = member1;
        this.member2 = member2;
    }

    public BayonetPersonnelResultVO getMember1() {
        return member1;
    }

    public void setMember1(BayonetPersonnelResultVO member1) {
        this.member1 = member1;
    }

    public BayonetPersonnelResultVO getMember2() {
        return member2;
    }

    public void setMember2(BayonetPersonnelResultVO member2) {
        this.member2 = member2;
    }
}
