package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.manage.entity.ZsOverviewTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
public interface ZsOverviewTemplateMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsOverviewTemplate record);

    ZsOverviewTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsOverviewTemplate record);


    /**
     * 获取智能搜索主体下配置的概览模板信息
     * @param subject_id
     * @return
     */
    List<Map<String,Object>> listTemplate(String subject_id);

    /**
     * 根据主题id和实体id找到对应的返回字段
     * @return
     */
    List<ZsEntityField> listReturnField(@Param("subjectId") String subjectId,@Param("entityId")String entityId);
    /**
     * 根据主题id和实体id找到对应的返回字段
     * @return
     */
    List<ZsEntityField> listSearchField(@Param("subjectId") String subjectId,@Param("entityId")String entityId);

    /**
     * 获取主题下对应实体配置的字段
     * @param subjectId
     * @return
     */
    List<Map<String,String>> listTemplateParam(@Param("subjectId") String subjectId,@Param("entityId")String entityId);
}