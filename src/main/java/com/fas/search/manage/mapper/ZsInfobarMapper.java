package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobar;

public interface ZsInfobarMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsInfobar record);

    int insertSelective(ZsInfobar record);

    ZsInfobar selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobar record);

    int updateByPrimaryKey(ZsInfobar record);
}