package com.fas.search.search.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.*;
import com.fas.search.manage.mapper.*;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.common.thread.search.WriteExcelDate;
import com.fas.search.search.common.thread.search.WriteExcelInfobarDate;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import com.fas.search.search.service.ZsOverviewSearchService;
import com.fas.search.search.vo.OverviewDataVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther wuzy
 * @description 橄榄数据查询实现类
 * @date 2019/11/11
 */
@Service
public class ZsOverviewSearchServiceImpl implements ZsOverviewSearchService {

    @Autowired
    private SearchEngineService searchEngineService;    //搜索引擎查询服务

    @Autowired
    private ZsEntityMapper zsEntityMapper;  //智搜实体查询服务

    @Autowired
    private ZsOverviewMapper zsOverviewMapper;

    @Autowired
    private ZsOverviewTemplateMapper zsOverviewTemplateMapper;

    @Autowired
    private ZsSubjectDicsMapper zsSubjectDicsMapper;

    @Autowired
    private ZsSubjectService zsSubjectService;

    @Autowired
    private ZsInfobarParamMapper zsInfobarParamMapper;

    @Autowired
    private ZsInfobarEntityMapper zsInfobarEntityMapper;


    @Override
    public OverviewDataVo listSubjectData(String key, String subjectId, Page page, String searchClass, boolean isHl) {
        SearchResultBO searchResultBO = searchSubjectData(key, subjectId, page, searchClass, isHl);
        //根据后台配置，封装查询结果
        OverviewDataVo overviewDataVo = analysisBatchSubjectResult(searchResultBO, page, subjectId);
        return overviewDataVo;
    }

    @Override
    public SXSSFWorkbook getSubjectExcel(String key, String subjectId, String searchClass) {
        //调用查询全部的方法
        return getAllExcel(key,searchClass,subjectId);
    }





    @Override
    public OverviewDataVo listAllData(String key, Page page, String searchClass, boolean isHl) {
        //调用查询接口，查询数据
        SearchResultBO searchResultBO = searchSubjectData(key, null, page, searchClass, isHl);
        //封装返回结果，
        OverviewDataVo overviewDataVo = analysisBatchAllResult(searchResultBO, page);
        //返回数据
        return overviewDataVo;
    }

