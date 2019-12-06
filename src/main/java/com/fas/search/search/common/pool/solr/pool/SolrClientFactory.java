package com.fas.search.search.common.pool.solr.pool;

import com.fas.search.search.common.pool.solr.SolrPropertiesUtil;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @date : 2018/7/20
 * @description
 */
public class SolrClientFactory implements PooledObjectFactory<CloudSolrClient> {


    public SolrClientFactory(){
    }


    /**
     * 产生连接对象
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<CloudSolrClient> makeObject() throws Exception {
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder();
        builder.withZkHost(SolrPropertiesUtil.zkHost);
        CloudSolrClient cloudSolrClient = builder.build();
        cloudSolrClient.setZkClientTimeout(SolrPropertiesUtil.zkClientTimeout);
        cloudSolrClient.setZkConnectTimeout(SolrPropertiesUtil.zkConnectTimeout);
        cloudSolrClient.connect();
        CloudSolrClient server  = cloudSolrClient;
        return new DefaultPooledObject(server);
    }

    /**
     * 销毁连接对象
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<CloudSolrClient> pooledObject) throws Exception {
        CloudSolrClient client = pooledObject.getObject();
        if(client!=null){
            try {
                client.close();
            }catch (Exception e){
                //ignore
                System.out.println("连接处关闭solr异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * 校验方法
     * @param pooledObject
     * @return
     */
    public boolean validateObject(PooledObject<CloudSolrClient> pooledObject) {
        CloudSolrClient client = pooledObject.getObject();
        try {
            if(client != null){
                return true;
            }
        }catch(Exception e){
           e.printStackTrace();
        }
        return false;
    }

    /**
     * 重新激活一个对象
     * @param pooledObject
     * @throws Exception
     */
    @Autowired
    public void activateObject(PooledObject<CloudSolrClient> pooledObject) throws Exception {
        CloudSolrClient client = pooledObject.getObject();
        boolean response = client == null;
    }


    /**
     * 钝化一个对象
     * @param pooledObject
     * @throws Exception
     */
    @Autowired
    public void passivateObject(PooledObject<CloudSolrClient> pooledObject) throws Exception {
        //nothing
    }


}
