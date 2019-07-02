package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobarParam;

public interface ZsInfobarParamMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsInfobarParam record);

    int insertSelective(ZsInfobarParam record);

    ZsInfobarParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobarParam record);

    int updateByPrimaryKey(ZsInfobarParam record);
}