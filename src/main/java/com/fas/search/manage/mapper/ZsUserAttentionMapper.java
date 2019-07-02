package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsUserAttention;

public interface ZsUserAttentionMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsUserAttention record);

    int insertSelective(ZsUserAttention record);

    ZsUserAttention selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsUserAttention record);

    int updateByPrimaryKey(ZsUserAttention record);
}