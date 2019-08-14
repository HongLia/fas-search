package com.fas.search.manage.service;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsSubject;
import java.util.List;
/**
 * 主体相关
 */
public interface ZsSubjectService {
    int removeById(String id);

    int save(ZsSubject record);

    ZsSubject getById(String id);

    int update(ZsSubject record);

    /**
     * 根据条件分页查询主体
     * @param subject
     * @param page
     * @return
     */
    List<ZsSubject> listByCondition( ZsSubject subject, Page page);

    /**
     * 根据条件统计数量
     * @param subject
     * @return
     */
    Integer countByCondition(ZsSubject subject);


    /**
     * 更改上线或者下线
     * @param id
     * @param status 上线状态0，下线状态2
     * @return
     */
    Integer updateLine(String id,String status);
}
