package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.Page;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsEtlTask;
import com.fas.search.manage.service.ZsEtlTaskService;
import com.fas.search.util.view.ReturnDataUtil;
import com.fas.search.manage.vo.PageDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("task")
public class ZsEtlTaskController {

    @Autowired
    private ZsEtlTaskService zsEtlTaskService;


    /**
     * 新增抽取任务
     * @param task
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @SystemControllerLog(description = "新增抽取任务")
    public String save(ZsEtlTask task){
        //保存任务信息
        int result = zsEtlTaskService.saveSelective(task);
        //返回操作结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改抽取任务
     * @param task
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改抽取任务")
    public String update(ZsEtlTask task){
        //修改任务信息
        int i = zsEtlTaskService.updateByPrimaryKeySelective(task);
        //返回修改细信息
        return ReturnDataUtil.saveOrUpdateOrDel(i);
    }

    /**
     * 删除智能搜索抽取任务
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" ,method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除智能搜索抽取任务")
    public String remove(@PathVariable("id") String id){
        //根据id进行删除
        int i = zsEtlTaskService.removeByPrimaryKey(id);
        //返回操作结果
        return ReturnDataUtil.saveOrUpdateOrDel(i);
    }

    /**
     * 根据id查找任务详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    @SystemControllerLog(description = "根据id查找任务详情")
    public String getById(@PathVariable("id")String id){
        //根据id查询
        ZsEtlTask task = zsEtlTaskService.getByPrimaryKey(id);
        //返回操作结果
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",task);
    }


    /**
     * 根据条件分页查询实体抽取任务
     * @param etlTask 条件
     * @param page  分页信息
     * @return
     */
    @RequestMapping(value = "getByPage" , method = RequestMethod.GET)
    @SystemControllerLog(description = "根据条件分页查询实体抽取任务")
    public String getByPage(ZsEtlTask etlTask, Page page){
        //分页查询数据
        List<ZsEtlTask> tasks = zsEtlTaskService.listByPage(etlTask, page);
        //统计数据量
        Integer counts = zsEtlTaskService.countNum(etlTask);
        //返回数据
        return ReturnDataUtil.getPageData(counts,tasks);
    }
}
