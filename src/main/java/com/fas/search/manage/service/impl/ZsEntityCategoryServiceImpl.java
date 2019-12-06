package com.fas.search.manage.service.impl;

import com.fas.base.util.MenuUtil;
import com.fas.search.manage.entity.ZsEntityCategory;
import com.fas.search.manage.exception.NameRepeatException;
import com.fas.search.manage.mapper.ZsEntityCategoryMapper;
import com.fas.search.manage.service.ZsEntityCategoryService;
import com.fas.search.util.common.BeanEntityTransformUtil;
import com.fas.search.util.user.UserVOUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class ZsEntityCategoryServiceImpl implements ZsEntityCategoryService {

    @Autowired
    private ZsEntityCategoryMapper zsEntityCategoryMapper;

    private Lock lock = new ReentrantLock();

    @Override
    public int removeById(String id) {
        //创建实体分类bean
        ZsEntityCategory category = new ZsEntityCategory(id, null,"1",null,UserVOUtil.getUserID());
        //逻辑删除，更改状态
        return zsEntityCategoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public int save(ZsEntityCategory record) {
        try {
            lock.lock();
            //检查名称是否已经存在
            ZsEntityCategory tempCategory = new ZsEntityCategory();
            tempCategory.setName(record.getName());
            List<ZsEntityCategory> zsEntityCategories = zsEntityCategoryMapper.selectBySelective(tempCategory);
            if (CollectionUtils.isEmpty(zsEntityCategories)) {
                throw new NameRepeatException(record.getName() + "已经存在，请确认以后再添加！");
            }
            //添加创建人、修改人、id信息
            BeanEntityTransformUtil.initCreateEntity(record);
            //新增实体分类
            return zsEntityCategoryMapper.insertSelective(record);
        }finally {
            lock.unlock();
        }
    }



    @Override
    public ZsEntityCategory getById(String id) {
        return zsEntityCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(ZsEntityCategory record) {
        record.setUpdator(UserVOUtil.getUserID());
        return zsEntityCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Map> listCategoryTree() {
        List<Map> lists = zsEntityCategoryMapper.selectCategoryTree();
        List<Map> tree = MenuUtil.treeMenuList(lists , "0", "pid", "id");
        return tree;
    }
}
