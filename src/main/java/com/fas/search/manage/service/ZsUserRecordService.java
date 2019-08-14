package com.fas.search.manage.service;

import com.fas.base.model.Page;

import java.util.Map;
import java.util.List;
/**
 * 智能搜索用户记录Service
 */
public interface ZsUserRecordService {

    /**
     * 获取智能搜索用户使用情况
     * @return
     */
    Map<String,Object> getUserCensus();

    /**
     * 获取系统使用情况
     * @param userName
     * @param page
     * @return
     */
    List<Map<String,Object>> listUse(String userName, Page page);

    /**
     * 统计使用情况总数
     * @param userName
     * @return
     */
    Integer countUse(String userName);
    /**
     * 获取用户使用情况
     * @param userName
     * @param page
     * @return
     */
    List<Map<String,Object>> listUser(String userName, Page page);

    /**
     * 统计用户使用总记录数
     * @param userName
     * @return
     */
    Integer countUser(String userName);
}
