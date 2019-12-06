package com.fas.search.manage.service.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsUserRecord;
import com.fas.search.manage.mapper.ZsUserRecordMapper;
import com.fas.search.manage.service.ZsUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 智能搜索用户行为记录查询Service实现类
 * @date 2019/8/13
 */
@Service
@Transactional
public class ZsUserRecordServiceImpl implements ZsUserRecordService {

    @Autowired
    private ZsUserRecordMapper zsUserRecordMapper;


    @Override
    public Map<String, Object> getUserCensus() {
        return zsUserRecordMapper.getUserCensus();
    }

    @Override
    public List<Map<String, Object>> listUse(ZsUserRecord record, Page page) {
        return zsUserRecordMapper.listUse(record,page);
    }

    @Override
    public Integer countUse(String userName) {
        return zsUserRecordMapper.countUse(userName);
    }

    @Override
    public List<Map<String, Object>> listUser(String userName, Page page) {
        return zsUserRecordMapper.listUser(userName,page);
    }

    @Override
    public Integer countUser(String userName) {
        return zsUserRecordMapper.countUse(userName);
    }

    @Override
    public List<String> latelyRecord(String userid) {
        return zsUserRecordMapper.latelyRecord(userid);
    }

    @Override
    public Integer saveRecord(ZsUserRecord zsUserRecord) {
        return zsUserRecordMapper.insertSelective(zsUserRecord);
    }

    @Override
    public Long countRecord(ZsUserRecord zsUserRecord) {
        return zsUserRecordMapper.countSelected(zsUserRecord);
    }
}
