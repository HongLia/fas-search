package com.fas.search.search.searchenum;

import org.apache.commons.lang.StringUtils;

/**
 * 智能搜索搜索类型枚举类
 */
public enum SearchClass {

    IK("1","IK"),STR("2","STR");

    //对应code值
    private String code;
    //对应value值
    private String value;

    SearchClass(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SearchClass getSearchClass(String code){
        if (StringUtils.isEmpty(code)){
            return IK;
        }
        for (SearchClass sc: SearchClass.values()) {
            if (sc.getCode().equals(code)){
                return sc;
            }
        }
        return  IK;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
