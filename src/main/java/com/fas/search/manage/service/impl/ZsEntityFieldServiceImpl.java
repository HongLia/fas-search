package com.fas.search.manage.service.impl;

import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.manage.mapper.ZsEntityFieldMapper;
import com.fas.search.manage.service.ZsEntityFieldService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ZsEntityFieldServiceImpl implements ZsEntityFieldService {

    @Autowired
    private ZsEntityFieldMapper zsEntityFieldMapper;

    @Override
    public int removeById(String id) {
        ZsEntityField zsEntityField = new ZsEntityField();
        zsEntityField.setId(id);
        BeanEntityTransformUtil.initDeleteEntity(zsEntityField);
        return zsEntityFieldMapper.updateByPrimaryKeySelective(zsEntityField);
    }

    @Override
    public int save(ZsEntityField record) {
        //添加初始化属性 id、创建人 、修改人
        BeanEntityTransformUtil.initCreateEntity(record);
        return zsEntityFieldMapper.insertSelective(record);
    }

    @Override
    public ZsEntityField getById(String id) {
        return zsEntityFieldMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ZsEntityField record) {
        record.setUpdator(UserVOUtil.getUserID());
        return zsEntityFieldMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<ZsEntityField> listByEntityId(String entityId) {
        return zsEntityFieldMapper.selectByEntityId(entityId);
    }
}
