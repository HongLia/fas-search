package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobarParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface ZsInfobarParamMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsInfobarParam record);

    ZsInfobarParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobarParam record);

    /**
     * 初始化主体信息栏属性
     * @param infobarParam
     * @return
     */
    int initParam(@Param("infobarParam") ZsInfobarParam infobarParam, @Param("entityId") String entityId);

    /**
     * 根据信息栏纬度id获取 该纬度的熟属性信息
     * @param archiveId
     * @return
     */
    List<ZsInfobarParam> listByArchive(String archiveId);

    /**
     * 交换两个排序值位置
     * @param sort0 排序值1
     * @param sort1 排序值2
     * @param archiveId 纬度id
     * @return
     */
    Integer swapSortValue(@Param("sort0") Integer sort0,@Param("sort1") Integer sort1, @Param("archiveId") String archiveId);

    /**
     * 查询信息栏纬度下最大的排序字段值
     * @param archiveId
     * @return
     */
    Integer selectMaxSortValue(String archiveId);
}