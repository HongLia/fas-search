package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityCategory;

public interface ZsEntityCategoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntityCategory record);

    int insertSelective(ZsEntityCategory record);

    ZsEntityCategory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntityCategory record);

    int updateByPrimaryKey(ZsEntityCategory record);
}