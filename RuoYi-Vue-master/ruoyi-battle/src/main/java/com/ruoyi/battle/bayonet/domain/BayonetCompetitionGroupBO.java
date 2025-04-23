package com.ruoyi.battle.bayonet.domain;

/**
 * @author hongjiasen
 */
public class BayonetCompetitionGroupBO {

    private GroupMemberBO member1;

    private GroupMemberBO member2;

    public BayonetCompetitionGroupBO() {
    }

    public BayonetCompetitionGroupBO(GroupMemberBO member1, GroupMemberBO member2) {
        this.member1 = member1;
        this.member2 = member2;
    }

    public GroupMemberBO getMember1() {
        return member1;
    }

    public void setMember1(GroupMemberBO member1) {
        this.member1 = member1;
    }

    public GroupMemberBO getMember2() {
        return member2;
    }

    public void setMember2(GroupMemberBO member2) {
        this.member2 = member2;
    }
}
