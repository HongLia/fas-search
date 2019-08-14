package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchivesRecodeParam;

public interface ZsArchivesRecodeParamMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsArchivesRecodeParam record);

    ZsArchivesRecodeParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsArchivesRecodeParam record);

}