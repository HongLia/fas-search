package com.fas.search.manage.service;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsOverview;

import java.util.List;
import java.util.Map;

public interface ZsOverviewService {

    int removeByPrimaryKey(String id);

    int saveSelective(ZsOverview record);

    ZsOverview getByPrimaryKey(String id);


    /**
     * 按条件分页查询
     * @param overview
     * @param page
     * @return
     */
    List<Map<String,Object>> listByConditionAndPage(ZsOverview overview, Page page);


    /**
     * 按照条件统计数量
     * @param overview
     * @return
     */
    Integer countByCondition(ZsOverview overview);
}
