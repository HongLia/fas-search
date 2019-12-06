package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsInfobar;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.mapper.ZsInfobarMapper;
import com.fas.search.manage.service.ZsInfobarService;
import com.fas.search.util.common.BeanEntityTransformUtil;
import com.fas.search.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther wuzy
 * @description 智能搜索信息栏service实现类
 * @date 2019/8/6
 */
@Service
@Transactional
public class ZsInfobarServiceImpl implements ZsInfobarService {

    @Autowired
    private ZsInfobarMapper zsInfobarMapper;

    private Lock lock = new ReentrantLock();

    @Override
    public int saveSelective(ZsInfobar infobar) {

        try {
            lock.lock();
            //查询是否有重复名称
            ZsInfobar queryInfobar = new ZsInfobar();
            queryInfobar.setName(infobar.getName());
            Integer integer = zsInfobarMapper.countBySelective(queryInfobar);
            if (integer > 0){
                throw new NameRepeatException("名称重复，请确认后在添加");
            }
            //初始化创建人、修改人、id等信息
            BeanEntityTransformUtil.initCreateEntity(infobar);
            //保存
            return zsInfobarMapper.insertSelective(infobar);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int removeById(String id) {
        //逻辑删除，创建修改对象
        ZsInfobar infobar = new ZsInfobar();
        //设置修改id
        infobar.setId(id);
        //初始化修改人信息
        BeanEntityTransformUtil.initDeleteEntity(infobar);
        //修改
        return zsInfobarMapper.updateByPrimaryKeySelective(infobar);
    }

    @Override
    public int updateSelective(ZsInfobar infobar) {
        try {
            lock.lock();
            Integer number = zsInfobarMapper.checkUpdateNameRepeat(infobar);
            if (number > 0){
                throw new NameRepeatException(infobar.getName() + "已存在，请确认后再修改！");
            }
            //设置修改人信息
            infobar.setUpdator(UserVOUtil.getUserID());
            //修改
            return zsInfobarMapper.updateByPrimaryKeySelective(infobar);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ZsInfobar> listBySubjectId(String subjectId) {
        //查询
        return zsInfobarMapper.listBySubjectId(subjectId);
    }
}
