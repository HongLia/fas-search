package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntity;

import javax.swing.text.html.parser.Entity;
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

    /**
     * 通过主题id查询实体信息
     * @param subjectId
     * @return
     */
    List<ZsEntity> listSubjectEntity(String subjectId);
}