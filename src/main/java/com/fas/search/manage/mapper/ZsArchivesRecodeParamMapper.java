package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchivesRecodeParam;

public interface ZsArchivesRecodeParamMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsArchivesRecodeParam record);

    ZsArchivesRecodeParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsArchivesRecodeParam record);

    /**
     * 根据档案维度实体关联id查询
     * @param archivesEntityId
     * @return
     */
    ZsArchivesRecodeParam getByArchivesEntityId(String archivesEntityId);

}