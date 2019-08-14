package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsEntity;

import java.util.List;
import java.util.Map;

/**
 * 智能搜索实体相关业务接口
 */
public interface ZsEntityService {

    int removeById(String id);

    int save(ZsEntity record);

    ZsEntity getById(String id);

    int update(ZsEntity record);

    /**
     * 根据实体id取查询实体的所有引用关系
     *  其中包括
     *      概览
     *      信息栏
     *      档案
     * @param id
     * @return
     */
    List<Map> quote(String id);

    /**
     * 初始化实体属性，从元数据管理找到表字段信息，同步成实体信息
     * @param entity
     * @return
     */
    int initEntityField(ZsEntity entity);
}
