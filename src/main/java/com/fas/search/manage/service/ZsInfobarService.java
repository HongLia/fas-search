package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsInfobar;

import java.util.List;

/**
 * 智能搜索信息栏service
 */
public interface ZsInfobarService {

    /**
     * 保存信息栏
     * @param infobar
     * @return
     */
    int saveSelective(ZsInfobar infobar);

    /**
     * 根据id删除信息栏
     * @param id
     * @return
     */
    int removeById(String id);

    /**
     * 修改信息栏信息
     * @param infobar
     * @return
     */
    int updateSelective(ZsInfobar infobar);

    /**
     * 根据主体id获取主体下所有信息栏信息
     * @param subjectId
     * @return
     */
    List<ZsInfobar> listBySubjectId(String subjectId);
}
