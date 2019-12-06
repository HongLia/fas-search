package com.fas.search.search.exception;

/**
 * @auther wuzy
 * @description 连接资源操作异常
 * @date 2019/9/10
 */
public class ConnectionResourceException extends RuntimeException {

    public ConnectionResourceException(String msg) {
        super(msg);
    }

    public ConnectionResourceException(Throwable cause) {
        super(cause);
    }

    public ConnectionResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
