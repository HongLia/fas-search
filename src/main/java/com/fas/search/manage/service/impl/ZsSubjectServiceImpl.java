package com.fas.search.manage.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.mapper.ZsSubjectMapper;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.util.common.BeanEntityTransformUtil;
import com.fas.search.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther wuzy
 * @description 智能搜索主体相关信息Service方法
 * @date 2019/7/10
 */
@Service
@Transactional
public class ZsSubjectServiceImpl implements ZsSubjectService {

    @Autowired
    private ZsSubjectMapper zsSubjectMapper;

    private Lock lock = new ReentrantLock();
    @Override
    public int removeById(String id) {
        ZsSubject subject = new ZsSubject();
        subject.setId(id);
        BeanEntityTransformUtil.initDeleteEntity(subject);
        return zsSubjectMapper.updateByPrimaryKeySelective(subject);
    }

    @Override
    public int save(ZsSubject record) {
        try {
            lock.lock();
            //查看名称是否存在
            Integer num = zsSubjectMapper.countBySelective(new ZsSubject(record.getName()));
            if (num > 0)
                throw new NameRepeatException(record.getName()+"已经存在，请确认后在新增");
            //实体的时候新增上创建人，修改人信息 ,id
            BeanEntityTransformUtil.initCreateEntity(record);
            return  zsSubjectMapper.insertSelective(record);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ZsSubject getById(String id) {
        return zsSubjectMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(ZsSubject record) {
        try {
            lock.lock();
            //检查名称是否已经存在
            Integer num = zsSubjectMapper.checkUpdateNameRepeat(record);
            if (num > 0){
                throw new NameRepeatException(record.getName() + "已经存在，请确认以后再修改");
            }
            //设置修改人
            record.setUpdator(UserVOUtil.getUserID());
            //修改
            return zsSubjectMapper.updateByPrimaryKeySelective(record);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ZsSubject> listByCondition(ZsSubject subject, Page page) {
        return zsSubjectMapper.selectBySelective(subject,page);
    }

    @Override
    public Integer countByCondition(ZsSubject subject) {
        return zsSubjectMapper.countBySelective(subject);
    }

    @Override
    public Integer updateLine(String id, String status) {
        ZsSubject subject = new ZsSubject();
        //设置id
        subject.setId(id);
        //设置修改人
        subject.setUpdator(UserVOUtil.getUserID());
        //设置状态
        subject.setEnable(status);
        if ("0".equals(status)){
            //设置上线时间
            subject.setOnline_time(new Date());
        }else{
            //设置下线时间
            subject.setOffline_time(new Date());
        }
        return zsSubjectMapper.updateByPrimaryKeySelective(subject);
    }
}
