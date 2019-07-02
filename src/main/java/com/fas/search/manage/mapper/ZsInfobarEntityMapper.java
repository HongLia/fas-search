package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobarEntity;

public interface ZsInfobarEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsInfobarEntity record);

    int insertSelective(ZsInfobarEntity record);

    ZsInfobarEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobarEntity record);

    int updateByPrimaryKey(ZsInfobarEntity record);
}