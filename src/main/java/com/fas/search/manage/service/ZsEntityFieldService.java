package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsEntityField;

import java.util.List;

/**
 * 智能搜索实体属性接口
 */
public interface ZsEntityFieldService {

    int removeById(String id);

    int save(ZsEntityField record);

    ZsEntityField getById(String id);

    int updateByPrimaryKeySelective(ZsEntityField record);

    /**
     * 根据实体id查询出实体的属性字段
     * @param entityId
     * @return
     */
    List<ZsEntityField> listByEntityId(String entityId);
}
