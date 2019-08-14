package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsArchives;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.mapper.ZsArchivesMapper;
import com.fas.search.manage.service.ZsArchivesService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther wuzy
 * @description 智能搜索档案纬度管理Service实现类
 * @date 2019/8/8
 */
@Service
@Transactional
public class ZsArchivesServiceImpl implements ZsArchivesService {

    @Autowired
    private ZsArchivesMapper zsArchivesMapper;

    private Lock lock = new ReentrantLock();


    @Override
    public Integer saveArchive(ZsArchives zsArchives) {

        try {
            lock.lock();
            //检查名称是否存在
            Integer countNameNumber = zsArchivesMapper.countNameNumber(zsArchives.getName(),null);
            if (countNameNumber > 0){
                throw new NameRepeatException(zsArchives.getName() + "已经存在，请确认以后再添加");
            }
            //初始化创建人、id、修改人等信息
            BeanEntityTransformUtil.initCreateEntity(zsArchives);
            return zsArchivesMapper.insertSelective(zsArchives);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer removeArchive(String id) {
        //创建操作对象的实体
        ZsArchives zsArchives = new ZsArchives();
        //设置id
        zsArchives.setId(id);
        //设置修改人
        BeanEntityTransformUtil.initDeleteEntity(zsArchives);
        //逻辑删除
        return zsArchivesMapper.updateByPrimaryKeySelective(zsArchives);
    }

    @Override
    public Integer updateArchive(ZsArchives zsArchives) {
        try {
            lock.lock();
            //检查名称是否已经存在
            Integer countNameNumber = zsArchivesMapper.countNameNumber(zsArchives.getName(), zsArchives.getId());
            if (countNameNumber > 0){
                throw new NameRepeatException(zsArchives.getName() + "名称已经存在，请确认后再修改");
            }
            //设置修改人
            zsArchives.setUpdator(UserVOUtil.getUserID());
            //修改
            return zsArchivesMapper.updateByPrimaryKeySelective(zsArchives);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ZsArchives> listArchives(String subjectId) {
        return zsArchivesMapper.listArchives(subjectId);
    }
}
