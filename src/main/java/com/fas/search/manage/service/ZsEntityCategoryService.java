package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsEntityCategory;
import java.util.List;
import java.util.Map;

/**
 * 智能搜索实体分相关解口方法
 */
public interface ZsEntityCategoryService {

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int removeById(String id);

    /**
     * 保存实体分类
     * @param record
     * @return
     */
    int save(ZsEntityCategory record);

    /**
     * 根据id获取实体分类信息
     * @param id
     * @return
     */
    ZsEntityCategory getById(String id);

    /**
     * 修改实体分类信息
     * @param record
     * @return
     */
    int update(ZsEntityCategory record);

    /**
     * 获取全部智搜实体分类
     * @return
     */
    List<Map> listCategoryTree();
}
