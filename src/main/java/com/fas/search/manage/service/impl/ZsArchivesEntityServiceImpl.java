package com.fas.search.manage.service.impl;

import com.fas.base.shiro.UserVO;
import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.entity.ZsArchivesEntity;
import com.fas.search.manage.entity.ZsArchivesRecodeParam;
import com.fas.search.manage.entity.ZsArchivesTableParam;
import com.fas.search.manage.exception.ParamUnusualException;
import com.fas.search.manage.exception.SortValueException;
import com.fas.search.manage.mapper.ZsArchivesEntityMapper;
import com.fas.search.manage.mapper.ZsArchivesMapper;
import com.fas.search.manage.mapper.ZsArchivesRecodeParamMapper;
import com.fas.search.manage.mapper.ZsArchivesTableParamMapper;
import com.fas.search.manage.service.ZsArchivesEntityService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索档案纬度实体关联Service实现类
 * @date 2019/8/9
 */
@Service
@Transactional
public class ZsArchivesEntityServiceImpl implements ZsArchivesEntityService {

    @Autowired
    private ZsArchivesEntityMapper zsArchivesEntityMapper;

    @Autowired
    private ZsArchivesMapper zsArchivesMapper;

    @Autowired
    private ZsArchivesTableParamMapper zsArchivesTableParamMapper;

    @Autowired
    private ZsArchivesRecodeParamMapper zsArchivesRecodeParamMapper;

    @Override
    public Integer addArchivesEntity(ZsArchivesEntity archivesEntity) {
        String archives_id = archivesEntity.getArchives_id();
        BeanEntityTransformUtil.initCreateEntity(archivesEntity);

        //查询档案纬度信息
        ZsArchives zsArchives = zsArchivesMapper.selectByPrimaryKey(archives_id);
        if (zsArchives == null){
            throw new ParamUnusualException("传递档案维度id不存在");
        }
        String type = zsArchives.getType();
        if ("2".equals(type)){
            //表格类
            //同步实体属性到表格参数表
            ZsArchivesTableParam param = new ZsArchivesTableParam();
            param.setEntity_id(archivesEntity.getEntity_id());
            param.setSubject_id(zsArchives.getSubject_id());
            param.setArchive_entity_id(archivesEntity.getId());
            param.setUpdator(archivesEntity.getCreator());
            param.setCreator(archivesEntity.getCreator());

            zsArchivesTableParamMapper.initTableParam(param);
        }else{
            //记录列表类
            ZsArchivesRecodeParam param = new ZsArchivesRecodeParam();
            param.setEntity_id(archivesEntity.getEntity_id());
            param.setSubject_id(zsArchives.getSubject_id());
            param.setArchive_entity_id(archivesEntity.getId());
            param.setUpdator(archivesEntity.getCreator());
            param.setCreator(archivesEntity.getCreator());
            param.setArchives_id(archives_id);
            BeanEntityTransformUtil.initCreateEntity(param);
            zsArchivesRecodeParamMapper.insertSelective(param);
        }
        int result = zsArchivesEntityMapper.insertSelective(archivesEntity);
        return result;
    }

    @Override
    public Integer removeArchivesEntity(String id) {
        ZsArchivesEntity archivesEntity = new ZsArchivesEntity();
        //初始化修改人
        BeanEntityTransformUtil.initDeleteEntity(archivesEntity);
        //设置id
        archivesEntity.setId(id);
        //修改
        return zsArchivesEntityMapper.updateByPrimaryKeySelective(archivesEntity);
    }

    @Override
    public List<Map<String, Object>> listTableField(String archiveEntityId) {
        return zsArchivesTableParamMapper.listTableField(archiveEntityId);
    }

    @Override
    public Integer updateTableField(ZsArchivesTableParam zsArchivesTableParam) {
        zsArchivesTableParam.setUpdator(UserVOUtil.getUserID());
        return zsArchivesTableParamMapper.updateSelective(zsArchivesTableParam);
    }

    @Override
    public Integer sortTableField(String fieldId, String sortType) {
        ZsArchivesTableParam tableParam = zsArchivesTableParamMapper.selectByPrimaryKey(fieldId);
        //当前字段的值
        Integer thesort = tableParam.getThesort();
        //需要交换的值
        Integer swapValue = 0;
        //type 0标示上移，1表示下移
        if ("0".equals(sortType)){
            //上移 判断元素是不是在最上层
            if (thesort <= 1)
                throw new SortValueException("当前字段已经在最上层，无法上移");
            swapValue = thesort - 1;
        }else if ("1".equals(sortType)){
            //下移 判断元素是不是在最下层
            Integer maxValue = zsArchivesTableParamMapper.selectMaxSortValue(tableParam.getArchive_entity_id());
            if (thesort == maxValue){
                throw new SortValueException("当前字段已经在最下层，无法下移");
            }
            swapValue = thesort + 1;
        }else {
            throw new ParamUnusualException("排序类型无效，请确认后再进行操作");
        }
        return zsArchivesTableParamMapper.swapSortValue(thesort,swapValue,tableParam.getArchive_entity_id());
    }

    @Override
    public Integer updateRecord(ZsArchivesRecodeParam recodeParam) {
        recodeParam.setUpdator(UserVOUtil.getUserID());
        return zsArchivesRecodeParamMapper.updateByPrimaryKeySelective(recodeParam);
    }


}
