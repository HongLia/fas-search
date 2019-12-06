package com.fas.search.search.common.pool.es;

import com.fas.search.search.common.pool.Pool;
import com.fas.search.search.common.pool.es.pool.ElasticSearchPool;
import com.fas.search.search.common.pool.es.pool.ElasticSearchPoolConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;


/**
 *版权声明 中科金审（北京）有限公司 版权所有 违者必究
 *<br> Company：中科金审
 *<br> @version 1.0
 *<br> 产生es连接
 *
 *
 */
public class ElasticSearchConnectionPool {

	public static Pool<TransportClient> esPool  = null;

	static {
        ElasticSearchPoolConfig elasticSearchPoolConfig = new ElasticSearchPoolConfig();
        elasticSearchPoolConfig.setConnectTimeMillis(800000);
        //最大连接数
        elasticSearchPoolConfig.setMaxTotal(50);
        //最多等待时间
        elasticSearchPoolConfig.setMaxWaitMillis(50000);
        esPool = new ElasticSearchPool(elasticSearchPoolConfig);

	}
	/**
	 * 从连接池获取Es连接
	 * @return
	 */
	public static TransportClient getEsClient(){
		TransportClient transportClient = null;
		try{
			transportClient = esPool.getResource();
		}catch (Exception e){
			e.printStackTrace();
			if(transportClient != null){
				esPool.returnBrokenResource(transportClient);
			}
		}
		return transportClient;
	}


	/**
	 * 归还es连接
	 * @param esClient
     */
	public static void  returnEsClient(TransportClient esClient){
		try {
			if(esClient != null){
				esPool.returnResource(esClient);
            }
		} catch (Exception e) {
			e.printStackTrace();
			esPool.returnBrokenResource(esClient);
		}
	}

}
