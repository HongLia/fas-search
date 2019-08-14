package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntity;
import java.util.List;
import java.util.Map;
public interface ZsEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntity record);

    int insertSelective(ZsEntity record);

    ZsEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntity record);

    int updateByPrimaryKey(ZsEntity record);

    /**
     * 根据实体id查询实体的引用关系
     * @param id
     * @return
     */
    List<Map> quote(String id);
}