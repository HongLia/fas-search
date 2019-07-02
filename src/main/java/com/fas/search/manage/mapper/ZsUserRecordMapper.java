package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsUserRecord;

public interface ZsUserRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsUserRecord record);

    int insertSelective(ZsUserRecord record);

    ZsUserRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsUserRecord record);

    int updateByPrimaryKey(ZsUserRecord record);
}