package com.fas.search.search.vo;

import com.fas.base.model.Page;

import java.util.Map;
import java.util.List;

/**
 * @auther wuzy
 * @description 橄榄数据vo对象
 * @date 2019/11/11
 */
public class OverviewDataVo {

    private List<Map<String,Object>> datas;

    private Paging paging;

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        this.datas = datas;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public OverviewDataVo(List<Map<String, Object>> datas, Paging paging) {
        this.datas = datas;
        this.paging = paging;
    }

    public OverviewDataVo() {
    }

    public OverviewDataVo(List<Map<String, Object>> datas, Long total, Page page) {
        this.datas = datas;
        Paging paging = new Paging();
        paging.setStart(page.getStart());
        paging.setLength(page.getLength());
        paging.setTotal_records(total);
        this.paging = paging;
    }
}

class Paging{
    private Integer length;
    private Integer start;
    private Long total_records;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Long getTotal_records() {
        return total_records;
    }

    public void setTotal_records(Long total_records) {
        this.total_records = total_records;
    }
}
