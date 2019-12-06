package com.fas.search.search.bo;

import java.util.Map;
import java.util.List;

/**
 * @auther wuzy
 * @description 智能搜索查询返回结果 BO
 * @date 2019/9/9
 */
public class SearchResultBO {
    //查询数据量
    private Long total = 0L;
    //查询数据
    private List<Map<String,Object>> data;

    private String msg ;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Map<String,Object>> getData() {
        return data;
    }

    public void setData(List<Map<String,Object>> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SearchResultBO() {
    }

    public SearchResultBO(Long total, List<Map<String,Object>> data,String msg) {
        this.total = total;
        this.data = data;
        this.msg = msg;
    }


}
