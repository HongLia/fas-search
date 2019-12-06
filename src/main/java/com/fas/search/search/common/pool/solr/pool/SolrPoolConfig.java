package com.fas.search.search.common.pool.solr.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @date : 2018/7/20
 * @description
 */
public class SolrPoolConfig extends GenericObjectPoolConfig {

    //连接时间
    private long connectTimeMillis;


    public long getConnectTimeMillis() {
        return connectTimeMillis;
    }

    public void setConnectTimeMillis(long connectTimeMillis) {
        this.connectTimeMillis = connectTimeMillis;
    }




}
