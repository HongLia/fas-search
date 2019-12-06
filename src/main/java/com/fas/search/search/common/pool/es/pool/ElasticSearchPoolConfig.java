package com.fas.search.search.common.pool.es.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @date : 2018/7/20
 * @description
 */
public class ElasticSearchPoolConfig extends GenericObjectPoolConfig {

    //连接时间
    private long connectTimeMillis;
    //集群名称
    private String clusterName;

    Set<HostAndPort> nodes = new HashSet<HostAndPort>();

    public long getConnectTimeMillis() {
        return connectTimeMillis;
    }

    public void setConnectTimeMillis(long connectTimeMillis) {
        this.connectTimeMillis = connectTimeMillis;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Set<HostAndPort> getNodes() {
        return nodes;
    }

    public void setNodes(Set<HostAndPort> nodes) {
        this.nodes = nodes;
    }
}
