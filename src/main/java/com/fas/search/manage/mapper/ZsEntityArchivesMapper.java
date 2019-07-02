package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityArchives;

public interface ZsEntityArchivesMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntityArchives record);

    int insertSelective(ZsEntityArchives record);

    ZsEntityArchives selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntityArchives record);

    int updateByPrimaryKey(ZsEntityArchives record);
}