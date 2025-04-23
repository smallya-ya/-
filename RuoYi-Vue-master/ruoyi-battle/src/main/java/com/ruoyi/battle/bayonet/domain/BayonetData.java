package com.ruoyi.battle.bayonet.domain;

import java.util.Collection;

/**
 * @author hongjiasen
 */
public class BayonetData {

    private Collection<BayonetVestModel> vestList;

    private int num;

    private String name;

    public Collection<BayonetVestModel> getVestList() {
        return vestList;
    }

    public void setVestList(Collection<BayonetVestModel> vestList) {
        this.vestList = vestList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
