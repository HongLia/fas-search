package com.fas.search.search.bo;

import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.search.searchenum.SearchClass;

import java.util.List;

/**
 * @auther wuzy
 * @description 智能搜索 搜索接口是有搜索数据搜索条件BO
 * @date 2019/9/9
 */
public class SearchConditionBO {

    //搜索关键字
    private String key;
    //搜索实体
    private ZsEntity entity;
    //搜索字段
    private List<ZsEntityField> searchField;
    //返回字段
    private List<ZsEntityField> returnField;
    //排序字段
    private List<ZsEntityField> sortField;
    //搜索类型
    private SearchClass searchClass;
    //是否高亮
    private boolean isHl;
    //搜索过滤条件
    private SearchFilterBO searchFilterBO;

    public String getKey() {
        return key;
    }

    public SearchConditionBO setKey(String key) {
        this.key = key;
        return this;
    }

    public ZsEntity getEntity() {
        return entity;
    }

    public SearchConditionBO setEntity(ZsEntity entity) {
        this.entity = entity;
        return this;
    }

    public List<ZsEntityField> getSearchField() {
        return searchField;
    }

    public SearchConditionBO setSearchField(List<ZsEntityField> searchField) {
        this.searchField = searchField;
        return this;
    }

    public List<ZsEntityField> getReturnField() {
        return returnField;
    }

    public SearchConditionBO setReturnField(List<ZsEntityField> returnField) {
        this.returnField = returnField;
        return this;
    }

    public List<ZsEntityField> getSortField() {
        return sortField;
    }

    public void setSortField(List<ZsEntityField> sortField) {
        this.sortField = sortField;
    }

    public SearchClass getSearchClass() {
        return searchClass;
    }

    public SearchConditionBO setSearchClass(SearchClass searchClass) {
        this.searchClass = searchClass;
        return this;
    }

    public boolean isHl() {
        return isHl;
    }

    public SearchConditionBO setHl(boolean hl) {
        isHl = hl;
        return this;
    }

    public SearchConditionBO(String key, ZsEntity entity, List<ZsEntityField> searchField, List<ZsEntityField> returnField, List<ZsEntityField> sortField, SearchClass searchClass, boolean isHl) {
        this.key = key;
        this.entity = entity;
        this.searchField = searchField;
        this.returnField = returnField;
        this.sortField = sortField;
        this.searchClass = searchClass;
        this.isHl = isHl;
    }

    public SearchConditionBO() {
    }

    public SearchFilterBO getSearchFilterBO() {
        return searchFilterBO;
    }

    public void setSearchFilterBO(SearchFilterBO searchFilterBO) {
        this.searchFilterBO = searchFilterBO;
    }
}
