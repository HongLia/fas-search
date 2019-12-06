package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsOverviewTemplate;
import com.fas.search.manage.mapper.ZsOverviewTemplateMapper;
import com.fas.search.manage.service.ZsOverviewTemplateService;
import com.fas.search.util.common.BeanEntityTransformUtil;
import com.fas.search.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索概览展示模板配置
 * @date 2019/7/26
 */
@Service
@Transactional
public class ZsOverviewTemplateServiceImpl implements ZsOverviewTemplateService {

    @Autowired
    private ZsOverviewTemplateMapper zsOverviewTemplateMapper;

    @Override
    public List<Map<String, Object>> listTemplateBySubjectId(String subject_id) {
        //调用查询接口，并返回数据
        return zsOverviewTemplateMapper.listTemplate(subject_id);
    }

    @Override
    public Integer updateTemplate(ZsOverviewTemplate template) {
        //设置修改人
        template.setUpdator(UserVOUtil.getUserID());
        //调用后台修改接口，返回结果
        return zsOverviewTemplateMapper.updateByPrimaryKeySelective(template);

    }

    @Override
    public Integer removeTemplate(String template_id) {
        return zsOverviewTemplateMapper.deleteByPrimaryKey(template_id);
    }

    @Override
    public Integer saveTemplate(ZsOverviewTemplate template) {
        BeanEntityTransformUtil.initCreateEntity(template);
        return zsOverviewTemplateMapper.insertSelective(template);
    }
}
