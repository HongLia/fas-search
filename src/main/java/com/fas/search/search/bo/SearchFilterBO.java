package com.fas.search.search.bo;

/**
 * @auther wuzy
 * @description 搜索数据封装的过滤BO对象
 * @date 2019/9/6
 */
public class SearchFilterBO {

    //筛选字段
    private String[] extFields;
    //筛选值
    private String[] values;
    //筛选规则  = ,  !=  , > , < 等等
    private String[] condition;
    //各个条件的连接符
    private String  extLink;

    public SearchFilterBO() {
    }

    public String[] getExtFields() {
        return extFields;
    }

    public void setExtFields(String[] extFields) {
        this.extFields = extFields;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getCondition() {
        return condition;
    }

    public void setCondition(String[] condition) {
        this.condition = condition;
    }

    public String getExtLink() {
        return extLink;
    }

    public void setExtLink(String extLink) {
        this.extLink = extLink;
    }
}
