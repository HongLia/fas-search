package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsOverviewTemplate;

import java.util.List;
import java.util.Map;

public interface ZsOverviewTemplateService {


    /**
     * 根据主体id查询概览模板信息
     * @param subject_id
     * @return
     */
    List<Map<String, Object>> listTemplateBySubjectId(String subject_id);


    /**
     * 修改该栏目模板参数
     * @param template
     * @return
     */
    Integer updateTemplate(ZsOverviewTemplate template);

    /**
     * 删除主体概览模板字段
     * @param template_id
     * @return
     */
    Integer removeTemplate(String template_id);


    /**
     * 新增模板字段信息
     * @param template
     * @return
     */
    Integer saveTemplate(ZsOverviewTemplate template);
}
