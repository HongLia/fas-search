package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.entity.ZsSubjectDics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZsSubjectMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsSubject record);

    ZsSubject selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsSubject record);

    /**
     * 根据条件分页查询主体
     * @param subject
     * @param page
     * @return
     */
    List<ZsSubject> selectBySelective(@Param("subject") ZsSubject subject, @Param("page") Page page);

    /**
     * 根据条件统计数量
     * @param subject
     * @return
     */
    Integer countBySelective(ZsSubject subject);

    /**
     * 查询主体名称有没有重复
     * @param subject
     */
    Integer checkUpdateNameRepeat(ZsSubject subject);



}