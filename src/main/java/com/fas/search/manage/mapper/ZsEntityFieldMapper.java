package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityField;

public interface ZsEntityFieldMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntityField record);

    int insertSelective(ZsEntityField record);

    ZsEntityField selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntityField record);

    int updateByPrimaryKey(ZsEntityField record);
}