package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.metadata.service.MetaDataInterfaceService;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.manage.service.ZsEntityFieldService;
import com.fas.search.manage.service.ZsEntityService;
import com.fas.search.manage.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("entity")
public class ZsEntityController {

    @Autowired
    private ZsEntityService zsEntityService;

    @Autowired
    private ZsEntityFieldService zsEntityFieldService;

    @Autowired
    private MetaDataInterfaceService metaDataInterfaceService;
    

    /**
     * 新增实体
     * @param entity
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @SystemControllerLog(description = "新增智能搜索实体")
    public String save(ZsEntity entity){
        int result = zsEntityService.save(entity);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改实体
     * @param entity
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT )
    @SystemControllerLog(description = "修改智能搜素实体")
    public String update(ZsEntity entity){
        int result = zsEntityService.update(entity);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除实体
     * @param id 根据id删除实体
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除智能搜实体")
    public String removeById(@PathVariable("id") String id){
        int result = zsEntityService.removeById(id);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 根据实体id查询智能搜索实体详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "查询智能搜索实体详细信息")
    public String getById(@PathVariable("id") String id){
        ZsEntity entity = zsEntityService.getById(id);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",entity);
    }

    /**
     * 查询实体属性字段
     * @param id
     * @return
     */
    @RequestMapping(value = "listFields/{id}" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "查询智能搜索实体属性字段")
    public String listFields(@PathVariable("id") String id){
        List<ZsEntityField> zsEntityFields = zsEntityFieldService.listByEntityId(id);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",zsEntityFields);
    }

    /**
     * 获取实体的引用关系
     * @param id
     * @return
     */
    @RequestMapping(value = "quote/{id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取实体的引用关系")
    public String quote(@PathVariable("id")String id){
        List<Map> quote = zsEntityService.quote(id);
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",quote);
    }

    /**
     * 获取数据源信息
     * @return
     */
    @RequestMapping(value = "getDataSource" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "获取数据源信息")
    public String getDataSource(){
        //调用元数管理据接口查询数据
        String dataSourceMsg = metaDataInterfaceService.getDataSource();
        return dataSourceMsg;
    }


    /**
     * 获取数据源数据库表信息
     * @param data_source_id
     * @return
     */
    @RequestMapping(value = "/themeData/{data_source_id}" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "获取数据源数据库表信息")
    public String themeData(@PathVariable("data_source_id")String data_source_id){
        String themeData = metaDataInterfaceService.getThemeData(data_source_id, "0", "0", "0");
        return themeData;
    }

    /**
     * 获取数据源数据库表字段信息
     * @param metadata_id
     * @return
     */
    @RequestMapping(value = "listTableFields/{metadata_id}")
    @SystemControllerLog(description = "获取数据源数据库表字段信息")
    public String listTableFields(@PathVariable("metadata_id")String metadata_id){
        String column = metaDataInterfaceService.getColumn(metadata_id);
        return column;
    }
}
