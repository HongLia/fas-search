package com.fas.search.util.common;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @auther wuzy
 * @description list|map转化工具类
 * @date 2019/11/13
 */
public class ListMapTransformUtil {


    public static Map<String,String> listToMap(List<Map<String,String>> datas,String keyName,String valueName){
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> stringStringMap = datas.get(i);
            map.put(stringStringMap.get(keyName),stringStringMap.get(valueName));
        }
        return map;
    }
}