    @Override
    public SXSSFWorkbook getAllExcel(String key, String searchClass,String subjectId) {
        //查询所有有效主题
        ZsSubject subject = new ZsSubject();
        subject.setEnable("0");
        subject.setId(subjectId);
        List<ZsSubject> zsSubjects = zsSubjectService.listByCondition(subject, null);
        //如果没有有效实体，则返回为空
        if (CollectionUtils.isEmpty(zsSubjects)){
            return null;
        }
        //新建workBook
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //新建
        CountDownLatch countDownLatch = new CountDownLatch(zsSubjects.size());
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(zsSubjects.size());
            for (int i = 0; i < zsSubjects.size(); i++) {
                Sheet sheet = workbook.createSheet(zsSubjects.get(i).getName());
                executorService.submit(new WriteExcelDate(zsSubjectDicsMapper,zsOverviewMapper,zsEntityMapper,zsOverviewTemplateMapper,searchEngineService,key,zsSubjects.get(i).getId(),searchClass,workbook,sheet,countDownLatch));
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
        return workbook;
    }

    @Override
    public SearchResultBO entityData(String key, Page page,String entityId, String infobarEntityId, String searchClass, boolean isHl) {
        //构造搜索BO
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setEntity(zsEntityMapper.selectByPrimaryKey(entityId));
        searchConditionBO.setHl(isHl);
        searchConditionBO.setSearchClass(SearchClass.getSearchClass(searchClass));
        searchConditionBO.setKey(key);
        //查找搜索字段，即详细字段
        searchConditionBO.setSearchField(zsInfobarParamMapper.listInfobarEntityParam(infobarEntityId,"0",null));
        //查找返回字段，即概览字段
        searchConditionBO.setReturnField(zsInfobarParamMapper.listInfobarEntityParam(infobarEntityId,null,"0"));
        //调用查询接口
        SearchResultBO searchResultBO = searchEngineService.queryResult(page, searchConditionBO);
        //返回数据
        return searchResultBO;
    }

    @Override
    public Map<String,Object> entityDetailData(String entityId, String infobarEntityId, String dataId) {
        //构造搜索条件
        ZsEntity zsEntity = zsEntityMapper.selectByPrimaryKey(entityId);
        if (zsEntity == null){
            return null;
        }
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setEntity(zsEntityMapper.selectByPrimaryKey(entityId));
        searchConditionBO.setHl(false);
        searchConditionBO.setSearchClass(SearchClass.STR);
        searchConditionBO.setKey(dataId);
        //查找搜索字段，即详细字段
        List<ZsEntityField> zsEntityFields = new ArrayList<>();
        ZsEntityField entityField = new ZsEntityField();
        entityField.setEname(zsEntity.getIncrement_field());
        zsEntityFields.add(entityField);
        searchConditionBO.setSearchField(zsEntityFields);
        //查找返回字段，即概览字段
        searchConditionBO.setReturnField(zsInfobarParamMapper.listInfobarEntityParam(infobarEntityId,"0",null));
        //调用查询接口
        Map<String, Object> detail = searchEngineService.getDetail(searchConditionBO);
        //构造新map，key为中文，value为值
        Map<String,Object> cDetail = new LinkedHashMap<>();
        for (int i = 0; i < searchConditionBO.getReturnField().size(); i++) {
            String ename = searchConditionBO.getReturnField().get(i).getEname();
            String cname = searchConditionBO.getReturnField().get(i).getCname();
            cDetail.put(cname,detail.get(ename));
        }
        return cDetail;
    }

    @Override
    public SXSSFWorkbook getInfobarExcel(String key, String infobarId, String searchClass, boolean isHl) {
        //查询信息栏下配置对应的所有实体信息
        List<ZsInfobarEntityDTO> zsInfobarEntityDTOS = zsInfobarEntityMapper.listArchiveInfo(infobarId,null);
        //栏
        CountDownLatch countDownLatch = new CountDownLatch(zsInfobarEntityDTOS.size());
        //线程池
        ExecutorService executorService = null;
        SXSSFWorkbook workbook = null;
        try {
            executorService = Executors.newFixedThreadPool(zsInfobarEntityDTOS.size());
            workbook = new SXSSFWorkbook();
            for (int i = 0; i < zsInfobarEntityDTOS.size(); i++) {
                //新建sheet
                Sheet sheet = workbook.createSheet(zsInfobarEntityDTOS.get(i).getName());
                //构建搜索查询BO
                SearchConditionBO searchConditionBO = new SearchConditionBO();
                //设置实体
                searchConditionBO.setEntity(zsEntityMapper.selectByPrimaryKey(zsInfobarEntityDTOS.get(i).getEntity_id()));
                //返回字段
                List<ZsEntityField> returnField = zsInfobarParamMapper.listInfobarEntityParam(zsInfobarEntityDTOS.get(i).getId(), "0", null);
                //设置返回字段
                searchConditionBO.setReturnField(returnField);
                //设置搜索字段
                searchConditionBO.setSearchField(returnField);
                //设置搜索类型
                searchConditionBO.setSearchClass(SearchClass.getSearchClass(searchClass));
                //设置关键字
                searchConditionBO.setKey(key);
                //设置高亮
                searchConditionBO.setHl(isHl);
                //提交任务，查询
                executorService.submit(new WriteExcelInfobarDate(searchEngineService,sheet,countDownLatch,searchConditionBO,workbook));
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
        return workbook;
    }


    /**
     * 调用相关接口查询数据
     * @param key
     * @param subjectId
     * @param page
     * @param searchClass
     * @param isHl
     * @return
     */
    protected  SearchResultBO searchSubjectData(String key, String subjectId, Page page, String searchClass, boolean isHl){
        //查询主题下配置的可用概览信息
        ZsOverview zsOverview = new ZsOverview();
        zsOverview.setEnable("0");
        zsOverview.setSubject_id(subjectId);
        List<ZsOverview> zsOverviews = zsOverviewMapper.listByCondition(zsOverview);

        //构建基础查询BO
        SearchConditionBO searchConditionBO = new SearchConditionBO();
        searchConditionBO.setKey(key).setSearchClass(SearchClass.getSearchClass(searchClass)).setHl(isHl);
        //构建批量查询BO
        List<SearchConditionBO> searchConditionBOS = new ArrayList<>();
        for (int i = 0; i < zsOverviews.size(); i++) {
            SearchConditionBO sc = new SearchConditionBO();
            //设置实体
            sc.setEntity(zsEntityMapper.selectByPrimaryKey(zsOverviews.get(i).getEntity_id()));
            //设置返回字段
            sc.setReturnField(zsOverviewTemplateMapper.listReturnField(zsOverviews.get(i).getSubject_id(),zsOverviews.get(i).getEntity_id()));
            //设置搜索字段
            sc.setSearchField(zsOverviewTemplateMapper.listSearchField(zsOverviews.get(i).getSubject_id(),zsOverviews.get(i).getEntity_id()));
            searchConditionBOS.add(sc);
        }
        //调用接口查询数据
        return searchEngineService.batchQueryResult(page, searchConditionBOS, searchConditionBO);
    }

    /**
     * 根据主题概览配置，封装查询反馈数据
     * @param searchResultBO
     * @param page
     * @param subjectId
     * @return
     */
    protected OverviewDataVo analysisBatchSubjectResult(SearchResultBO searchResultBO, Page page, String subjectId){
        //找到对应主题
        ZsSubject subject = zsSubjectService.getById(subjectId);
        //数据量
        Long total = searchResultBO.getTotal();
        //查询反馈的结果集
        List<Map<String, Object>> data = searchResultBO.getData();
        //真实反馈的数据
        List<Map<String,Object>> resultData = new ArrayList<>();
        //封装前台展示的map
        for (int i = 0; i < data.size(); i++) {
            //一条数据
            Map<String,Object> onceReturnData = new HashMap<>();
            //主题id
            onceReturnData.put("subject_id",subject.getId());
            //主题名称
            onceReturnData.put("subject_name",subject.getName());
            //实体id
            onceReturnData.put("entity_id",data.get(i).get("ENTITY_ID"));
            //数据id
            onceReturnData.put("data_id",data.get(i).get("id"));
            //查询主题对应的查询主键
            ZsSubjectDics tempzsSubjectDics = new ZsSubjectDics();
            tempzsSubjectDics.setSearchpk("0");
            tempzsSubjectDics.setSubject_id(subject.getId());
            ZsSubjectDics zsSubjectDics = zsSubjectDicsMapper.selectBySelective(tempzsSubjectDics);
            ZsOverviewParam zsOverviewParam = zsOverviewMapper.getObjectIdParam(subjectId, String.valueOf(data.get(i).get("ENTITY_ID")), zsSubjectDics.getId());
            if (zsOverviewParam != null){
                //放置对应的业务主键
                Object o = data.get(i).get(zsOverviewParam.getAttename());
                if (o != null){
                    o = String.valueOf(o).replaceAll("<font style='color:red !important'>","");
                    o = String.valueOf(o).replaceAll("</font>","");
                }
                onceReturnData.put("object_id",o);
            }

            //查询出配置的长信息项，短信息项，标题
            List<Map<String, String>> params = zsOverviewTemplateMapper.listTemplateParam(subject.getId(), String.valueOf(data.get(i).get("ENTITY_ID")));
            //如果没有配置，继续下一条
            if (CollectionUtils.isEmpty(params)){
                continue;
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
                    shortInfos.put(tempDic.get("dic_name"),data.get(i).get(tempDic.get("attename")));
                }else if ("3".equals(tempDic.get("template_type"))){//长信息项
                    longInfo.put(tempDic.get("dic_name"),data.get(i).get(tempDic.get("attename")));
                }else if ("1".equals(tempDic.get("template_type"))){//标题
                    titleInfo = data.get(i).get(tempDic.get("attename"));
                }
            }
            infos.put("1",titleInfo);
            infos.put("2",shortInfos);
            infos.put("3",longInfo);
            onceReturnData.put("infos",infos);
            resultData.add(onceReturnData);
        }
        //构造前台需要的vo对象
        OverviewDataVo overviewDataVo = new OverviewDataVo(resultData,total,page);
        //返回
        return overviewDataVo;
    }
    /**
     * 根据主题概览配置，封装查询反馈数据
     * @param searchResultBO
     * @param page
     * @return
     */
    protected OverviewDataVo analysisBatchAllResult(SearchResultBO searchResultBO, Page page){
        //数据量
        Long total = searchResultBO.getTotal();
        //查询反馈的结果集
        List<Map<String, Object>> data = searchResultBO.getData();
        //真实反馈的数据
        List<Map<String,Object>> resultData = new ArrayList<>();
        //封装前台展示的map
        for (int i = 0; i < data.size(); i++) {

            //一条数据
            Map<String,Object> onceReturnData = new HashMap<>();
            //实体id
            onceReturnData.put("entity_id",data.get(i).get("ENTITY_ID"));
            Object entity_id = data.get(i).get("ENTITY_ID");
            ZsOverview zsOverview = new ZsOverview();
            zsOverview.setEnable("0");
            zsOverview.setEntity_id(String.valueOf(entity_id));
            List<ZsOverview> zsOverviews = zsOverviewMapper.listByCondition(zsOverview);
            if (CollectionUtils.isEmpty(zsOverviews)){
                continue;
            }
            ZsSubject subject = zsSubjectService.getById(zsOverviews.get(0).getSubject_id());
            //主题id
            onceReturnData.put("subject_id",subject.getId());
            //主题名称
            onceReturnData.put("subject_name",subject.getName());

            //数据id
            onceReturnData.put("data_id",data.get(i).get("id"));
            //查询主题对应的查询主键
            ZsSubjectDics tempzsSubjectDics = new ZsSubjectDics();
            tempzsSubjectDics.setSearchpk("0");
            tempzsSubjectDics.setSubject_id(subject.getId());
            ZsSubjectDics zsSubjectDics = zsSubjectDicsMapper.selectBySelective(tempzsSubjectDics);
            //放置对应的业务主键
            onceReturnData.put("object_id",data.get(i).get(zsSubjectDics.getEname()));
            //查询出配置的长信息项，短信息项，标题
            List<Map<String, String>> params = zsOverviewTemplateMapper.listTemplateParam(subject.getId(), String.valueOf(data.get(i).get("ENTITY_ID")));
            //如果没有配置，继续下一条
            if (CollectionUtils.isEmpty(params)){
                continue;
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
                    shortInfos.put(tempDic.get("dic_name"),data.get(i).get(tempDic.get("attename")));
                }else if ("3".equals(tempDic.get("template_type"))){//长信息项
                    longInfo.put(tempDic.get("dic_name"),data.get(i).get(tempDic.get("attename")));
                }else if ("1".equals(tempDic.get("template_type"))){//标题
                    titleInfo = data.get(i).get(tempDic.get("attename"));
                }
            }
            infos.put("1",titleInfo);
            infos.put("2",shortInfos);
            infos.put("3",longInfo);
            onceReturnData.put("infos",infos);
            resultData.add(onceReturnData);
        }
        //构造前台需要的vo对象
        OverviewDataVo overviewDataVo = new OverviewDataVo(resultData,total,page);
        //返回
        return overviewDataVo;
    }

}
