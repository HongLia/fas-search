package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntity;

public interface ZsEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntity record);

    int insertSelective(ZsEntity record);

    ZsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntity record);

    int updateByPrimaryKey(ZsEntity record);
}