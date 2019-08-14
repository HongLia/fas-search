package com.fas.search.manage.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsOverview;
import com.fas.search.manage.entity.ZsOverviewParam;
import com.fas.search.manage.mapper.ZsOverviewMapper;
import com.fas.search.manage.mapper.ZsOverviewParamMapper;
import com.fas.search.manage.service.ZsOverviewService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 主体概览配置先关实现类
 * @date 2019/7/23
 */
@Service
@Transactional
public class ZsOverviewServiceImpl implements ZsOverviewService {

    @Autowired
    private ZsOverviewMapper zsOverviewMapper;

    @Autowired
    private ZsOverviewParamMapper zsOverviewParamMapper;

    @Override
    public int removeByPrimaryKey(String id) {
        ZsOverview overview = new ZsOverview();
        //设置id
        overview.setId(id);
        //设置修改人、状态
        BeanEntityTransformUtil.initDeleteEntity(overview);
        //修改，逻辑删除
        return zsOverviewMapper.updateByPrimaryKeySelective(overview);
    }

    @Override
    public int saveSelective(ZsOverview record) {
        //添加初始化信息
        BeanEntityTransformUtil.initCreateEntity(record);
        //同步实体属性
        ZsOverviewParam param = new ZsOverviewParam();
        //橄榄id
        param.setOverview_id(record.getId());
        //实体id
        param.setEntity_id(record.getEntity_id());
        //主体id
        param.setSubject_id(record.getSubject_id());
        //修改人
        param.setUpdator(UserVOUtil.getUserID());
        //创建人
        param.setCreator(UserVOUtil.getUserID());
        //添加初始化属性
        zsOverviewParamMapper.initParam(param);

        //新增概览实体
        return zsOverviewMapper.insertSelective(record);
    }

    @Override
    public ZsOverview getByPrimaryKey(String id) {
        return zsOverviewMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<Map<String, Object>> listByConditionAndPage(ZsOverview overview, Page page) {
        List<Map<String, Object>> overviews = zsOverviewMapper.listByConditionAndPage(overview, page);
        return overviews;
    }

    @Override
    public Integer countByCondition(ZsOverview overview) {
        return zsOverviewMapper.countByCondition(overview);
    }
}
