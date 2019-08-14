package com.fas.search.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.fas.metadata.service.MetaDataInterfaceService;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.mapper.ZsEntityFieldMapper;
import com.fas.search.manage.mapper.ZsEntityMapper;
import com.fas.search.manage.service.ZsEntityService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.common.CreateDataUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ZsEntityServiceImpl implements ZsEntityService {

    @Autowired
    private ZsEntityMapper zsEntityMapper;

    @Autowired
    private MetaDataInterfaceService metaDataInterfaceService;

    @Autowired
    private ZsEntityFieldMapper zsEntityFieldMapper;


    @Override
    public int removeById(String id) {
        //创建实体分类bean
        ZsEntity entity = new ZsEntity();
        entity.setId(id);
        //添加修改人、状态信息
        BeanEntityTransformUtil.initDeleteEntity(entity);
        //逻辑删除，更改状态
        return zsEntityMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int save(ZsEntity record) {
        BeanEntityTransformUtil.initCreateEntity(record);
        //todo 以后需要在搜索引擎中添加Collection
        int i = zsEntityMapper.insertSelective(record);
        initEntityField(record);
        return i;
    }

    @Override
    public ZsEntity getById(String id) {
        return zsEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(ZsEntity record) {
        record.setUpdator(UserVOUtil.getUserID());
        return zsEntityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Map> quote(String id) {
        return zsEntityMapper.quote(id);
    }

    @Override
    public int initEntityField(ZsEntity entity) {
        String colums = metaDataInterfaceService.getColumn(entity.getMetadata_id());
        Map map = JSON.parseObject(colums, Map.class);
        if ("200".equals(map.get("code"))){
            //解析数据
            List<Map> data = (List<Map>) map.get("data");
            if (CollectionUtils.isEmpty(data)){
                return 0;
            }
            for (int i = 0; i < data.size(); i++) {
                Map col = data.get(i);
                CreateDataUtil.initCreateData(col);
                col.put("ename", col.get("name"));
                col.put("cname", col.get("note"));
                col.put("tableename", entity.getTablename());
                col.put("fieldename", col.get("name"));
                col.put("entity_id", entity.getId());
                col.put("field_type", col.get("datatype"));
                zsEntityFieldMapper.insertSelectiveByMap(col);
            }
           //return zsEntityFieldMapper.insertBatch(data);
        }

        return 0;
    }
}
