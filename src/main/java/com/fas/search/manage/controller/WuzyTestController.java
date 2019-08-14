package com.fas.search.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class WuzyTestController {

    @RequestMapping("test")
    @ResponseBody
    public String test(){
        throw new RuntimeException("ffffff");
    }
}
