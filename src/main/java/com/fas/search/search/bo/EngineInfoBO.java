package com.fas.search.search.bo;


import java.util.Map;

/**
 * Created by Administrator on 2019/4/24.
 * 创建索引Document 参数封装的BO
 */
public class EngineInfoBO<T> {

    //访问 collection  名称
    private String collectionName;
    //搜索引擎连接  预留属性
    private T client;
    //分片数
    private Integer shardNum;
    //副本数
    private Integer replicaNum;


    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    public Integer getShardNum() {
        return shardNum;
    }

    public EngineInfoBO setShardNum(Integer shardNum) {
        this.shardNum = shardNum;
        return this;
    }

    public Integer getReplicaNum() {
        return replicaNum;
    }

    public EngineInfoBO setReplicaNum(Integer replicaNum) {
        this.replicaNum = replicaNum;
        return this;
    }

    public EngineInfoBO() {
    }
}

