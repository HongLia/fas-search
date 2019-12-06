package com.fas.search.search.common.pool.es.pool;


import com.fas.search.search.common.pool.Pool;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Set;

/**
 * @date : 2018/7/20
 * @description
 */
public class ElasticSearchPool extends Pool<TransportClient> {

    private String clusterName;
    private Set<HostAndPort> clusterNodes;

    public ElasticSearchPool(ElasticSearchPoolConfig config){
        super(config, new ElasticSearchClientFactory(config.getClusterName(), config.getNodes()));
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Set<HostAndPort> getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(Set<HostAndPort> clusterNodes) {
        this.clusterNodes = clusterNodes;
    }



}
