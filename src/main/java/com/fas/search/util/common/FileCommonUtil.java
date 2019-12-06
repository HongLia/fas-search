package com.fas.search.util.common;

import java.util.Properties;

/**
 * @auther wuzy
 * @description 文件操作相关工具类
 * @date 2019/8/14
 */
public class FileCommonUtil {

    /**
     * 获取当前工作路径
     * @return
     */
    public static String getWorkPath(){
        String pathname = Thread.currentThread().getContextClassLoader().getResource("").toString();
        Properties properties = System.getProperties();
        String osName = properties.getProperty("os.name");
        pathname = pathname.replace("file:", "");
        if(properties != null && osName.toLowerCase().indexOf("windows") > -1){
            pathname = pathname.substring(1);
        }
        return pathname;
    }
}
