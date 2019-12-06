package com.fas.search.search.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.*;
import com.fas.search.manage.mapper.*;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import com.fas.search.search.service.ArchivesSearchService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.util.*;

/**
 * @auther wuzy
 * @description 档案搜索接口服务实现类
 * @date 2019/11/19
 */
@Service
public class ArchivesSearchServiceImpl implements ArchivesSearchService {

    @Autowired
    private SearchEngineService searchEngineService;

    @Autowired
    private ZsEntityMapper zsEntityMapper;

    @Autowired
    private ZsOverviewTemplateMapper zsOverviewTemplateMapper;

    @Autowired
    private ZsUserAttentionMapper zsUserAttentionMapper;

    @Autowired
    private ZsArchivesTableParamMapper zsArchivesTableParamMapper;

    @Autowired
    private ZsArchivesEntityMapper zsArchivesEntityMapper;

    @Autowired
    private ZsArchivesRecodeParamMapper zsArchivesRecodeParamMapper;

    @Override
    public Map<String, Object> baseinfo(String entityId, String subjectId, String dataId) {
        //构造搜索条件
        ZsEntity zsEntity = zsEntityMapper.selectByPrimaryKey(entityId);
        if (zsEntity == null){
            return null;
        }
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setEntity(zsEntityMapper.selectByPrimaryKey(entityId));
        searchConditionBO.setSearchClass(SearchClass.getSearchClass("2"));
        searchConditionBO.setHl(false);
        searchConditionBO.setKey(dataId);
        //查找搜索字段，即详细字段
        List<ZsEntityField> zsEntityFields = new ArrayList<>();
        ZsEntityField entityField = new ZsEntityField();
        entityField.setEname(zsEntity.getIncrement_field());
        searchConditionBO.setSearchField(zsEntityFields);
        zsEntityFields.add(entityField);
        //查找返回字段，即概览字段
        searchConditionBO.setReturnField(zsOverviewTemplateMapper.listReturnField(subjectId,entityId));
        Map<String, Object> detail = searchEngineService.getDetail(searchConditionBO);
        //查询反馈的结果集

        //一条数据
        Map<String,Object> onceReturnData = new HashMap<>();
        //查询主题对应的查询主键
        //查询出配置的长信息项，短信息项，标题
        List<Map<String, String>> params = zsOverviewTemplateMapper.listTemplateParam(subjectId, entityId);
        //如果没有配置，继续下一条
        //保存长信息项，短信息项，标题的map
        Map<String,Object> infos = new HashMap<>();
        Iterator<Map<String, String>> iteratorShort = params.iterator();
        //短信息项
        Map<String,Object> shortInfos = new LinkedHashMap<>();
        //长信息项
        Map<String,Object> longInfo = new LinkedHashMap<>();
        //标题
        Object titleInfo = null;
        while (iteratorShort.hasNext()){
            Map<String, String> tempDic = iteratorShort.next();
            if ("2".equals(tempDic.get("template_type"))){//短信息项
                shortInfos.put(tempDic.get("dic_name"),detail.get(tempDic.get("attename")));
            }else if ("3".equals(tempDic.get("template_type"))){//长信息项
                longInfo.put(tempDic.get("dic_name"),detail.get(tempDic.get("attename")));
            }else if ("1".equals(tempDic.get("template_type"))){//标题
                titleInfo = detail.get(tempDic.get("attename"));
            }
        }
        infos.put("1",titleInfo);
        infos.put("2",shortInfos);
        infos.put("3",longInfo);
        onceReturnData.put("infos",infos);
        //返回
        return onceReturnData;
    }

