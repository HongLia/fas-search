package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsInfobar;
import com.fas.search.manage.entity.ZsInfobarEntity;
import com.fas.search.manage.entity.ZsInfobarEntityDTO;
import com.fas.search.manage.entity.ZsInfobarParam;
import com.fas.search.manage.service.ZsInfobarEntityService;
import com.fas.search.manage.service.ZsInfobarService;
import com.fas.search.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther wuzy
 * @description 智能搜索信息栏Controller
 * @date 2019/8/6
 */
@RestController
@RequestMapping("infobar")
public class ZsInfobarController {

    @Autowired
    private ZsInfobarService zsInfobarService;

    @Autowired
    private ZsInfobarEntityService zsInfobarEntityService;


    /**
     * 新增信息栏信息
     * @param zsInfobar
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @SystemControllerLog(description = "新增信息栏信息")
    public String save(ZsInfobar zsInfobar){
        //新增
        int result = zsInfobarService.saveSelective(zsInfobar);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除信息栏信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除信息栏信息")
    public String delete(@PathVariable("id")String id){
        int result = zsInfobarService.removeById(id);
        return  ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改智能搜索信息栏信息
     * @param infobar
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改智能搜索信息栏信息")
    public String update(ZsInfobar infobar){
        //修改
        int result = zsInfobarService.updateSelective(infobar);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 根据主体id获取信息栏信息
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "/list/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主体id获取信息栏信息")
    public String listBySubjectId(@PathVariable("subject_id") String subject_id){
        //查询
        List<ZsInfobar> zsInfobars = zsInfobarService.listBySubjectId(subject_id);
        //返回
        return ReturnDataUtil.getData(zsInfobars);
    }

    /**
     * 新增智能搜索信息栏纬度配置
     * @param zsInfobarEntity
     * @return
     */
    @RequestMapping(value = "/archive",method = RequestMethod.POST)
    @SystemControllerLog(description = "新增智能搜索信息栏纬度配置")
    public String saveArchive(ZsInfobarEntity zsInfobarEntity){
        int result = zsInfobarEntityService.save(zsInfobarEntity);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改智能搜索信息栏纬度配置信息
     * @param zsInfobarEntity
     * @return
     */
    @RequestMapping(value = "/archive",method = RequestMethod.PUT )
    @SystemControllerLog(description = "修改智能搜索信息栏纬度配置信息")
    public String updateArchive(ZsInfobarEntity zsInfobarEntity){
        int result = zsInfobarEntityService.update(zsInfobarEntity);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 列表获取信息栏下纬度信息
     * @param infobar_id 信息栏id
     * @return
     */
    @RequestMapping(value = "/archive",method = RequestMethod.GET)
    @SystemControllerLog(description = "列表获取信息栏下纬度信息")
    public String listInfobarArchive(String infobar_id){
        List<ZsInfobarEntityDTO> zsInfobarEntityDTOS = zsInfobarEntityService.listArchiveInfo(infobar_id);
        return ReturnDataUtil.getData(zsInfobarEntityDTOS);
    }

    /**
     * 删除信息栏纬度配置信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delEntity/{id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除信息栏纬度配置信息")
    public String removeArchive(@PathVariable("id") String id){
        int result = zsInfobarEntityService.delete(id);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 获取主体信息栏实体属性
     * @return
     */
    @RequestMapping(value = "/listField/{infobar_entity_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取主体信息栏实体属性")
    public String listField(@PathVariable("infobar_entity_id")String infobar_entity_id){
        List<ZsInfobarParam> zsInfobarParams = zsInfobarEntityService.listArchiveParam(infobar_entity_id);
        return ReturnDataUtil.getData(zsInfobarParams);
    }

    /**
     * 修改主体信息栏实体属性
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateField",method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改主体信息栏实体属性")
    public String updateField(ZsInfobarParam param){
        Integer result = zsInfobarEntityService.updateParam(param);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 信息栏属性字段排序，上移或下移
     * @param field_id  字段id
     * @param sort_type 排序类型 0表示上移，1表示下移
     * @return
     */
    @RequestMapping(value = "/sortField/{field_id}/{sort_type}",method = RequestMethod.PUT)
    @SystemControllerLog(description = "信息栏属性字段排序，上移或下移")
    public String sortField(@PathVariable("field_id") String field_id, @PathVariable("sort_type") String sort_type){
        Integer result = zsInfobarEntityService.sortParam(field_id, sort_type);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }
}
