package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsSubjectDics;

import java.util.Map;
import java.util.List;

public interface ZsSubjectDicsMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsSubjectDics record);

    ZsSubjectDics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsSubjectDics record);


    /**
     * 根据中文名、英文名、查询主键做检索，统计当前主体下符合要求的个数
     * @param dic
     * @return
     */
    Map<String,Long> countCheckExists(ZsSubjectDics dic);


    /**
     * 根据主题id，获取主题的信息项
     * @param subjectId
     * @return
     */
    List<ZsSubjectDics> listBySubjectId(String subjectId);

}