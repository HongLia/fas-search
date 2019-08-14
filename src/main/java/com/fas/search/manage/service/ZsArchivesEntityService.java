package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.entity.ZsArchivesEntity;
import com.fas.search.manage.entity.ZsArchivesRecodeParam;
import com.fas.search.manage.entity.ZsArchivesTableParam;

import java.util.List;
import java.util.Map;

/**
 * 智能搜索档案纬度-实体关联service
 */
public interface ZsArchivesEntityService {

    /**
     * 新增
     * @param archivesEntity
     * @return
     */
    Integer addArchivesEntity(ZsArchivesEntity archivesEntity);

    /**
     * 删除档案纬度实体
     * @param id
     * @return
     */
    Integer removeArchivesEntity(String id);


    /**
     * 获取表格类型展示数据的字段
     * @param archiveEntityId
     * @return
     */
    List<Map<String,Object>> listTableField(String archiveEntityId);

    /**
     * 修改表格类型展示数据展示名称
     * @param zsArchivesTableParam
     * @return
     */
    Integer updateTableField(ZsArchivesTableParam zsArchivesTableParam);

    /**
     * 表格展示数据字段排序
     * @return
     */
    Integer sortTableField(String fieldId,String sortType);


    /**
     * 更改记录列表形展示数据
     * @param recodeParam
     * @return
     */
    Integer updateRecord(ZsArchivesRecodeParam recodeParam);
}
