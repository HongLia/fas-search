package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsUserAttention;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

public interface ZsUserAttentionMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsUserAttention record);

    ZsUserAttention selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsUserAttention record);

    /**
     * 根据条件统计数量
     * @param zsUserAttention
     * @return
     */
    Integer countUserAttentionSelective(ZsUserAttention zsUserAttention);

    /**
     * 取消用户收藏
     * @param subjectId
     * @param entityId
     * @param dataId
     * @param userId
     * @return
     */
    Integer cancleAttention(@Param("subjectId")String subjectId,@Param("entityId") String entityId,@Param("dataId")String dataId,@Param("userId")String userId);


    /**
     * 获取当前用户关注数量
     * @param userId
     * @return
     */
    List<Map<String,Object>> userAttentionNum(String userId);


    /**
     * 统计所有关注此数据用户量
     * @param subjectId
     * @param entityId
     * @param dataId
     * @return
     */
    Integer allAttentionNum(@Param("subjectId") String subjectId,@Param("entityId") String entityId,@Param("dataId") String dataId);

    /**
     * 获取用户关注数据
     * @param userId
     * @param subjectId
     * @param page
     * @return
     */
    List<ZsUserAttention> listUserAttention(@Param("userId")String userId, @Param("subjectId")String subjectId, @Param("page")Page page);



}