package com.fas.search.manage.vo;

/**
 * 分页查询数据VO对象
 */
public class PageDataVO {
    //数据量
    private Integer count;
    //查询出的数据
    private Object datas;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }


    public PageDataVO() {
    }

    public PageDataVO(Integer count, Object datas) {
        this.count = count;
        this.datas = datas;
    }
}
