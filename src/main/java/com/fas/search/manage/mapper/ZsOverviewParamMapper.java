package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsOverviewParam;

public interface ZsOverviewParamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZsOverviewParam record);

    int insertSelective(ZsOverviewParam record);

    ZsOverviewParam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZsOverviewParam record);

    int updateByPrimaryKey(ZsOverviewParam record);
}