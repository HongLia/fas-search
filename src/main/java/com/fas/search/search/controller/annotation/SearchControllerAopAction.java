package com.fas.search.search.controller.annotation;

import com.fas.search.manage.entity.ZsUserRecord;
import com.fas.search.manage.service.ZsUserRecordService;
import com.fas.search.util.common.CreateDataUtil;
import com.fas.search.util.user.UserVOUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @auther wuzy
 * @description aop搜索日志处理类
 * @date 2019/11/20
 */
@Component
@Aspect
public class SearchControllerAopAction {

    //本地日志记录
    private static Logger logger = Logger.getLogger(SearchControllerAopAction.class);

    @Resource
    private ZsUserRecordService zsUserRecordService;

    @Pointcut("@annotation(com.fas.search.search.controller.annotation.SearchControllerLog)")
    private void controllerAspect(){}

    @After("controllerAspect()")
    public void before(JoinPoint pjp){
        try {
            Object[] params = pjp.getArgs();
            MethodSignature ms = (MethodSignature) pjp.getSignature();
            Method method = ms.getMethod();
            SearchControllerLog annotation = method.getAnnotation(SearchControllerLog.class);
            ZsUserRecord zsUserRecord = parseRecord(annotation, params);
            Integer integer = zsUserRecordService.saveRecord(zsUserRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ZsUserRecord parseRecord(SearchControllerLog annotation,Object[] params){
        ZsUserRecord zsUserRecord = new ZsUserRecord();
        zsUserRecord.setUserid(UserVOUtil.getUserID());
        zsUserRecord.setUsername(UserVOUtil.getUserName());
        zsUserRecord.setId(CreateDataUtil.getUUID());
        //数据id
        int dataIdIndex = annotation.dataIdIndex();
        if (dataIdIndex != -1){
            Object dataid = params[dataIdIndex];
            zsUserRecord.setDataid(String.valueOf(dataid));
        }
        //实体di
        int entityIdIndex = annotation.entityIdIndex();
        if (entityIdIndex != -1){
            Object entityId = params[entityIdIndex];
            zsUserRecord.setEntity_id(String.valueOf(entityId));
        }
        //搜索关键字
        int keyIndex = annotation.keyIndex();
        if (keyIndex != -1){
            Object key = params[keyIndex];
            zsUserRecord.setSearchkey(String.valueOf(key));
        }
        //主题id
        int subjectIdIndex = annotation.subjectIdIndex();
        if (subjectIdIndex != -1){
            Object subjectId = params[subjectIdIndex];
            zsUserRecord.setSubject_id(String.valueOf(subjectId));
        }
        //搜索类型
        String type = annotation.type();
        zsUserRecord.setType(type);
        return zsUserRecord;
    }


}
