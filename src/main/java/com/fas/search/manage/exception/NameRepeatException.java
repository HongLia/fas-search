package com.fas.search.manage.exception;

/**
 * @auther wuzy
 * @description 新增或修改相关信息，名称已经存在，无法修改成为目标名称 异常
 * @date 2019/7/10
 */
public class NameRepeatException extends RuntimeException {

    public NameRepeatException(String msg) {
        super(msg);
    }
}
