package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsUserRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;


public interface ZsUserRecordMapper {

    int insertSelective(ZsUserRecord record);

    ZsUserRecord selectByPrimaryKey(String id);

    /**
     * 获取智能搜索用户使用情况
     * @return
     */
    Map<String,Object> getUserCensus();


    /**
     * 获取系统使用情况
     * @param record
     * @return
     */
    List<Map<String,Object>> listUse(@Param("record") ZsUserRecord record, @Param("page")Page page);

    /**
     * 统计使用情况总数
     * @param userName
     * @return
     */
    Integer countUse(@Param("userName") String userName);


    /**
     * 获取用户使用总情况
     * @param userName
     * @param page
     * @return
     */
    List<Map<String,Object>> listUser(@Param("userName")String userName,@Param("page") Page page);

    /**
     * 统计用户使用总记录数
     * @param userName
     * @return
     */
    Integer countUser(@Param("userName") String userName);


    /**
     * 获取用户最近搜索的五条记录
     * @param userid
     * @return
     */
    List<String> latelyRecord(String userid);


    /**
     * 按条件统计查询
     * @param zsUserRecord
     * @return
     */
    Long countSelected(ZsUserRecord zsUserRecord);
}