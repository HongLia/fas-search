package com.fas.search.search.exception;

import com.fas.search.util.view.ReturnDataUtil;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther wuzy
 * @description 搜索controller全局异常处理
 * @date 2019/12/3
 */
@ControllerAdvice(basePackages = "com.fas.search.search.controller")
public class SearchControllerAdvice {
    protected final Logger log = Logger.getLogger(getClass());
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleException(Exception e){
        String msg = e.getMessage();
        log.error(msg);
        System.out.println(msg);
        return ReturnDataUtil.getFailData(msg);
    }


}
