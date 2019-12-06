package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsOverview;
import com.fas.search.manage.entity.ZsOverviewParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZsOverviewMapper {

    int deleteByPrimaryKey(String id);

    int insertSelective(ZsOverview record);

    ZsOverview selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsOverview record);


    /**
     * 按条件分页查询
     * @param overview
     * @param page
     * @return
     */
    List<Map<String,Object>> listByConditionAndPage(@Param("overview") ZsOverview overview, @Param("page")Page page);

    /**
     * 按条件统计数量
     * @param overview
     * @return
     */
    Integer countByCondition(ZsOverview overview);


    /**
     * 条件查询概览信息
     * @param zsOverview
     * @return
     */
    List<ZsOverview> listByCondition(ZsOverview zsOverview);


    /**
     * 获取查询主键对应的字段信息
     * @param subjectId
     * @param entityId
     * @param dicId
     * @return
     */
    ZsOverviewParam getObjectIdParam(@Param("subject_id")String subjectId, @Param("entity_id")String entityId, @Param("dic_id")String dicId);

}