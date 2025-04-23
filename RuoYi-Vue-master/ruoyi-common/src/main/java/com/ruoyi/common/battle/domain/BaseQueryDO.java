package com.ruoyi.common.battle.domain;

/**
 * @author hongjiasen
 */
public class BaseQueryDO {

    protected Integer page = 1;
    protected Integer pageSize = 100;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
