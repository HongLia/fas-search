package com.fas.search.manage.exception;

/**
 * @auther wuzy
 * @description 智能搜索主体信息项设置的时候，重复设置查询主键异常
 * @date 2019/7/22
 */
public class SearchPkRepeatException extends RuntimeException {

    public SearchPkRepeatException(String msg){
        super(msg);
    }
}
