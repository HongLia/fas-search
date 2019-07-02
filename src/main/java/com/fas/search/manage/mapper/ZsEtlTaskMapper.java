package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEtlTask;

public interface ZsEtlTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZsEtlTask record);

    int insertSelective(ZsEtlTask record);

    ZsEtlTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZsEtlTask record);

    int updateByPrimaryKey(ZsEtlTask record);
}