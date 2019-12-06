package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.Page;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsOverview;
import com.fas.search.manage.entity.ZsOverviewParam;
import com.fas.search.manage.entity.ZsOverviewTemplate;
import com.fas.search.manage.service.ZsOverviewParamService;
import com.fas.search.manage.service.ZsOverviewService;
import com.fas.search.manage.service.ZsOverviewTemplateService;
import com.fas.search.util.view.ReturnDataUtil;
import com.fas.search.manage.vo.PageDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索橄榄配置Controller
 * @date 2019/7/23
 */
@RestController
@RequestMapping("overview")
public class ZsOverviewController {

    @Autowired
    private ZsOverviewService zsOverviewService;

    @Autowired
    private ZsOverviewParamService zsOverviewParamService;

    @Autowired
    private ZsOverviewTemplateService zsOverviewTemplateService;

    /**
     * 新增智能搜索概览实体
     * @param overview
     * @return
     */
    @RequestMapping(value = "/" ,method = RequestMethod.POST)
    @SystemControllerLog(description = "新增智能搜索概览实体")
    public String save(ZsOverview overview){
        //调用保存接口
        int result = zsOverviewService.saveSelective(overview);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }


    /**
     * 删除智能搜索实体概览
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除智能搜索实体概览")
    public String delete(@PathVariable("id") String  id){
        //调用删除接口
        int result = zsOverviewService.removeByPrimaryKey(id);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 分页查询智能搜索主体概览配置实体
     * @param overview
     * @param page
     * @return
     */
    @RequestMapping(value = "getByPage" , method = RequestMethod.GET)
    @SystemControllerLog(description = "分页查询智能搜索主体概览配置实体")
    public String getByPage(ZsOverview overview, Page page){
        //分页查询数据
        List<Map<String, Object>> maps = zsOverviewService.listByConditionAndPage(overview, page);
        //查询总数
        Integer count = zsOverviewService.countByCondition(overview);
        //返回结果
        return ReturnDataUtil.getPageData(count,maps);
    }

    /**
     * 获取概览实体的属性信息
     * @param overview_id
     * @return
     */
    @RequestMapping(value = "getParam/{overview_id}" , method = RequestMethod.GET)
    @SystemControllerLog(description = "获取概览实体的属性信息")
    public String getParam(@PathVariable("overview_id")String overview_id){
        //调用查询接口
        List<Map<String, Object>> atts = zsOverviewParamService.listParamsByOverviewId(overview_id);
        //返回数据
        return ReturnDataUtil.getData(atts);
    }


    /**
     * 修改概览实体属性信息
     * @param param_id 参数id
     * @param dic_id    字典id
     * @param search_field  是否是搜索字段
     * @return
     */
    @RequestMapping(value = "updateParam" ,method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改概览实体属性信息")
    public String updateParam(String param_id,String dic_id,String search_field){
        ZsOverviewParam param = new ZsOverviewParam();
        param.setId(param_id);
        param.setDic_id(dic_id);
        param.setSearch_field(search_field);
        Integer result = zsOverviewParamService.updateParam(param);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 获取主体配置概览模板配置信息
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "getTemplate/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取主体配置概览模板配置信息")
    public String getTemplate(@PathVariable("subject_id")String subject_id){
        //调用查询接口，
        List<Map<String, Object>> templates = zsOverviewTemplateService.listTemplateBySubjectId(subject_id);
        //返回数据
        return ReturnDataUtil.getData(templates);
    }

    /**
     * 修改模板信息
     * @param template_id
     * @param dic_id
     * @return
     */
    @RequestMapping(value = "updateTemplate/{template_id}/{dic_id}" , method = RequestMethod.PUT)
    @SystemControllerLog(description = "修改模板信息" )
    public String updateTemplate(@PathVariable("template_id")String template_id,@PathVariable("dic_id")String dic_id){
        ZsOverviewTemplate template = new ZsOverviewTemplate();
        template.setId(template_id);
        template.setDic_id(dic_id);
        //调用后台接口
        Integer result = zsOverviewTemplateService.updateTemplate(template);
        //返回结果
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除概览数据模板配置
     * @param template_id
     * @return
     */
    @RequestMapping(value = "deleteTemplate/{template_id}" , method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除概览数据模板配置")
    public String deleteTemplate(@PathVariable("template_id")String template_id){
        //调用删除接口
        Integer result = zsOverviewTemplateService.removeTemplate(template_id);
        //返回数据
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 新增智能搜索概览模板实体配置
     * @param template
     * @return
     */
    @RequestMapping(value = "addTemplate",method = RequestMethod.POST)
    @SystemControllerLog(description = "新增智能搜索概览模板实体配置")
    public String addTemplate(ZsOverviewTemplate template){
        Integer result = zsOverviewTemplateService.saveTemplate(template);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }
}
