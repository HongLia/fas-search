package com.fas.search.manage.service;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsUserRecord;

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
     * @param record
     * @param page
     * @return
     */
    List<Map<String,Object>> listUse(ZsUserRecord record, Page page);

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


    /**
     * 获取用户最近的搜索的五条记录
     * @param userid
     * @return
     */
    List<String> latelyRecord(String userid);

    /**
     * 保存搜索记录
     * @param zsUserRecord
     * @return
     */
    Integer saveRecord(ZsUserRecord zsUserRecord);

    /**
     * 根据条件统计数量
     * @param zsUserRecord
     * @return
     */
    Long countRecord(ZsUserRecord zsUserRecord);
}
