package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZaArchives;

public interface ZaArchivesMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZaArchives record);

    int insertSelective(ZaArchives record);

    ZaArchives selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZaArchives record);

    int updateByPrimaryKey(ZaArchives record);
}