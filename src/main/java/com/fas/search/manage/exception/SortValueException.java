package com.fas.search.manage.exception;

/**
 * @auther wuzy
 * @description 排序时，排序字段不符合规定
 * @date 2019/8/8
 */
public class SortValueException extends RuntimeException {

    public SortValueException(String msg) {
        super(msg);
    }
}
