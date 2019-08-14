package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsSubjectDics;
import com.fas.search.manage.service.ZsSubjectDicService;
import com.fas.search.manage.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther wuzy
 * @description 智能搜索信息项Controller
 * @date 2019/7/22
 */
@RestController
@RequestMapping("dic")
public class ZsSubjectDisController {

    @Autowired
    private ZsSubjectDicService zsSubjectDicService;

    /**
     * 新增智能搜索主题信息项
     * @param dic
     * @return
     */
    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @SystemControllerLog(description = "新增智能搜索主体信息项")
    public String save(ZsSubjectDics dic){
        //调用保存业务方法
        Integer result = zsSubjectDicService.save(dic);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除智能搜索主体信息项
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除智能搜索主体信息项")
    public String delete(@PathVariable("id") String id){
        //调用删除业务方法
        Integer result = zsSubjectDicService.remove(id);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 修改智能搜索信息项
     * @param dic
     * @return
     */
    @RequestMapping(value = "/" , method = RequestMethod.PUT )
    @SystemControllerLog(description = "修改智能搜索信息项")
    public String update(ZsSubjectDics dic){
        //调用修改业务方法
        Integer result = zsSubjectDicService.updateByPrimaryKeySelective(dic);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 查询智能搜索主体下的信息项
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "getAllDics/{subject_id}" , method = RequestMethod.GET)
    @SystemControllerLog(description = "查询智能搜索主体下的信息项")
    public String getAllDics(@PathVariable("subject_id") String subject_id){
        //调用业务查询方法
        List<ZsSubjectDics> zsSubjectDics = zsSubjectDicService.listBySubjectId(subject_id);
        //返回结果
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功", zsSubjectDics);
    }

}
