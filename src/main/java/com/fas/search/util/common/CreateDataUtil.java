package com.fas.search.util.common;

import com.fas.search.util.user.UserVOUtil;

import java.util.Map;
import java.util.UUID;

/**
 * 用于生成数据的工具类
 */
public class CreateDataUtil {

    /**
     * 生成UUID ，去除中横线
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 为map类型数据新增 id、创建人、修改人信息
     * @param data
     */
    public static void initCreateData(Map data){
        data.put("id",getUUID());
        data.put("creator", UserVOUtil.getUserID());
        data.put("updator",UserVOUtil.getUserID());
    }
}
