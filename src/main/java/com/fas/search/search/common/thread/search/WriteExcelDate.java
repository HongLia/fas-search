package com.fas.search.search.common.thread.search;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsOverview;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.mapper.ZsEntityMapper;
import com.fas.search.manage.mapper.ZsOverviewMapper;
import com.fas.search.manage.mapper.ZsOverviewTemplateMapper;
import com.fas.search.manage.mapper.ZsSubjectDicsMapper;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import com.fas.search.util.common.ListMapTransformUtil;
import com.fas.search.util.excel.ExcelDataWrite;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther wuzy
 * @description 多线程往excel写数据
 * @date 2019/11/14
 */
public class WriteExcelDate implements Runnable {

    public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 * 2);

    private ZsSubjectDicsMapper zsSubjectDicsMapper;

    private ZsOverviewMapper zsOverviewMapper;

    private ZsEntityMapper zsEntityMapper;

    private ZsOverviewTemplateMapper zsOverviewTemplateMapper;

    private SearchEngineService searchEngineService;

    private String key;
    private String subjectId;
    private String searchClass;

    private SXSSFWorkbook workbook ;
    private Sheet sheet;

    private CountDownLatch countDownLatch;


    //现在查询时，单次查询的个数
    private final Integer DOWNLOAD_QUERY_PAGE_SIZE = 5000;
    //下载的条数
    private final Integer DOWNLOAD_QUERY_NUMBER = 10000;

    @Override
    public void run() {
        try {
            List<Map<String, Object>> datas = getSubjectExcelData(key, subjectId, searchClass);
            Map<String, String> excelHeader = getExcelHeader(subjectId);
            Map<String, Map<String, String>> entityHeaders = getEntityHeader(subjectId);
            ExcelDataWrite.writeSubjectData(datas,excelHeader,entityHeaders,sheet,workbook);
        } finally {
            countDownLatch.countDown();
        }
    }


    public WriteExcelDate( ZsSubjectDicsMapper zsSubjectDicsMapper, ZsOverviewMapper zsOverviewMapper, ZsEntityMapper zsEntityMapper, ZsOverviewTemplateMapper zsOverviewTemplateMapper, SearchEngineService searchEngineService, String key, String subjectId, String searchClass, SXSSFWorkbook workbook, Sheet sheet, CountDownLatch countDownLatch) {
        this.zsSubjectDicsMapper = zsSubjectDicsMapper;
        this.zsOverviewMapper = zsOverviewMapper;
        this.zsEntityMapper = zsEntityMapper;
        this.zsOverviewTemplateMapper = zsOverviewTemplateMapper;
        this.searchEngineService = searchEngineService;
        this.key = key;
        this.subjectId = subjectId;
        this.searchClass = searchClass;
        this.workbook = workbook;
        this.sheet = sheet;
        this.countDownLatch = countDownLatch;
    }

    /**
     * 根据subjectExcelData
     * @param key
     * @param subjectId
     * @param searchClass
     * @return
     */
    public List<Map<String,Object>> getSubjectExcelData(String key, String subjectId, String searchClass){
        List<Map<String,Object>> datas = new ArrayList<>();
        Integer currentStart = 0;
        Page page = new Page();
        page.setLength(DOWNLOAD_QUERY_PAGE_SIZE);
        page.setStart(currentStart);
        while (true){
            SearchResultBO searchResultBO = searchSubjectData(key, subjectId, page, searchClass, false);
            List<Map<String, Object>> data = searchResultBO.getData();
            datas.addAll(data);
            if (data.size() < DOWNLOAD_QUERY_PAGE_SIZE || datas.size() >= DOWNLOAD_QUERY_NUMBER)
                break;
            page.setStart(page.getStart() + DOWNLOAD_QUERY_PAGE_SIZE);
        }
        //查询主题下配置的可用概览信息
        return datas;
    }

    public Map<String, String> getExcelHeader(String subjectId){
        List<Map<String, String>> listExcelHeader = zsSubjectDicsMapper.selectSubjectDataHeader(subjectId);
        Map<String, String> excelHeader = ListMapTransformUtil.listToMap(listExcelHeader, "dic_id", "cname");
        return excelHeader;
    }


    public Map<String,Map<String,String>> getEntityHeader(String subjectId){
        ZsOverview zsOverview = new ZsOverview();
        zsOverview.setEnable("0");
        zsOverview.setSubject_id(subjectId);
        List<ZsOverview> zsOverviews = zsOverviewMapper.listByCondition(zsOverview);
        Map<String,Map<String,String>> entityHeaders = new HashMap<>();
        for (int i = 0; i < zsOverviews.size(); i++) {
            List<Map<String, String>> listEntityHeader = zsSubjectDicsMapper.selectSubjectEntityHeader(subjectId, zsOverviews.get(i).getEntity_id());
            Map<String, String> entityHeader = ListMapTransformUtil.listToMap(listEntityHeader, "dic_id", "attename");
            entityHeaders.put(zsOverviews.get(i).getEntity_id(),entityHeader);
        }
        return entityHeaders;
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
}
