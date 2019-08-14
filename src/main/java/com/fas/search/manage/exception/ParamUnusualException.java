package com.fas.search.manage.exception;

/**
 * @auther wuzy
 * @description 传递参数不合法异常
 * @date 2019/8/9
 */
public class ParamUnusualException extends RuntimeException {

    public ParamUnusualException(String msg){
        super(msg);
    }
}
