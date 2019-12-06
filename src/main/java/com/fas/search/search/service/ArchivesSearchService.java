package com.fas.search.search.service;

import com.fas.base.model.Page;
import com.fas.search.search.bo.SearchResultBO;

import java.util.Map;
import java.util.List;

/**
 * 档案搜索接口服务
 */
public interface ArchivesSearchService {

    /**
     * 查询档案页基础信息
     * @param entityId
     * @param subjectId
     * @param dataId
     * @return
     */
    Map<String,Object> baseinfo(String entityId,String subjectId,String dataId);


    /**
     * 智搜档案表格类维度信息获取数据
     * @param archivesId
     * @param objectId
     * @return
     */
    SearchResultBO tableData(String archivesId, String objectId, Page page);

    /**
     * 智搜档案表格类维度信息获取数据
     * @param archivesId
     * @param dataId
     * @return
     */
    Map<String,Object> tableDataDetail(String archivesId, String dataId);


    /**
     * 智能搜索记录列表维度数据
     * @param archivesId
     * @param objectId
     * @return
     */
    List<Map<String,String>> recordData(String archivesId, String objectId);

}
