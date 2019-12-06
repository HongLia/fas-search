package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsOverviewParam;
import com.fas.search.manage.mapper.ZsOverviewParamMapper;
import com.fas.search.manage.service.ZsOverviewParamService;
import com.fas.search.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索概览实体参数Service实现类
 * @date 2019/7/24
 */
@Service
@Transactional
public class ZsOverviewParamServiceImpl implements ZsOverviewParamService {

    @Autowired
    private ZsOverviewParamMapper zsOverviewParamMapper;

    @Override
    public List<Map<String, Object>> listParamsByOverviewId(String overview_id) {
        return zsOverviewParamMapper.listParamsByOverviewId(overview_id);
    }

    @Override
    public Integer updateParam(ZsOverviewParam param) {
        param.setUpdator(UserVOUtil.getUserID());
        return zsOverviewParamMapper.updateByPrimaryKeySelective(param);
    }
}
