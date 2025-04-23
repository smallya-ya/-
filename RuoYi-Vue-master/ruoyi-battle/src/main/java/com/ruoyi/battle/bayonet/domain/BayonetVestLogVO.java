package com.ruoyi.battle.bayonet.domain;

/**
 * @author hongjiasen
 */
public class BayonetVestLogVO {

    private Long id;

    private String msg;

    public BayonetVestLogVO() {
    }

    public BayonetVestLogVO(Long id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
