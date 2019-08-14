package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchivesEntity;

public interface ZsArchivesEntityMapper {

    int deleteByPrimaryKey(String id);

    int insertSelective(ZsArchivesEntity record);

    ZsArchivesEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsArchivesEntity record);


}