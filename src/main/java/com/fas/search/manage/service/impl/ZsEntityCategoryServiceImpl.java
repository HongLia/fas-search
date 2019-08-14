package com.fas.search.manage.service.impl;

import com.fas.base.util.MenuUtil;
import com.fas.search.manage.entity.ZsEntityCategory;
import com.fas.search.manage.mapper.ZsEntityCategoryMapper;
import com.fas.search.manage.service.ZsEntityCategoryService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ZsEntityCategoryServiceImpl implements ZsEntityCategoryService {

    @Autowired
    private ZsEntityCategoryMapper zsEntityCategoryMapper;

    @Override
    public int removeById(String id) {
        //创建实体分类bean
        ZsEntityCategory category = new ZsEntityCategory(id, UserVOUtil.getUserID(),"1",null);
        //逻辑删除，更改状态
        return zsEntityCategoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public int save(ZsEntityCategory record) {
        //添加创建人、修改人、id信息
        BeanEntityTransformUtil.initCreateEntity(record);
        //新增实体分类
        return zsEntityCategoryMapper.insertSelective(record);
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
