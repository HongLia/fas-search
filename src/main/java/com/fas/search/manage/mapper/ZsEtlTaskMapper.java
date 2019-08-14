package com.fas.search.manage.mapper;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEtlTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 智能搜索实体抽取任务相关
 */
public interface ZsEtlTaskMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsEtlTask record);

    ZsEtlTask selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEtlTask record);


    /**
     * 按条件分页查询，如果两个都为空，则查询全部
     * @param etlTask
     * @param page
     * @return
     */
    List<ZsEtlTask> listByPage(@Param("etlTask") ZsEtlTask etlTask , @Param("page") Page page);

    /**
     * 统计智能搜索抽取任务数量
     * @param etlTask
     * @return
     */
    Integer countNum(@Param("etlTask") ZsEtlTask etlTask );
}