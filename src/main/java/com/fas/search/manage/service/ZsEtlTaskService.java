package com.fas.search.manage.service;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEtlTask;
import java.util.List;


/**
 * 智能搜索数据抽取任务
 */
public interface ZsEtlTaskService {

    int removeByPrimaryKey(String id);

    int saveSelective(ZsEtlTask record);

    ZsEtlTask getByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEtlTask record);


    /**
     * 按条件分页查询，如果两个都为空，则查询全部
     * @param etlTask
     * @param page
     * @return
     */
    List<ZsEtlTask> listByPage(ZsEtlTask etlTask , Page page);

    /**
     * 统计智能搜索抽取任务数量
     * @param etlTask
     * @return
     */
    Integer countNum(ZsEtlTask etlTask);
}
