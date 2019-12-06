package com.fas.search.search.common.pool.es.pool;

import com.fas.search.search.common.pool.es.EsConfigureUtil;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @date : 2018/7/20
 * @description
 */
public class ElasticSearchClientFactory implements PooledObjectFactory<TransportClient> {

    private AtomicReference<Set<HostAndPort>> nodesReference = new AtomicReference<Set<HostAndPort>>();

    private String clusterName;

    public ElasticSearchClientFactory(String clusterName, Set<HostAndPort> clusterNodes){
        this.clusterName = clusterName;
        this.nodesReference.set(clusterNodes);
    }


    /**
     * 产生连接对象
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<TransportClient> makeObject() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", EsConfigureUtil.ES_CLUSTER_NAME)
                .put("client.transport.sniff",true)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(EsConfigureUtil.ADDRESSES);
        return new DefaultPooledObject(client);
    }

    /**
     * 销毁连接对象
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<TransportClient> pooledObject) throws Exception {
        TransportClient client = pooledObject.getObject();
        if(client!=null){
            try {
                client.close();
            }catch (Exception e){
                //ignore
            }
        }
    }

    /**
     * 校验方法
     * @param pooledObject
     * @return
     */
    public boolean validateObject(PooledObject<TransportClient> pooledObject) {
        TransportClient client = pooledObject.getObject();
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
    public void activateObject(PooledObject<TransportClient> pooledObject) throws Exception {
        TransportClient client = pooledObject.getObject();
        boolean response = client == null;
    }


    /**
     * 钝化一个对象
     * @param pooledObject
     * @throws Exception
     */
    @Autowired
    public void passivateObject(PooledObject<TransportClient> pooledObject) throws Exception {
        //nothing
    }


}
