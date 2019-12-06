package com.fas.search.search.exception;

/**
 * @auther wuzy
 * @description 使用文件搜索，但没有上传文件异常
 * @date 2019/11/27
 */
public class SearchNoFileException extends RuntimeException {

    public SearchNoFileException(String message) {
        super(message);
    }
}
