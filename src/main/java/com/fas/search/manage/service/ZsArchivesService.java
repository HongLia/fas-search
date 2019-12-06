package com.fas.search.manage.service;

import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.entity.ZsArchivesDTO;

import java.util.List;
/**
 * 智能搜索档案纬度service类
 */
public interface ZsArchivesService {

    /**
     * 新增档案纬度
     * @param zsArchives
     * @return
     */
    Integer saveArchive(ZsArchives zsArchives);

    /**
     * 删除档案纬度
     * @param id
     * @return
     */
    Integer removeArchive(String id);

    /**
     * 修改档案纬度
     * @param zsArchives
     * @return
     */
    Integer updateArchive(ZsArchives zsArchives);


    /**
     * 通过主体id获取主体下档案纬度信息
     * @param subjectId
     * @return
     */
    List<ZsArchives> listArchives(String subjectId);


    /**
     * 档案详情页获取主体下 档案纬度相关信息，表格类型则包含字段值
     * @param subjectId
     * @return
     */
    List<ZsArchivesDTO> listDetailArchives(String subjectId);
}
