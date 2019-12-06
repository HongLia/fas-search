package com.fas.search.search.common.pool.es;

import com.fas.base.model.FasConstants;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * ES 配置工具类
 * Created by Administrator on 2019/2/14.
 */

@Configuration
public class EsConfigureUtil {
    //ES集群名称
    public static String ES_CLUSTER_NAME ;
    //ES ip和集群集合
    public static String ES_HOST_CLOUD;
    //单机 host ip
    public static String ES_HOST;
    //单机 port
    public static Integer ES_PORT;
    //集群地址集合
    public static TransportAddress[] ADDRESSES;
    //默认分片数number_of_shards
    public static Integer DEFAULT_NUMBER_OF_SHARDS = 5;
    //默认副本数number_of_replicas
    public static Integer DEFAULT_NUMBER_OF_REPLICAS = 1;
    static{

        try {
            ES_CLUSTER_NAME =  FasConstants.PROPERTIES.PROMAP.get("search.es.cluster.name");
            ES_HOST = FasConstants.PROPERTIES.PROMAP.get("search.es.host");
            ES_HOST_CLOUD = FasConstants.PROPERTIES.PROMAP.get("search.es.hostCloud");
            //判断分片
            if(!StringUtils.isEmpty( FasConstants.PROPERTIES.PROMAP.get("search.es.numberOfShards"))){
                DEFAULT_NUMBER_OF_SHARDS = Integer.parseInt( FasConstants.PROPERTIES.PROMAP.get("search.es.numberOfShards"));
            }
            //判断副本
            if (!StringUtils.isEmpty(FasConstants.PROPERTIES.PROMAP.get("search.es.numberOfReplicas"))){
                DEFAULT_NUMBER_OF_REPLICAS = Integer.parseInt(FasConstants.PROPERTIES.PROMAP.get("search.es.numberOfReplicas"));
            }

            if (StringUtils.isNotEmpty(ES_HOST_CLOUD)){
                String[] hosts = ES_HOST_CLOUD.split(",");
                ADDRESSES = new TransportAddress[hosts.length];
                for (int i = 0; i < hosts.length; i++) {
                    String[] hostAndPort = hosts[i].split(":");
                    if (hostAndPort.length<2){
                        continue;
                    }
                    ADDRESSES[i] = new TransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1]));
                }
            }

            ES_PORT = Integer.parseInt(FasConstants.PROPERTIES.PROMAP.get("search.es.port"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    @PostConstruct
    void init(){
        //java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
        //避免netty冲突
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }
}
