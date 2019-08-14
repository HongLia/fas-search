package com.fas.search.manage.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages = "com.fas.search.manage.controller")
public class ManageControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleException(Exception e){
        String msg = "";
        if (true){
            System.out.println("这里是运行时异常");
            e.printStackTrace();
        }
        return "这里是全局异常处理";
    }


}
