package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchivesEntity;
import java.util.List;

public interface ZsArchivesEntityMapper {

    int deleteByPrimaryKey(String id);

    int insertSelective(ZsArchivesEntity record);

    ZsArchivesEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsArchivesEntity record);

    /**
     * 通过维度id，获取维度实体关联信息
     * @param archivesId
     * @return
     */
    List<ZsArchivesEntity> listArchivesEntity(String archivesId);

}