package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.Page;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.service.ZsUserRecordService;
import com.fas.search.manage.vo.PageDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜搜用户使用记录Controller
 * @date 2019/8/13
 */
@RestController
@RequestMapping("census")
public class ZsUserRecordController {

    @Autowired
    private ZsUserRecordService zsUserRecordService;


    /**
     * 获取智能搜索使用情况
     * @return
     */
    @RequestMapping(value = "get-census" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "获取智能搜索使用情况")
    public String getCenusus(){
        Map<String, Object> userCensus = zsUserRecordService.getUserCensus();
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",userCensus);
    }

    /**
     * 用户搜索使用记录展示
     * @param username
     * @param page
     * @return
     */
    @RequestMapping(value = "use",method = RequestMethod.GET)
    @SystemControllerLog(description = "用户搜索使用记录展示")
    public String listUse(String username, Page page){
        List<Map<String, Object>> use = zsUserRecordService.listUse(username, page);
        Integer countUse = zsUserRecordService.countUse(username);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",new PageDataVO(countUse,use));
    }


    /**
     * 用户列表数据查询接口
     * @param username
     * @param page
     * @return
     */
    @RequestMapping(value = "user",method = RequestMethod.GET)
    @SystemControllerLog(description = "用户列表数据查询接口")
    public String listUser(String username,Page page){
        List<Map<String, Object>> user = zsUserRecordService.listUser(username, page);
        Integer countUser = zsUserRecordService.countUser(username);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",new PageDataVO(countUser,user));
    }


}
