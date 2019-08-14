package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZsEntityFieldMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsEntityField record);

    ZsEntityField selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntityField record);

    /**
     * 根据实体id查询出实体的属性字段
     * @param entityId
     * @return
     */
    List<ZsEntityField> selectByEntityId(String entityId);

    /**
     * 批量插入实体属性
     * @param list
     * @return
     */
    int insertBatch(@Param("list")List<Map> list);

    /**
     *
     * @param record
     * @return
     */
    int insertSelectiveByMap(Map record);
}