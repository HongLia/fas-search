package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsSubjectDics;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.exception.SearchPkRepeatException;
import com.fas.search.manage.mapper.ZsSubjectDicsMapper;
import com.fas.search.manage.service.ZsSubjectDicService;
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
 * @description 智能搜索信息项Service实现类
 * @date 2019/7/22
 */
@Service
@Transactional
public class ZsSubjectDicServiceImpl implements ZsSubjectDicService {

    @Autowired
    private ZsSubjectDicsMapper zsSubjectDicsMapper;

    private Lock lock = new ReentrantLock();

    @Override
    public Integer remove(String id) {
        ZsSubjectDics zsSubjectDics = new ZsSubjectDics();
        zsSubjectDics.setId(id);
        BeanEntityTransformUtil.initDeleteEntity(zsSubjectDics);
        return zsSubjectDicsMapper.updateByPrimaryKeySelective(zsSubjectDics);
    }

    @Override
    public Integer save(ZsSubjectDics dic) {
        try {
            lock.lock();
            //校验信息是否已经存在
            checkNum(dic);
            // 经过验证  初始化id、创建人、修改人
            BeanEntityTransformUtil.initCreateEntity(dic);
            return zsSubjectDicsMapper.insertSelective(dic);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ZsSubjectDics getById(String id) {
        return zsSubjectDicsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(ZsSubjectDics subjectDics) {
        try {
            lock.lock();
            //校验信息
            checkNum(subjectDics);
            //设置修改人
            subjectDics.setUpdator(UserVOUtil.getUserID());
            return zsSubjectDicsMapper.updateByPrimaryKeySelective(subjectDics);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ZsSubjectDics> listBySubjectId(String subjectId) {
        return zsSubjectDicsMapper.listBySubjectId(subjectId);
    }


    /**
     * 校验信息是否重复
     * @param dic
     */
    public void checkNum(ZsSubjectDics dic){
        //查询中文名、英文名、查询主键是否已经存在
        Map<String, Long> cs = zsSubjectDicsMapper.countCheckExists(dic);
        Long cnameCount = cs.get("cnameCount");
        //判断中文名是否已经存在
        if ( cnameCount != null && cnameCount > 0)
            throw new NameRepeatException("中文名已经存在");
        //判断英文名是否已经存在
        Long enameCount = cs.get("enameCount");
        if ( enameCount != null && enameCount > 0)
            throw new NameRepeatException("英文名已经存在");
        //判断查询主键是否已经设置过
        Long searchPkCount = cs.get("searchPkCount");
        if ( searchPkCount != null && searchPkCount > 0)
            throw new SearchPkRepeatException("查询主键已经设置");
    }
}
