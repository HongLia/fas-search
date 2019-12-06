package com.fas.search.manage.mapper;

import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.entity.ZsArchivesDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface ZsArchivesMapper {

    int deleteByPrimaryKey(String id);

    int insertSelective(ZsArchives record);

    ZsArchives selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ZsArchives record);

    /**
     * 根据名称查询纬度信息，且状态为正常的
     * @param name
     * @param id
     * @return
     */
    Integer countNameNumber(@Param("name") String name, @Param("id") String id);


    /**
     * 获取主体下 档案纬度相关信息
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