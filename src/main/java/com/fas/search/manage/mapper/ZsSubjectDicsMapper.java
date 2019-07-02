package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsSubjectDics;

public interface ZsSubjectDicsMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsSubjectDics record);

    int insertSelective(ZsSubjectDics record);

    ZsSubjectDics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsSubjectDics record);

    int updateByPrimaryKey(ZsSubjectDics record);
}