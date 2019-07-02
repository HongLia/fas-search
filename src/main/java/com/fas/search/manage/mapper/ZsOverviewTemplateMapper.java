package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsOverviewTemplate;

public interface ZsOverviewTemplateMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsOverviewTemplate record);

    int insertSelective(ZsOverviewTemplate record);

    ZsOverviewTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsOverviewTemplate record);

    int updateByPrimaryKey(ZsOverviewTemplate record);
}