package com.fas.search.search.common.pool.solr;

import com.fas.search.search.common.pool.Pool;
import com.fas.search.search.common.pool.solr.pool.SolrPool;
import com.fas.search.search.common.pool.solr.pool.SolrPoolConfig;
import org.apache.solr.client.solrj.impl.CloudSolrClient;


/**
 *版权声明 中科金审（北京）有限公司 版权所有 违者必究
 *<br> Company：中科金审
 *<br> @version 1.0
 *<br> 产生HttpSolrServer
 *
 *
 */
public class SolrConnectionPool {

	public static Pool<CloudSolrClient> solrClientPool  = null;

	static {
        SolrPoolConfig solrPoolConfig = new SolrPoolConfig();
		solrPoolConfig.setConnectTimeMillis(80000);
        //最大连接数
		solrPoolConfig.setMaxTotal(200);
        //最多等待时间
		solrPoolConfig.setMaxWaitMillis(50000);
		solrClientPool = new SolrPool(solrPoolConfig);

	}
	/**
	 * 从连接池获取Es连接
	 * @return
	 */
	public static CloudSolrClient getSolrClient(){
		CloudSolrClient cloudSolrClient = null;
		try{
			cloudSolrClient = solrClientPool.getResource();
		}catch (Exception e){
			if(cloudSolrClient != null){
				solrClientPool.returnBrokenResource(cloudSolrClient);
			}
		}
		return cloudSolrClient;
	}


	/**
	 * 归还es连接
	 * @param solrClient
     */
	public static void  returnsolrClient(CloudSolrClient solrClient){
		try {
			if(solrClient != null){
				solrClientPool.returnResource(solrClient);
            }
		} catch (Exception e) {
			e.printStackTrace();
			solrClientPool.returnBrokenResource(solrClient);
		}
	}

}
