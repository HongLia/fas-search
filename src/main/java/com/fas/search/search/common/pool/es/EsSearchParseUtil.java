package com.fas.search.search.common.pool.es;

import java.util.Set;

/**
 * Created by Administrator on 2019/2/15.
 */
public class EsSearchParseUtil {


    /**
     * 拼接查询文件中的关键字
     * @param keys
     * @param searchClass
     * @return
     */
    public static String getFileKey(Set<String> keys, String searchClass){
        StringBuilder sb = new StringBuilder();

        for (String key: keys) {
            sb.append(key).append(" ");
        }

        return sb.toString();
    }
}
