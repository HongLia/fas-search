package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsSubjectDics;
import java.util.List;
/**
 * 智能搜索主体信息项Service
 */
public interface ZsSubjectDicService {

    /**
     * 删除信息项信息
     * @param Id
     * @return
     */
    Integer remove(String Id);

    /**
     * 保存信息项目信息
     * @param dic
     * @return
     */
    Integer save(ZsSubjectDics dic);

    /**
     * 根据id查找信息项
     * @param id
     * @return
     */
    ZsSubjectDics getById(String id);

    /**
     * 修改信息项
     * @param subjectDics
     * @return
     */
    Integer updateByPrimaryKeySelective(ZsSubjectDics subjectDics);

    /**
     * 查询全部主题下全部信息项
     * @return
     */
    List<ZsSubjectDics> listBySubjectId(String subjectId);
}
