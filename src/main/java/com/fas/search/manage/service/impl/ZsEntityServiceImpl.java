package com.fas.search.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.fas.metadata.service.MetaDataInterfaceService;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.mapper.ZsEntityFieldMapper;
import com.fas.search.manage.mapper.ZsEntityMapper;
import com.fas.search.manage.service.ZsEntityService;
import com.fas.search.search.bo.EngineInfoBO;
import com.fas.search.search.common.pool.es.EsConfigureUtil;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.engine.impl.SearchEngineElasticsearchServiceImpl;
import com.fas.search.search.exception.CreateCollectionException;
import com.fas.search.util.common.BeanEntityTransformUtil;
import com.fas.search.util.common.CreateDataUtil;
import com.fas.search.util.user.UserVOUtil;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private SearchEngineService searchEngineService;


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
        //初始化创建人信息
        BeanEntityTransformUtil.initCreateEntity(record);
        //设置集合名称
        if (StringUtils.isEmpty(record.getCollection_name())){
            record.setCollection_name(record.getTablename() + "_"+ record.getId());
        }
        int i = zsEntityMapper.insertSelective(record);
        //同步实体字段
        initEntityField(record);
        //创建 搜索引擎 collection
        int collectoin = searchEngineService.createCollectoin(record.getCollection_name());
        if (collectoin > 0){
            throw new CreateCollectionException("创建Collection异常！");
        }
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
