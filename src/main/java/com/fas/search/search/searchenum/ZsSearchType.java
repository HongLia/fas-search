package com.fas.search.search.searchenum;

/**
 * 搜索操作类型
 */
public enum ZsSearchType {
    //普通搜索
    GENERAL_SESRCH("1"),
    //档案搜索
    ARCHIVES_SEARCH("2"),
    //信息栏搜索
    INFOBAR_SEARCH("3");

    private String type;

    ZsSearchType(String type) {
        this.type = type;
    }

    public String getCode(){
        return this.type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
