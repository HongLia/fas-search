package com.fas.search.search.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.*;
import com.fas.search.manage.mapper.*;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import com.fas.search.search.service.ZsUserAttentionService;
import com.fas.search.search.vo.OverviewDataVo;
import com.fas.search.util.common.CreateDataUtil;
import com.fas.search.util.user.UserVOUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @auther wuzy
 * @description 用户收藏Service
 * @date 2019/11/20
 */
@Service
@Transactional
public class ZsUserAttentionServiceImpl implements ZsUserAttentionService {

    @Autowired
    private ZsUserAttentionMapper zsUserAttentionMapper;

    @Autowired
    private SearchEngineService searchEngineService;

    @Autowired
    private ZsOverviewTemplateMapper zsOverviewTemplateMapper;

    @Autowired
    private ZsEntityMapper zsEntityMapper;

    @Autowired
    private ZsSubjectService zsSubjectService;

    @Autowired
    private ZsSubjectDicsMapper zsSubjectDicsMapper;

    @Autowired
    private ZsOverviewMapper zsOverviewMapper;

    @Override
    public Integer getUserIsAttention(String userId, String entityId, String subjectId, String dataId) {
        ZsUserAttention zsUserAttention = new ZsUserAttention();
        zsUserAttention.setUserid(userId);
        zsUserAttention.setEntity_id(entityId);
        zsUserAttention.setSubject_id(subjectId);
        zsUserAttention.setData_id(dataId);
        Integer countNum = zsUserAttentionMapper.countUserAttentionSelective(zsUserAttention);
        return countNum;
    }

    @Override
    public Integer addAttention(String userId, String entityId, String subjectId, String dataId,String objectId) {
        ZsUserAttention zsUserAttention = new ZsUserAttention();
        zsUserAttention.setData_id(dataId);
        zsUserAttention.setSubject_id(subjectId);
        zsUserAttention.setEntity_id(entityId);
        zsUserAttention.setObject_id(objectId);
        zsUserAttention.setUserid(userId);
        zsUserAttention.setId(CreateDataUtil.getUUID());
        int i = zsUserAttentionMapper.insertSelective(zsUserAttention);
        return i;
    }

    @Override
    public Integer removeAttention(String userId, String entityId, String subjectId, String dataId) {
        Integer result = zsUserAttentionMapper.cancleAttention(subjectId, entityId, dataId, userId);
        return result;
    }

    @Override
    public List<Map<String, Object>> userAttentionNum(String userId) {
        return zsUserAttentionMapper.userAttentionNum(userId);
    }

    @Override
    public Integer attentionNum(String subjectId, String entityId, String dataId) {
        return zsUserAttentionMapper.allAttentionNum(subjectId,entityId,dataId);
    }

    @Override
    public List<Map<String,Object>> queryAttentionData(Page page, String subjectId) {
        List<ZsUserAttention> zsUserAttentions = zsUserAttentionMapper.listUserAttention(UserVOUtil.getUserID(), subjectId, page);

        List<Map<String,Object>> resultData = new ArrayList<>();
        for (int i = 0; i < zsUserAttentions.size(); i++) {
            ZsUserAttention zsUserAttention = zsUserAttentions.get(i);
            String data_id = zsUserAttention.getData_id();
            String entity_id = zsUserAttention.getEntity_id();
            String subject_id = zsUserAttention.getSubject_id();
            SearchConditionBO searchConditionBO = new SearchConditionBO();
            List<ZsEntityField> returnField = zsOverviewTemplateMapper.listReturnField(subject_id, entity_id);
            searchConditionBO.setHl(false).setReturnField(returnField).setEntity(zsEntityMapper.selectByPrimaryKey(entity_id)).setSearchClass(SearchClass.STR);
            List<String> ids = new ArrayList<>();
            ids.add(data_id);
            SearchResultBO searchResultBO = searchEngineService.queryResultByIds(searchConditionBO, ids);
            if (CollectionUtils.isNotEmpty(searchResultBO.getData())){
                Map<String, Object> onceData = analysisQueryResult(searchResultBO.getData().get(0), data_id, zsUserAttention.getSubject_id(), entity_id);
                resultData.add(onceData);
            }
        }
        return resultData;
    }


    /**
     *
     * @param data
     * @param dataId
     * @param subjectId
     * @param entityId
     * @return
     */
    protected Map<String,Object> analysisQueryResult(Map<String,Object> data,String dataId, String subjectId,String entityId){
        //找到对应主题
        ZsSubject subject = zsSubjectService.getById(subjectId);
        //查询反馈的结果集
        //封装前台展示的map
        //一条数据
        Map<String,Object> onceReturnData = new HashMap<>();
        //主题id
        onceReturnData.put("subject_id",subject.getId());
        //主题名称
        onceReturnData.put("subject_name",subject.getName());
        //实体id
        onceReturnData.put("entity_id",entityId);
        //数据id
        onceReturnData.put("data_id",dataId);
        //查询主题对应的查询主键
        ZsSubjectDics tempzsSubjectDics = new ZsSubjectDics();
        tempzsSubjectDics.setSearchpk("0");
        tempzsSubjectDics.setSubject_id(subject.getId());
        ZsSubjectDics zsSubjectDics = zsSubjectDicsMapper.selectBySelective(tempzsSubjectDics);
        ZsOverviewParam zsOverviewParam = zsOverviewMapper.getObjectIdParam(subjectId, String.valueOf(data.get("ENTITY_ID")), zsSubjectDics.getId());
        if (zsOverviewParam != null){
            //放置对应的业务主键
            Object o = data.get(zsOverviewParam.getAttename());
            onceReturnData.put("object_id",o);
        }

        //查询出配置的长信息项，短信息项，标题
        List<Map<String, String>> params = zsOverviewTemplateMapper.listTemplateParam(subjectId, entityId);
        //如果没有配置，继续下一条
        if (CollectionUtils.isEmpty(params)){
            return null;
        }
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
                shortInfos.put(tempDic.get("dic_name"),data.get(tempDic.get("attename")));
            }else if ("3".equals(tempDic.get("template_type"))){//长信息项
                longInfo.put(tempDic.get("dic_name"),data.get(tempDic.get("attename")));
            }else if ("1".equals(tempDic.get("template_type"))){//标题
                titleInfo = data.get(tempDic.get("attename"));
            }
        }
        infos.put("1",titleInfo);
        infos.put("2",shortInfos);
        infos.put("3",longInfo);
        onceReturnData.put("infos",infos);
        //构造前台需要的vo对象
        //返回
        return onceReturnData;
    }

}
