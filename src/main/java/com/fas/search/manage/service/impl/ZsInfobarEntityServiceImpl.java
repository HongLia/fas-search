package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsInfobarEntity;
import com.fas.search.manage.entity.ZsInfobarEntityDTO;
import com.fas.search.manage.entity.ZsInfobarParam;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.exception.SortValueException;
import com.fas.search.manage.mapper.ZsInfobarEntityMapper;
import com.fas.search.manage.mapper.ZsInfobarParamMapper;
import com.fas.search.manage.service.ZsInfobarEntityService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther wuzy
 * @description 智能搜索信息栏纬度信息Service实现类
 * @date 2019/8/6
 */
@Service
@Transactional
public class ZsInfobarEntityServiceImpl implements ZsInfobarEntityService {

    @Autowired
    private ZsInfobarEntityMapper zsInfobarEntityMapper;

    @Autowired
    private ZsInfobarParamMapper zsInfobarParamMapper;


    private Lock lock = new ReentrantLock();


    @Override
    public int save(ZsInfobarEntity zsInfobarEntity) {
        try {
            lock.lock();
            //检查名称是否存在
            Integer integer = zsInfobarEntityMapper.checkNum(zsInfobarEntity);
            if (integer > 0){
                throw new NameRepeatException(zsInfobarEntity.getName() + "名称已存在，请确认后再添加");
            }
            //初始化实体创建人、修改人信息
            BeanEntityTransformUtil.initCreateEntity(zsInfobarEntity);
            int i = zsInfobarEntityMapper.insertSelective(zsInfobarEntity);
            // 同步实体字段
            ZsInfobarParam zsInfobarParam = new ZsInfobarParam();
            BeanEntityTransformUtil.initCreateEntity(zsInfobarParam);
            zsInfobarParam.setInfobar_entity_id(zsInfobarEntity.getId());
            zsInfobarParamMapper.initParam(zsInfobarParam,zsInfobarEntity.getEntity_id());
            return i;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int update(ZsInfobarEntity zsInfobarEntity) {

        try {
            lock.lock();
            Integer integer = zsInfobarEntityMapper.checkNum(zsInfobarEntity);
            if (integer > 0){
                throw new NameRepeatException(zsInfobarEntity.getName() + "名称已存在，请确认后再修改");
            }
            zsInfobarEntity.setUpdator(UserVOUtil.getUserID());
            return zsInfobarEntityMapper.updateByPrimaryKeySelective(zsInfobarEntity);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int delete(String id) {
        ZsInfobarEntity infobarEntity = new ZsInfobarEntity();
        infobarEntity.setId(id);
        BeanEntityTransformUtil.initDeleteEntity(infobarEntity);
        return zsInfobarEntityMapper.updateByPrimaryKeySelective(infobarEntity);
    }

    @Override
    public List<ZsInfobarEntityDTO> listArchiveInfo(String infobarId) {
        return zsInfobarEntityMapper.listArchiveInfo(infobarId);
    }

    @Override
    public List<ZsInfobarParam> listArchiveParam(String archiveId) {
        return zsInfobarParamMapper.listByArchive(archiveId);
    }

    @Override
    public Integer updateParam(ZsInfobarParam infobarParam) {
        infobarParam.setUpdator(UserVOUtil.getUserID());
        return zsInfobarParamMapper.updateByPrimaryKeySelective(infobarParam);
    }

    @Override
    public Integer sortParam(String paramId, String sortType) {
        //找到当前数据
        ZsInfobarParam zsInfobarParam = zsInfobarParamMapper.selectByPrimaryKey(paramId);
        //当前字段的值
        Integer thesort = zsInfobarParam.getThesort();
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
            Integer maxValue = zsInfobarParamMapper.selectMaxSortValue(zsInfobarParam.getInfobar_entity_id());
            if (thesort == maxValue){
                throw new SortValueException("当前字段已经在最下层，无法下移");
            }
            swapValue = thesort + 1;
        }
        //交换排序
        return zsInfobarParamMapper.swapSortValue(thesort, swapValue, zsInfobarParam.getInfobar_entity_id());
    }
}