    @Override
    public SearchResultBO tableData(String archivesId, String objectId, Page page) {
        List<ZsArchivesEntity> zsArchivesEntities = zsArchivesEntityMapper.listArchivesEntity(archivesId);
        if(CollectionUtils.isEmpty(zsArchivesEntities)){
            return new SearchResultBO();
        }
        List<Map<String, Object>> tableFields = zsArchivesTableParamMapper.listTableField(zsArchivesEntities.get(0).getId());
        ZsEntity zsEntity = zsEntityMapper.selectByPrimaryKey(zsArchivesEntities.get(0).getEntity_id());
        List<ZsEntityField> overview_field = getTargetField(tableFields, "overview_field");
        List<ZsEntityField> search_pk = getTargetField(tableFields, "search_pk");
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setHl(false);
        searchConditionBO.setKey(objectId);
        searchConditionBO.setSearchClass(SearchClass.STR);
        searchConditionBO.setSearchField(search_pk);
        searchConditionBO.setReturnField(overview_field);
        searchConditionBO.setEntity(zsEntity);
        SearchResultBO searchResultBO = searchEngineService.queryResult(page, searchConditionBO);
        return searchResultBO;
    }

    @Override
    public Map<String,Object> tableDataDetail(String archivesId, String dataId) {

        List<ZsArchivesEntity> zsArchivesEntities = zsArchivesEntityMapper.listArchivesEntity(archivesId);
        if(CollectionUtils.isEmpty(zsArchivesEntities)){
            return new HashMap<>();
        }
        ZsEntity zsEntity = zsEntityMapper.selectByPrimaryKey(zsArchivesEntities.get(0).getEntity_id());
        List<Map<String, Object>> tableFields = zsArchivesTableParamMapper.listTableField(zsArchivesEntities.get(0).getId());
        List<ZsEntityField> detail_field = getTargetField(tableFields, "detail_field");
        //构造搜索条件
        if (zsEntity == null){
            return null;
        }
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setEntity(zsEntity);
        searchConditionBO.setHl(false).setSearchClass(SearchClass.STR).setKey(dataId).setReturnField(detail_field);
        //查找搜索字段，即详细字段
        List<ZsEntityField> zsEntityFields = new ArrayList<>();
        ZsEntityField entityField = new ZsEntityField();
        entityField.setEname(zsEntity.getIncrement_field());
        zsEntityFields.add(entityField);
        searchConditionBO.setSearchField(zsEntityFields);
        Map<String, Object> detail = searchEngineService.getDetail(searchConditionBO);
        Map<String, Object> resultData = analysisDataDetail(detail,detail_field);
        return resultData;
    }

    @Override
    public List<Map<String, String>> recordData(String archivesId, String objectId) {
        //查询当前维度下所有关联实体信数据
        List<ZsArchivesEntity> zsArchivesEntities = zsArchivesEntityMapper.listArchivesEntity(archivesId);
        if(CollectionUtils.isEmpty(zsArchivesEntities)){
            return new ArrayList<>();
        }
        //分页查询关联数据条数
        Page page = new Page();
        page.setStart(0);
        page.setLength(30);
        //便利查询实体，组装数据
        List<Map<String,String>> datas = new ArrayList<>();
        for (int i = 0; i < zsArchivesEntities.size(); i++) {
            //每条配置关联
            ZsArchivesEntity zsArchivesEntity = zsArchivesEntities.get(i);
            //对应实体
            ZsEntity zsEntity = zsEntityMapper.selectByPrimaryKey(zsArchivesEntity.getEntity_id());
            //组装  查询BO
            SearchConditionBO searchConditionBO = new SearchConditionBO();
            //对应查询字段
            String relationField = zsArchivesEntity.getRelation_field();
            List<ZsEntityField> searchField = new ArrayList<>();
            ZsEntityField entityField = new ZsEntityField();
            entityField.setEname(relationField);
            searchField.add(entityField);
            //对应返回字段
            ZsArchivesRecodeParam param  = zsArchivesRecodeParamMapper.getByArchivesEntityId(zsArchivesEntity.getId());
            //解析配置字段
            List<String> tempReturnField = analysisRecordsParamIndex(param.getShowContent());
            //解析返回字段
            List<ZsEntityField> returnFields = analysisRecordsParam(tempReturnField);
            searchConditionBO
                    .setReturnField(returnFields)
                    .setSearchField(searchField)
                    .setKey(objectId)
                    .setEntity(zsEntity)
                    .setSearchClass(SearchClass.STR)
                    .setHl(false)
                    .setKey(objectId);
            //查询数据
            SearchResultBO searchResultBO = searchEngineService.queryResult(page, searchConditionBO);
            //解析记录数据
            analysisRecordData(searchResultBO.getData(),datas,zsEntity,tempReturnField,param.getShowContent(),zsArchivesEntity);
        }
        return datas;
    }


