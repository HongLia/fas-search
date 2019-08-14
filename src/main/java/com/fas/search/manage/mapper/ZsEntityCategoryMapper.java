package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsEntityCategory;
import java.util.List;
import java.util.Map;

public interface ZsEntityCategoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(ZsEntityCategory record);

    int insertSelective(ZsEntityCategory record);

    ZsEntityCategory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsEntityCategory record);

    int updateByPrimaryKey(ZsEntityCategory record);

    /**
     * 获取全部智搜实体分类
     * @return
     */
    List<Map> selectCategoryTree();

    /**
     * 根据条件查询智搜实体分类
     * @param category
     * @return
     */
    List<ZsEntityCategory> selectBySelective(ZsEntityCategory category);
}