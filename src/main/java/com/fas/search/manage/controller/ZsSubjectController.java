package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.Page;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.manage.util.view.ReturnDataUtil;
import com.fas.search.manage.vo.PageDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther wuzy
 * @description 主体管理相关Controller
 * @date 2019/7/10
 */
@RestController
@RequestMapping("subject")
public class ZsSubjectController {

    @Autowired
    private ZsSubjectService zsSubjectService;

    /**
     * 保存智能搜索主体信息
     * @param subject
     * @return
     */
    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @SystemControllerLog(description = "保存智能搜索主体信息")
    public String save(ZsSubject subject){
        int result = zsSubjectService.save(subject);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 修改主体信息
     * @param subject
     * @return
     */
    @RequestMapping(value = "/" ,method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改主体信息")
    public String update(ZsSubject subject){
        int result = zsSubjectService.update(subject);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 根据id删除主体信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE)
    @SystemControllerLog(description = "根据id删除主体信息")
    public String delete(@PathVariable("id") String id){
        int reslut = zsSubjectService.removeById(id);
        return ReturnDataUtil.saveOrUpdateOrDel(reslut);
    }

    /**
     * 根据id查询主体详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据id查询主体详细信息")
    public String getById(@PathVariable("id") String id){
        ZsSubject subject = zsSubjectService.getById(id);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功",subject);
    }

    /**
     * 按条件分页查询主体信息
     * @param subject
     * @param page
     * @return
     */
    @RequestMapping(value = "getByPage" , method = RequestMethod.GET)
    @SystemControllerLog(description = "按条件分页查询主体信息")
    public String getByPage(ZsSubject subject, Page page){
        List<ZsSubject> zsSubjects = zsSubjectService.listByCondition(subject, page);
        Integer count = zsSubjectService.countByCondition(subject);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功", new PageDataVO(count,zsSubjects));
    }


    /**
     * 下线主体
     * @param id
     * @return
     */
    @RequestMapping(value = "/offline/{id}",method = RequestMethod.PUT)
    @SystemControllerLog(description = "下线智能搜索主体")
    public String offline(@PathVariable("id")String id){
        Integer result = zsSubjectService.updateLine(id, "2");
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功",result);
    }

    /**
     * 上线主体
     * @param id
     * @return
     */
    @RequestMapping(value = "/online/{id}" , method = RequestMethod.PUT)
    @SystemControllerLog(description = "上线主体")
    public String online(@PathVariable("id")String id){
        Integer result = zsSubjectService.updateLine(id, "0");
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功",result);
    }



}
