package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsOverview;

public interface ZsOverviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZsOverview record);

    int insertSelective(ZsOverview record);

    ZsOverview selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZsOverview record);

    int updateByPrimaryKey(ZsOverview record);
}