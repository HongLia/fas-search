package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobarEntity;
import com.fas.search.manage.entity.ZsInfobarEntityDTO;

import java.util.List;

public interface ZsInfobarEntityMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsInfobarEntity record);

    ZsInfobarEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobarEntity record);

    /**
     * 根据信息栏id，查询信息栏下纬度信息，以及纬度属性信息
     * @param infobarId
     * @return
     */
    List<ZsInfobarEntityDTO> listArchiveInfo(String infobarId);

    /**
     * 校验 信息栏名字 数量，从而判断数据是否重复
     * @param zsInfobarEntity
     * @return
     */
    Integer checkNum(ZsInfobarEntity zsInfobarEntity);


}