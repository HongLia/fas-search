package com.fas.search.search.service;

import com.fas.base.model.Page;
import com.fas.search.search.bo.SearchResultBO;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏Service
 */
public interface ZsUserAttentionService {

    /**
     * 查看当前用户是否收藏此数据
     * @param userId
     * @param entityId
     * @param subjectId
     * @param dataId
     * @return
     */
    Integer getUserIsAttention(String userId,String entityId,String subjectId,String dataId);

    /**
     * 用户收藏数据
     * @param userId
     * @param entityId
     * @param subjectId
     * @param dataId
     * @return
     */
    Integer addAttention(String userId,String entityId,String subjectId,String dataId,String object_id);

    /**
     * 取消用户收藏数据
     * @param userId
     * @param entityId
     * @param subjectId
     * @param dataId
     * @return
     */
    Integer removeAttention(String userId,String entityId,String subjectId,String dataId);

    /**
     * 获取当前用户关注数量
     * @param userId
     * @return
     */
    List<Map<String,Object>> userAttentionNum(String userId);

    /**
     * 查询用户收藏此数据数据量
     * @param subjectId
     * @param entityId
     * @param dataId
     * @return
     */
    Integer attentionNum(String subjectId, String entityId, String dataId );

    /**
     * 分页获取用户关注数据
     * @param page
     * @param subjectId
     * @return
     */
    List<Map<String,Object>> queryAttentionData(Page page , String subjectId);

}
