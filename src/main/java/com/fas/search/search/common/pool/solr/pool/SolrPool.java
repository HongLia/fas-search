package com.fas.search.search.common.pool.solr.pool;


import com.fas.search.search.common.pool.Pool;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

/**
 * @date : 2018/7/20
 * @description
 */
public class SolrPool extends Pool<CloudSolrClient> {

    private String clusterName;

    public SolrPool(SolrPoolConfig config){
        super(config, new SolrClientFactory());
    }


}
