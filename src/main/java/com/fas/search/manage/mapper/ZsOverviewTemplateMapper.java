package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsOverviewTemplate;

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
}