package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsOverviewParam;

import java.util.Map;
import java.util.List;

/**
 * 智能搜索概览实体属性Service
 */
public interface ZsOverviewParamService {

    /**
     * 通过 配置概览id找到实体属性
     * @param overview_id
     * @return
     */
    List<Map<String,Object>> listParamsByOverviewId(String overview_id);

    /**
     * 修改参数信息
     * @param param
     * @return
     */
    Integer updateParam(ZsOverviewParam param);
}
