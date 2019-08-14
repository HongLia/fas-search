package com.fas.search.manage.service.impl;

import com.fas.base.model.Page;
import com.fas.base.shiro.UserVO;
import com.fas.search.manage.entity.ZsEtlTask;
import com.fas.search.manage.mapper.ZsEtlTaskMapper;
import com.fas.search.manage.service.ZsEtlTaskService;
import com.fas.search.manage.util.common.BeanEntityTransformUtil;
import com.fas.search.manage.util.user.UserVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ZsEtlTaskServiceImpl implements ZsEtlTaskService {

    @Autowired
    private ZsEtlTaskMapper zsEtlTaskMapper;


    @Override
    public int removeByPrimaryKey(String id) {
        /*ZsEtlTask etlTask = new ZsEtlTask();
        etlTask.setId(id);
        //添加修改人、状态信息
        BeanEntityTransformUtil.initDeleteEntity(etlTask);*/

        return zsEtlTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int saveSelective(ZsEtlTask record) {
        BeanEntityTransformUtil.initCreateEntity(record);
        int i = zsEtlTaskMapper.insertSelective(record);
        //TODO  调用调度中心接口新增任务
        return i;
    }

    @Override
    public ZsEtlTask getByPrimaryKey(String id) {
        ZsEtlTask etlTask = zsEtlTaskMapper.selectByPrimaryKey(id);
        //TODO 通过lts调度中心查询任务的定时、抽取的信息
        return etlTask;
    }

    @Override
    public int updateByPrimaryKeySelective(ZsEtlTask record) {
        record.setUpdator(UserVOUtil.getUserID());
        return zsEtlTaskMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<ZsEtlTask> listByPage(ZsEtlTask etlTask, Page page) {
        return zsEtlTaskMapper.listByPage(etlTask,page);
    }

    @Override
    public Integer countNum(ZsEtlTask etlTask) {
        return zsEtlTaskMapper.countNum(etlTask);
    }
}
