package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsOverview;
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

}