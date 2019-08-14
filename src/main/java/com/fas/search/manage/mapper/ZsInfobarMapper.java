package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsInfobar;

import java.util.List;

public interface ZsInfobarMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ZsInfobar record);

    ZsInfobar selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsInfobar record);

    /**
     * 根据主体id，获取主体下所有信息栏的id
     * @param subjectId
     * @return
     */
    List<ZsInfobar> listBySubjectId(String subjectId);


    /**
     * 根据条件获取信息栏信息
     * @param infobar
     * @return
     */
    Integer countBySelective(ZsInfobar infobar);


    /**
     * 修改信息栏时 检查名称是否重复
     * @param infobar
     * @return
     */
    Integer checkUpdateNameRepeat(ZsInfobar infobar);


}