package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchivesTableParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZsArchivesTableParamMapper {

    int insertSelective(ZsArchivesTableParam record);

    int updateSelective(ZsArchivesTableParam record);

    ZsArchivesTableParam selectByPrimaryKey(String id);

    /**
     * 初始化表格参数
     * @param ZsArchivesTableParam
     * @return
     */
    Integer initTableParam(ZsArchivesTableParam ZsArchivesTableParam);



    /**
     * 获取表格展示属性
     * @param archive_entity_id
     * @return
     */
    List<Map<String,Object>> listTableField(String archive_entity_id);

    /**
     * 交换表格类型展示数据顺序
     * @param sort0
     * @param sort1
     * @param archiveEntityId
     * @return
     */
    Integer swapSortValue(@Param("sort0") Integer sort0, @Param("sort1") Integer sort1, @Param("archiveEntityId") String archiveEntityId);

    /**
     * 获取当前表格字段中排序最大的值
     * @param archiveEntityId
     * @return
     */
    Integer selectMaxSortValue(String archiveEntityId);
}