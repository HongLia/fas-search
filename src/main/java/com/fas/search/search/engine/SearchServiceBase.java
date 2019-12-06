package com.fas.search.search.engine;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchFilterBO;
import com.fas.search.search.bo.SearchResultBO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2019/1/22.
 * 搜索接口
 */
public interface SearchServiceBase {


    /**
     * 统计查询数据数量
     * @param searchConditionBO 搜索数据对象
     * @return
     */
    long queryNum(SearchConditionBO searchConditionBO);


    /**
     * 查询数据接口
     * @param page
     * @param searchConditionBO
     * @return
     */
    SearchResultBO queryResult(Page page, SearchConditionBO searchConditionBO);


    /**
     * 查询solr结果列表   文件查询时精确查询
     * @param key 文件查询中的内容
     * @param page  分页信息
     * @param searchConditionBO 搜索条件BO
     * @return
     */
    SearchResultBO queryByFile(Set<String> key, Page page, SearchConditionBO searchConditionBO);


    /**
     * 通过文档id查找数据
     * @param searchConditionBO 搜索条件BO
     * @param ids 搜索文档的id集合
     * @return
     */
    SearchResultBO queryResultByIds(SearchConditionBO searchConditionBO, List<String> ids);


    /**通过id获得详细信息   通过数据库查询
     * @param searchConditionBO 搜索条件BO
     * @return
     */

    Map<String,Object> getDetail(SearchConditionBO searchConditionBO);


    /**
     * 关注数据  根据数据id查询结果
     * @param page
     * @param searchConditionBO
     * @param ids
     * @return
     */
    Map<String,Object> queryResultByAttention(List<String> ids,Page page,SearchConditionBO searchConditionBO);




    /**
     * 根据身份证号码查询照片信息
     * @param map 参数集合
     * @return String 返回结果
     * @return
     */
    Map<String,String> queryImage( Map<String, Object> map);
}
