package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsInfobarEntity;
import com.fas.search.manage.entity.ZsInfobarEntityDTO;
import com.fas.search.manage.entity.ZsInfobarParam;

import java.util.List;
import java.util.Map;

public interface ZsInfobarEntityService {

    /**
     * 保存信息栏纬度信息
     * @param zsInfobarEntity
     * @return
     */
    int save(ZsInfobarEntity zsInfobarEntity);

    /**
     * 修改智能搜索信息栏纬度信息
     * @param zsInfobarEntity
     * @return
     */
    int update(ZsInfobarEntity zsInfobarEntity);

    /**
     * 删除信息栏纬度信息
     * @param id
     * @return
     */
    int delete(String id);


    /**
     * 根据信息栏id，查询信息栏各个纬度信息，以及包含纬度属性
     * @param infobarId
     * @return
     */
    List<ZsInfobarEntityDTO> listArchiveInfo(String infobarId);

    /**
     * 获取档案纬度下的属性信息
     * @param archiveId
     * @return
     */
    List<ZsInfobarParam> listArchiveParam(String archiveId);

    /**
     * 修改档案纬度属性信息
     * @param infobarParam
     * @return
     */
    Integer updateParam(ZsInfobarParam infobarParam);

    /**
     * 排序信息栏纬度属性
     * @param paramId
     * @param sortType
     * @return
     */
    Integer sortParam(String paramId,String sortType);
}