    /**
     * 获取目标字段
     * @param tableFields
     * @param target
     * @return
     */
    public List<ZsEntityField> getTargetField(List<Map<String, Object>> tableFields,String target){
        List<ZsEntityField> zsEntityFields = new ArrayList<>();
        for (int i = 0; i < tableFields.size(); i++) {
            Map<String, Object> fieldMap = tableFields.get(i);
            Object o = fieldMap.get(target);
            if ("0".equals(o)){
                ZsEntityField entityField = new ZsEntityField();
                entityField.setCname(String.valueOf(fieldMap.get("showname")));
                entityField.setEname(String.valueOf(fieldMap.get("ename")));
                zsEntityFields.add(entityField);
            }
        }
        return zsEntityFields;
    }


    /**
     * 表格维度数据详细信息
     * @param data
     * @param fields
     * @return
     */
    public Map<String,Object> analysisDataDetail(Map<String,Object> data,List<ZsEntityField> fields){
        Map<String,Object> resultData = new LinkedHashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            String ename = fields.get(i).getEname();
            String cname = fields.get(i).getCname();
            resultData.put(cname,data.get(ename));
        }
        return resultData;
    }

    public List<String> analysisRecordsParamIndex(String param){
        List<String> fields = new ArrayList<>();
        if (StringUtils.isEmpty(param)){
            return fields;
        }
        for (int i = 0; i < param.length(); i++) {
            if (param.charAt(i) == '['){
                i ++;
                String fieldEname = "";
                while (param.charAt(i) != ']' && i < param.length()){
                    fieldEname += param.charAt(i);
                    i++;
                }
                fields.add(fieldEname);
                i --;
            }
        }
        return fields;
    }

    /**
     * 解析成实体返回字段
     * @param param
     * @return
     */
    public List<ZsEntityField> analysisRecordsParam(List<String> param){
        List<ZsEntityField> fields = new ArrayList<>();
        if (CollectionUtils.isEmpty(param)){
            return fields;
        }
        for (int i = 0; i < param.size(); i++) {
            String fieldEname = param.get(i);
            ZsEntityField entityField = new ZsEntityField();
            entityField.setEname(fieldEname);
            fields.add(entityField);
        }
        return fields;
    }

    /**
     * 解析记录列表数据
     * @param searchResultData
     * @param datas
     * @param entity
     * @param params
     * @param targetMsg
     * @param zsArchivesEntity
     */
    public void analysisRecordData(List<Map<String,Object>> searchResultData,List<Map<String,String>> datas,ZsEntity entity,List<String> params,String targetMsg,ZsArchivesEntity zsArchivesEntity){
        for (int i = 0; i < searchResultData.size(); i++) {
            Map<String, Object> d = searchResultData.get(i);
            String tempTargetData = targetMsg;
            for (int j = 0; j < params.size(); j++) {
                tempTargetData = tempTargetData.replace("[" + params.get(j) + "]",d.get(params.get(j)) == null ? "（暂无数据）" : String.valueOf(d.get(params.get(j))));
            }
            Map<String,String> onceData = new HashMap<>();
            String show_source = zsArchivesEntity.getShow_source();
            if ("0".equalsIgnoreCase(show_source)){
                onceData.put("source",entity.getEntity_name());
            }
            onceData.put("context",tempTargetData);
            datas.add(onceData);
        }
    }
}
