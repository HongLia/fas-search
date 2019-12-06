package com.fas.search.search.engine;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.search.bo.EngineInfoBO;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchFilterBO;
import com.fas.search.search.bo.SearchResultBO;

import java.util.List;
import java.util.Map;

/**
 *
 * 搜索引擎，搜索数据接口，用于搜索搜索引擎中的数据
 */
public interface SearchEngineService<T> extends SearchServiceBase{

    /**
     * 创建集合 0 成功   1、失败
     * @param collectionName
     */
    int createCollectoin(String collectionName) ;

    /**
     * 删除集合 0成功  1、失败
     * @param collectionName
     * @return
     */
    int deleteCollection(String collectionName);

    /**
     * 写文档
     * @param wdbo
     * @param datas
     * @return
     */
    Object writeDocument(EngineInfoBO<T> wdbo, List<Map<String,Object>> datas);


    /**
     * 清空collection
     * @param wdBo
     */
    void clearnCollectionData(EngineInfoBO<T> wdBo);

    /**
     * 释放连接
     * @param wdbo
     */
    void releaseClient(EngineInfoBO<T> wdbo);

    /**
     * 统计collection中数据量
     * @param collectionName
     * @return
     */
    Long countCollecitonNumber(String collectionName);


    /**
     * 批量查询多个实体
     * @param page  分页信息
     * @param searchConditionBOs    所有需要搜索的实体，实体中返回的字段
     * @param searchConditionBO 搜索基础信息，搜索关键字，搜索类型等信息
     * @return
     */
    SearchResultBO batchQueryResult( Page page, List<SearchConditionBO> searchConditionBOs,SearchConditionBO searchConditionBO);
}
