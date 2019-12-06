package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsSubjectDics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 条件查询
     * @param zsSubjectDics
     * @return
     */
    ZsSubjectDics selectBySelective(ZsSubjectDics zsSubjectDics);
    /**
     * 通过主题id，查询出对应的配置模板项目，key对应的是dicid，value对应的是中文名
     * @param subjectId 主题id
     * @return
     */
    List<Map<String,String>> selectSubjectDataHeader(String subjectId);


    List<Map<String,String>> selectSubjectEntityHeader(@Param("subjectId") String subjectId, @Param("entityId") String entityId);

}