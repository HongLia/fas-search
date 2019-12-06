package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsOverviewParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZsOverviewParamMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsOverviewParam record);

    ZsOverviewParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsOverviewParam record);

    int updateByPrimaryKey(ZsOverviewParam record);

    /**
     * 新增智能搜索概览实体时  初始化概览实体属性
     * @param record
     * @return
     */
    int initParam(ZsOverviewParam record);


    /**
     * 根据概览实体id 查询属性
     * @param overview_id
     * @return
     */
    List<Map<String,Object>> listParamsByOverviewId(String overview_id);

}