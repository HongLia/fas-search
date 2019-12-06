package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.log.SystemLogAspect;
import com.fas.base.model.LogTypeConstants;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.entity.ZsArchivesEntity;
import com.fas.search.manage.entity.ZsArchivesRecodeParam;
import com.fas.search.manage.entity.ZsArchivesTableParam;
import com.fas.search.manage.service.ZsArchivesEntityService;
import com.fas.search.manage.service.ZsArchivesService;
import com.fas.search.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索档案纬度相关信息配置controller
 * @date 2019/8/8
 */
@RestController
@RequestMapping("archives")
public class ZsArchivesController {
    @Autowired
    private ZsArchivesService zsArchivesService;

    @Autowired
    private ZsArchivesEntityService zsArchivesEntityService;

    /**
     * 新增主体档案纬度
     * @param archives
     * @return
     */
    @RequestMapping(value = "/" ,method = RequestMethod.POST)
    @SystemControllerLog(description = "新增主体档案纬度",operType = LogTypeConstants.ADD)
    public String saveArchives(ZsArchives archives){
        Integer result = zsArchivesService.saveArchive(archives);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改档案纬度信息
     * @param archives
     * @return
     */
    @RequestMapping(value = "/" ,method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改档案纬度信息")
    public String updateArchive(ZsArchives archives){
        Integer result = zsArchivesService.updateArchive(archives);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除主体档案纬度
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    @SystemControllerLog(description = "删除主体档案纬度",operType = LogTypeConstants.DELETE)
    public String removeArchives(@PathVariable("id") String id){
        Integer result = zsArchivesService.removeArchive(id);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 获取主体下档案纬度信息
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "/list/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取主体下档案纬度信息",operType = LogTypeConstants.SELECT)
    public String listArchives(@PathVariable("subject_id") String subject_id){
        List<ZsArchives> zsArchives = zsArchivesService.listArchives(subject_id);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",zsArchives);
    }


    /**
     * 新增主体档案纬度展示实体
     * @param zsArchivesEntity
     * @return
     */
    @RequestMapping(value = "addEntity",method = RequestMethod.POST)
    @SystemControllerLog(description = "新增主体档案纬度展示实体",operType = LogTypeConstants.ADD)
    public String addArchivesEntity(ZsArchivesEntity zsArchivesEntity){
        Integer result = zsArchivesEntityService.addArchivesEntity(zsArchivesEntity);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 删除主体档案纬度配置实体
     * @param id
     * @return
     */
    @RequestMapping(value = "/delEntity/{id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除主体档案纬度配置实体" ,operType = LogTypeConstants.DELETE)
    public String removeArhiveEntity(@PathVariable("id")String id){
        Integer result = zsArchivesEntityService.removeArchivesEntity(id);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 获取主体档案表格类型维度属性信息
     * @param archives_entity_id
     * @return
     */
    @RequestMapping(value = "/listTableField/{archives_entity_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取主体档案表格类型维度属性信息",operType = LogTypeConstants.SELECT)
    public String listTableField(@PathVariable("archives_entity_id")String archives_entity_id){
        List<Map<String, Object>> result = zsArchivesEntityService.listTableField(archives_entity_id);
        return ReturnDataUtil.getData(result);
    }


    /**
     * 修改主体档案表格纬度表格属性信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateField/{param_id}",method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改主体档案表格纬度表格属性信息",operType = LogTypeConstants.MODIFY)
    public String updateField(@PathVariable("param_id")String param_id, ZsArchivesTableParam param){
        param.setId(param_id);
        Integer result = zsArchivesEntityService.updateTableField(param);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 排序主体档案纬度表格形属性顺序
     * @param field_id
     * @param sort_type
     * @return
     */
    @RequestMapping("sortField/{field_id}/{sort_type}")
    @SystemControllerLog(description = "排序主体档案纬度表格形属性顺序",operType = LogTypeConstants.MODIFY)
    public String sortTableField(@PathVariable("field_id")String field_id,@PathVariable("sort_type")String sort_type){
        Integer result = zsArchivesEntityService.sortTableField(field_id, sort_type);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 修改记录列表形展示数据展示样式
     * @param param
     * @return
     */
    @RequestMapping(value = "updateRecodeArchives" , method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改记录列表形展示数据展示样式",operType = LogTypeConstants.MODIFY)
    public String updateRecodeArchives(ZsArchivesRecodeParam param){
        Integer result = zsArchivesEntityService.updateRecord(param);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

}
