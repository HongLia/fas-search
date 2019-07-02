package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsSubject;

public interface ZsSubjectMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsSubject record);

    int insertSelective(ZsSubject record);

    ZsSubject selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsSubject record);

    int updateByPrimaryKey(ZsSubject record);
}