package com.fas.search.search.common.thread.search;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.manage.entity.ZsInfobarEntity;
import com.fas.search.manage.entity.ZsOverview;
import com.fas.search.manage.mapper.ZsEntityMapper;
import com.fas.search.manage.mapper.ZsOverviewMapper;
import com.fas.search.manage.mapper.ZsOverviewTemplateMapper;
import com.fas.search.manage.mapper.ZsSubjectDicsMapper;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import com.fas.search.util.common.ListMapTransformUtil;
import com.fas.search.util.excel.ExcelDataWrite;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @auther wuzy
 * @description 多线程往excel写数据
 * @date 2019/11/14
 */
public class WriteExcelInfobarDate implements Runnable {

    //搜索引擎实现类
    private SearchEngineService searchEngineService;

    //sheet
    private Sheet sheet;

    private CountDownLatch countDownLatch;

    private SearchConditionBO searchConditionBO;

    private SXSSFWorkbook workbook;

    //现在查询时，单次查询的个数
    private final Integer DOWNLOAD_QUERY_PAGE_SIZE = 5000;
    //下载的条数
    private final Integer DOWNLOAD_QUERY_NUMBER = 50000;

    @Override
    public void run() {
        try {
            //获取数据
            List<Map<String, Object>> infobarExcelData = getInfobarExcelData();
            //构造excel头
            Map<String, String> excelHeader = getExcelHeader();
            //写sheet数据
            ExcelDataWrite.writeData(infobarExcelData,excelHeader,sheet,workbook);
        } finally {
            countDownLatch.countDown();
        }
    }


    /**
     * 获取Excel数据
     * @return
     */
    public List<Map<String,Object>> getInfobarExcelData(){
        List<Map<String,Object>> datas = new ArrayList<>();
        Integer currentStart = 0;
        Page page = new Page();
        page.setLength(DOWNLOAD_QUERY_PAGE_SIZE);
        page.setStart(currentStart);
        while (true){
            SearchResultBO searchResultBO = searchEngineService.queryResult(page, searchConditionBO);
            List<Map<String, Object>> data = searchResultBO.getData();
            datas.addAll(data);
            if (data.size() < DOWNLOAD_QUERY_PAGE_SIZE || datas.size() >= DOWNLOAD_QUERY_NUMBER)
                break;
            page.setStart(page.getStart() + DOWNLOAD_QUERY_PAGE_SIZE);
        }
        //查询主题下配置的可用概览信息
        return datas;
    }

    /**
     * 构造Excel头信息
     * @return
     */
    public Map<String,String> getExcelHeader(){
        List<ZsEntityField> searchField = searchConditionBO.getSearchField();
        Map<String,String> excelHeader = new LinkedHashMap<>();
        for (int i = 0; i < searchField.size(); i++) {
            ZsEntityField entityField = searchField.get(i);
            excelHeader.put(entityField.getEname(),entityField.getCname());
        }
        return excelHeader;
    }



    public WriteExcelInfobarDate(SearchEngineService searchEngineService, Sheet sheet, CountDownLatch countDownLatch, SearchConditionBO searchConditionBO, SXSSFWorkbook workbook) {
        this.searchEngineService = searchEngineService;
        this.sheet = sheet;
        this.countDownLatch = countDownLatch;
        this.searchConditionBO = searchConditionBO;
        this.workbook = workbook;
    }
}
