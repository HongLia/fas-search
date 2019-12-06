package com.fas.search.search.service;

import com.fas.base.model.Page;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.vo.OverviewDataVo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;

/**
 * 概览搜索相关接口
 */
public interface ZsOverviewSearchService {

    /**
     * 根据主题查询概览数据
     * @param key   查询关键字
     * @param subjectId 主题id
     * @param page  分页信息
     * @param searchClass   搜索类型
     * @param isHl  是否高亮
     * @return
     */
    OverviewDataVo listSubjectData(String key, String subjectId , Page page,String searchClass,boolean isHl);
    /**
     * 根据主题查询,导出查询数据
     * @param key   查询关键字
     * @param subjectId 主题id
     * @param searchClass   搜索类型
     * @return
     */
    SXSSFWorkbook getSubjectExcel(String key, String subjectId , String searchClass);


    /**
     * 根据主题查询概览数据
     * @param key   查询关键字
     * @param page  分页信息
     * @param searchClass   搜索类型
     * @param isHl  是否高亮
     * @return
     */
    OverviewDataVo listAllData(String key, Page page,String searchClass,boolean isHl);


    /**
     * 根据主题查询,导出查询数据
     * @param key   查询关键字
     * @param searchClass   搜索类型
     * @return
     */
    SXSSFWorkbook getAllExcel(String key , String searchClass,String subjectId);


    /**
     * 分页获取实体数据
     * @param key
     * @param searchClass
     * @param isHl
     * @return
     */
    SearchResultBO entityData(String key, Page page, String entityId, String infobarEntityId, String searchClass,boolean isHl);


    /**
     * 根据数据id，实体id，信息栏实体id查询信息栏配置实体详细信息
     * @param entityId
     * @param infobarEntityId
     * @param dataId
     * @return
     */
    Map<String,Object> entityDetailData(String entityId, String infobarEntityId, String dataId);


    /**
     * 导出信息栏数据
     * @param key 关键字
     * @param infobarId 信息栏
     * @param searchClass
     * @return
     */
    SXSSFWorkbook getInfobarExcel(String key,String infobarId,String searchClass,boolean isHl);




}
