package com.fas.search.search.engine.impl;

import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsEntityField;
import com.fas.search.search.bo.SearchConditionBO;
import com.fas.search.search.bo.SearchFilterBO;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.bo.EngineInfoBO;
import com.fas.search.search.common.pool.es.ElasticSearchConnectionPool;
import com.fas.search.search.common.pool.es.EsConfigureUtil;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.search.searchenum.SearchClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @auther wuzy
 * @description 搜索引擎查询接口，elasticsearch实现类
 * @date 2019/9/11
 */
public class SearchEngineElasticsearchServiceImpl implements SearchEngineService<TransportClient> {


    @Override
    public int createCollectoin(String collectionName) {
        TransportClient client = null;
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("settings")
                    .field("number_of_shards",EsConfigureUtil.DEFAULT_NUMBER_OF_SHARDS)
                    .field("number_of_replicas",EsConfigureUtil.DEFAULT_NUMBER_OF_REPLICAS)
                    .endObject()
                    .startObject("mappings")
                    .startObject(collectionName)
                    .startArray("dynamic_templates")
                    .startObject()
                        .startObject("text_ik")
                            .field("match", "IK_*")
                            .field("match_mapping_type", "*")
                                .startObject("mapping")
                                    .field("type", "text")
                                    .field("analyzer","ik_max_word")
                                    .field("search_analyzer","ik_smart")
                                    .field("copy_to","all_IK")
                                .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                        .startObject("text_int")
                            .field("match", "INT_*")
                            //.field("match_mapping_type", "integer")
                            .startObject("mapping")
                                .field("type", "integer")
                            .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                        .startObject("text_long")
                            .field("match", "LONG_*")
                            //.field("match_mapping_type", "long")
                            .startObject("mapping")
                                .field("type", "long")
                            .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                        .startObject("text_double")
                            .field("match", "DOUBLE_*")
                            //.field("match_mapping_type", "double")
                            .startObject("mapping")
                                .field("type", "double")
                            .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                        .startObject("text_float")
                            .field("match", "FLOAT_*")
                            //.field("match_mapping_type", "float")
                            .startObject("mapping")
                                .field("type", "float")
                            .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                        .startObject("text_date")
                            .field("match", "DATE_*")
                            //.field("match_mapping_type", "date")
                            .startObject("mapping")
                                .field("type", "date")
                                .field("format","yyyy-MM-dd HH:mm:ss.SSS")
                            .endObject()
                        .endObject()
                    .endObject()


                    .startObject()
                        .startObject("text_bool")
                            .field("match", "BOOL_*")
                            //.field("match_mapping_type", "boolean")
                            .startObject("mapping")
                                .field("type", "boolean")
                            .endObject()
                        .endObject()
                    .endObject()

                    .startObject()
                    .startObject("text_str")
                    .field("match", "STR_*")
                    .field("match_mapping_type", "*")
                    .startObject("mapping")
                    .field("type", "keyword")
                    .field("copy_to","all_STR")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endArray()
                    .endObject()
                    .endObject()
                    .startObject("properties")
                    .startObject("all_IK")
                    .field("type","text")
                    .field("analyzer","ik_max_word")
                    .field("search_analyzer","ik_smart")
                    .endObject()
                   /* .startObject("all_PY")
                    .field("type","text")
                    .field("analyzer","pinyin")
                    .field("search_analyzer","pinyin")
                    .endObject()*/
                    .startObject("all_STR")
                    .field("type","keyword")
                    .endObject()
                    .startObject("entity_id")
                    .field("type","keyword")
                    .field("store","true")
                    .field("index","false")
                    .endObject()
                    .endObject()
                    .endObject();


            client = createTransportClient();

            CreateIndexRequestBuilder cirb = client.admin().indices()
                    .prepareCreate(collectionName)
                    .setSource(mapping);

            CreateIndexResponse response = cirb.execute().actionGet();
            if(response.isAcknowledged()){
                System.out.println("创建" + collectionName + "成功");
                return 0;
            }else{
                System.out.println("创建" + collectionName + "失败");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (client != null){
                client.close();
            }
        }
        return 1;
    }


    @Override
    public int deleteCollection(String collectionName) {
        //创建连接
        TransportClient client = null;
        try {
            client = createTransportClient();
            client.admin().indices().prepareDelete(collectionName).execute().actionGet();
            System.out.println("删除成功："+collectionName);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }finally {
            if (client != null){
                client.close();
            }
        }
        //删除
        return  0;
    }

    @Override
    public Object writeDocument(EngineInfoBO<TransportClient> wdbo, List<Map<String, Object>> datas) {
 /*       TransportClient client = wdbo.getClient();
        String collectionName = wdbo.getCollectionName();

        TransportClient esClient = null;
        if(client == null){
            esClient = ElasticSearchConnectionPool.getEsClient();
            wdbo.setClient(esClient);
        }*/
        TransportClient esClient = null;
        String collectionName = wdbo.getCollectionName();

        try {
            esClient = ElasticSearchConnectionPool.getEsClient();
            //200
            BulkRequestBuilder bulkRequestBuilder = null;
            bulkRequestBuilder = esClient.prepareBulk();

            for (int i = 0; i < datas.size(); i++) {

                XContentBuilder onceEsData = XContentFactory.jsonBuilder().startObject();
                //一条数据
                Map<String, Object> data = datas.get(i);
                Set<String> keys = data.keySet();
                //设置主键为为id值
                IndexRequestBuilder indexRequestBuilder = esClient.prepareIndex(collectionName, collectionName, String.valueOf(data.get("id")));
                for (String key:keys) {
                    onceEsData.field(key,data.get(key));
                }
                onceEsData.endObject();
                indexRequestBuilder.setSource(onceEsData);
                bulkRequestBuilder.add(indexRequestBuilder);
            }
            if (bulkRequestBuilder.numberOfActions() > 0) {
                //6, 添加索引
                BulkResponse bulkItemResponses = bulkRequestBuilder.get();

                if (bulkItemResponses.hasFailures()) {
                    System.out.println(bulkItemResponses.buildFailureMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据抽取异常");
        }finally {
            ElasticSearchConnectionPool.returnEsClient(esClient);
        }

        return wdbo;
    }

    @Override
    public void clearnCollectionData(EngineInfoBO<TransportClient> wdBo) {
        TransportClient client = null;
        try {
            //创建新的连接，不从连接池取
            client = createTransportClient();
            BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).source(wdBo.getCollectionName()).filter(QueryBuilders.matchAllQuery()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (client != null){
                client.close();
            }
        }
    }

    @Override
    public void releaseClient(EngineInfoBO<TransportClient> wdbo) {
        TransportClient client = wdbo.getClient();
        if (client != null){
            client.close();
        }
    }

    @Override
    public Long countCollecitonNumber(String collectionName) {
        long totalHits = 0L;
        TransportClient client = null;
        try {
            //创建查询
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            //从连接池取Es连接
            client = ElasticSearchConnectionPool.getEsClient();
            //构建
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(collectionName)
                    .setTypes(collectionName)
                    .setQuery(matchAllQueryBuilder)
                    .setFrom(0)
                    .setSize(0);
            //查询
            SearchResponse searchResponse = searchRequestBuilder.get();
            //结果
            SearchHits hits = searchResponse.getHits();
            totalHits = hits.totalHits; //总数
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (client != null){
                ElasticSearchConnectionPool.returnEsClient(client);
            }
        }

        return totalHits;
    }

    @Override
    public SearchResultBO batchQueryResult(Page page, List<SearchConditionBO> searchConditionBOs,SearchConditionBO searchConditionBO) {
        SearchResultBO searchResultBO = new SearchResultBO();
        if (CollectionUtils.isEmpty(searchConditionBOs)){
            searchResultBO.setMsg("查询实体为空");
            return  searchResultBO;
        }
        if (StringUtils.isEmpty(searchConditionBO.getKey())){
            searchResultBO.setMsg("没有搜索关键字");
            return searchResultBO;
        }
        //站到元数据表信息
        TransportClient client = null;
        try{
            //创建 ES  client
            client = ElasticSearchConnectionPool.getEsClient();
            //查询的集合名称
            String[] collectionNames = getCollectionsBySearchCondition(searchConditionBOs);
            //构造查询
            QueryBuilder queryBuilder = null;
            String[] keys = searchConditionBO.getKey().split(" ");
            if(keys.length > 1){//多个关键字，去查询全量字段
                String name = "all_" + searchConditionBO.getSearchClass().name();
                if (SearchClass.STR == searchConditionBO.getSearchClass()){
                    name += ".keyword";
                    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                    for (int i = 0; i < keys.length; i++) {
                        boolQueryBuilder.should(QueryBuilders.matchQuery(name, keys[i]));
                    }
                    queryBuilder = boolQueryBuilder;
                }else {
                    queryBuilder = QueryBuilders.multiMatchQuery(searchConditionBO.getKey(), new String[]{name}).minimumShouldMatch(EsConfigureUtil.MINIMUM_SHOULD_MATCH);
                }
            }else{//一个关键字，查询指定字段
                queryBuilder = QueryBuilders.multiMatchQuery(searchConditionBO.getKey(), convertField(searchConditionBO.getSearchClass().name(),getAllSearchField(searchConditionBOs))).minimumShouldMatch(EsConfigureUtil.MINIMUM_SHOULD_MATCH);
            }

            //设置必须满足条件  //设置过滤查询
            //BoolQueryBuilder boolQueryBuilder = parseFilter(searchFilterBO);
            //指定查询index 和 分页信息
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(collectionNames)
                    .setFetchSource(convertReturnField(searchConditionBO.getSearchClass().name(),getAllReturnField(searchConditionBOs)),null) //设置返回的字段      支持通配符返回
                    .setQuery(queryBuilder)
                    .setFrom(page.getStart())
                    //.setPostFilter(boolQueryBuilder)
                    .setSize(page.getLength());

            //设置高亮
            if(searchConditionBO.isHl()){
                startHl(searchRequestBuilder,searchConditionBO);
            }
            //查询
            SearchResponse searchResponse = searchRequestBuilder.get();
            //结果
            searchResultBO = analysisBatchResult(searchResponse,searchConditionBOs,searchConditionBO);
        }catch (Exception e){
            e.printStackTrace();
            searchResultBO.setMsg(e.getMessage());
        }finally {
            if (client != null){
                ElasticSearchConnectionPool.returnEsClient(client);
            }
        }
        return searchResultBO;

    }



    @Override
    public long queryNum(SearchConditionBO searchConditionBO) {
        //判断过滤条件
        SearchFilterBO searchFilterBO = searchConditionBO.getSearchFilterBO();
        if(!StringUtils.isEmpty(searchFilterBO.getExtLink())){
            searchFilterBO.setExtLink(searchFilterBO.getExtLink().toUpperCase());
        }
        //如果没有配置实体  返回0
        if(searchConditionBO.getEntity() == null || StringUtils.isEmpty(searchConditionBO.getKey())){
            return 0;
        }
        //如果没有搜索字段，则返回index中的全部数据
        if (searchConditionBO.getSearchField() == null|| searchConditionBO.getSearchField().size() == 0 ){
            return countCollecitonNumber(searchConditionBO.getEntity().getCollection_name());
        }
        //站到元数据表信息
        try{
            //只查询数据，不要结果，创建一个分页信息，调用查询结果的方法
            Page page = new Page();
            page.setStart(0);
            page.setLength(0);
            //调用查询结果接口
            SearchResultBO searchResultBO = queryResult(page, searchConditionBO);
            //返回数据
            return searchResultBO.getTotal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return 0;
    }

    @Override
    public SearchResultBO queryResult(Page page, SearchConditionBO searchConditionBO) {
        //过滤条件 中的连接字符
        SearchFilterBO searchFilterBO = searchConditionBO.getSearchFilterBO();
        if(searchFilterBO != null && !StringUtils.isEmpty(searchFilterBO.getExtLink())){
            searchFilterBO.setExtLink(searchFilterBO.getExtLink().toUpperCase());
        }
        //如果没有配置实体  返回0
        if(searchConditionBO.getEntity() == null || StringUtils.isEmpty(searchConditionBO.getKey())){
            return new SearchResultBO(0L,new ArrayList<>(),(searchConditionBO.getEntity() == null?"智能搜索实体信息不正确":"搜索关键字为空"));
        }

        //没有展示字段  返回空结果
        if (CollectionUtils.isEmpty(searchConditionBO.getSearchField()) || CollectionUtils.isEmpty(searchConditionBO.getReturnField())){
            return new SearchResultBO(0L,new ArrayList<>(),"搜索字段或者是返回字段不正确");
        }
        //指定查询的字段
        //站到元数据表信息
        TransportClient client = null;
        SearchResultBO searchResultBO = new SearchResultBO();
        try{
            //创建 ES  client
            client = ElasticSearchConnectionPool.getEsClient();
            //查询的集合名称
            String collectionName = searchConditionBO.getEntity().getCollection_name();
            //构造查询
            QueryBuilder queryBuilder = null;
            String[] keys = searchConditionBO.getKey().split(" ");
            if(keys.length > 1){//多个关键字，去查询全量字段
                String name = "all_" + searchConditionBO.getSearchClass().name();
                if (SearchClass.STR == searchConditionBO.getSearchClass()){
                    name += ".keyword";
                    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                    for (int i = 0; i < keys.length; i++) {
                        boolQueryBuilder.should(QueryBuilders.matchQuery(name, keys[i]));
                    }
                    queryBuilder = boolQueryBuilder;
                }else
                    queryBuilder = QueryBuilders.multiMatchQuery(searchConditionBO.getKey(), new String[]{name}).minimumShouldMatch(EsConfigureUtil.MINIMUM_SHOULD_MATCH);
            }else{//一个关键字，查询指定字段
                queryBuilder = QueryBuilders.multiMatchQuery(searchConditionBO.getKey(), convertField(searchConditionBO.getSearchClass().name(),searchConditionBO.getSearchField())).minimumShouldMatch(EsConfigureUtil.MINIMUM_SHOULD_MATCH);
            }
            //设置必须满足条件  //设置过滤查询
            BoolQueryBuilder boolQueryBuilder = null;
            if (searchFilterBO != null) {
                boolQueryBuilder = parseFilter(searchFilterBO);
            }

            //指定查询index 和 分页信息
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(collectionName)
                    .setTypes(collectionName)
                    .setFetchSource(convertField(searchConditionBO),null) //设置返回的字段      支持通配符返回
                    .setQuery(queryBuilder)
                    .setFrom(page.getStart())
                    //.setPostFilter(boolQueryBuilder)
                    .setSize(page.getLength());

            //设置高亮
            if(searchConditionBO.isHl()){
                startHl(searchRequestBuilder,searchConditionBO);
            }
            //查询
            SearchResponse searchResponse = searchRequestBuilder.get();
            //结果
            searchResultBO = analysisResult(searchResponse,searchConditionBO);
        }catch (Exception e){
            e.printStackTrace();
            searchResultBO.setMsg(e.getMessage());
        }finally {
            if (client != null){
                ElasticSearchConnectionPool.returnEsClient(client);
            }
        }
        return searchResultBO;
    }

    @Override
    public SearchResultBO queryByFile(Set<String> key, Page page, SearchConditionBO searchConditionBO) {
        String fileKey = getFileKey(key);
        searchConditionBO.setKey(fileKey);
        return queryResult(page,searchConditionBO);
    }

    @Override
    public SearchResultBO queryResultByIds(SearchConditionBO searchConditionBO, List<String> ids) {
        //如果没有配置实体  返回0
        List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
        if(searchConditionBO.getEntity() == null || CollectionUtils.isEmpty(ids)){
            return new SearchResultBO(0L ,ls ,"实体信息为空，或者id为空");
        }

        //没有展示字段  返回空结果
        if (CollectionUtils.isEmpty(searchConditionBO.getReturnField())){
            return new SearchResultBO(0L ,ls ,"返回字段为空");
        }
        TransportClient client = null;
        String searchClass = searchConditionBO.getSearchClass().name();
        try{
            //创建 ES  client

            client = ElasticSearchConnectionPool.getEsClient();

            //查询的集合名称
            String collectionName = searchConditionBO.getEntity().getCollection_name();
            //构造查询
            MultiGetRequestBuilder multiGetRequestBuilder = client.prepareMultiGet();
            multiGetRequestBuilder.add(collectionName,collectionName,ids);
            //查询
            MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
            Iterator<MultiGetItemResponse> resultIterator = multiGetItemResponses.iterator();
            //结果
            //解析结果集
            while (resultIterator.hasNext()){
                //实际返回的map
                Map<String,Object> realResult = new HashMap<>();
                //ES返回的map
                MultiGetItemResponse nextResult = resultIterator.next();
                GetResponse itemResponse = nextResult.getResponse();
                Map<String, Object> sourceAsMap = itemResponse.getSourceAsMap();
                //遍历返回map 解析封装到
                realResult.put("id",nextResult.getId());
                for (ZsEntityField rf: searchConditionBO.getReturnField()) {
                    //普通非高亮集合
                    realResult.put(rf.getEname(),sourceAsMap.get(searchClass + "_" + rf.getEname()));
                }
                //添加到数据集合
                ls.add(realResult);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (client != null){
                ElasticSearchConnectionPool.returnEsClient(client);
            }
        }
        return new SearchResultBO(Long.parseLong("" + ls.size()),ls,null);
    }

    @Override
    public Map<String, Object> getDetail( SearchConditionBO searchConditionBO) {
        //构造分页信息
        Page page = new Page();
        page.setLength(1);
        page.setStart(0);
        //调用查询接口
        SearchResultBO searchResultBO = queryResult(page, searchConditionBO);
        List<Map<String, Object>> data = searchResultBO.getData();
        if (CollectionUtils.isEmpty(data)){
            return null;
        }
        //返回数据
        return data.get(0);
    }



    @Override
    public Map<String, Object> queryResultByAttention(List<String> ids, Page page, SearchConditionBO searchConditionBO) {
        return null;
    }

    @Override
    public Map<String, String> queryImage(Map<String, Object> map) {
        return null;
    }

    /**
     * 创建TransportClient
     * @return
     */
    protected TransportClient createTransportClient(){
        Settings settings = Settings.builder()
                .put("cluster.name", EsConfigureUtil.ES_CLUSTER_NAME)
                .put("client.transport.sniff",true)
                .build();

        return new PreBuiltTransportClient(settings).addTransportAddresses(EsConfigureUtil.ADDRESSES);
    }






    /**
     * 转化字段变为String数组，供ES使用
     * @param searchClass
     * @param fields
     * @return
     */
    protected String[] convertField(String searchClass , List<ZsEntityField> fields){
        String [] fieldArray = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fieldArray[i] = searchClass +"_"+fields.get(i).getEname();
        }
        return  fieldArray;
    }
    /**
     * 转化字段变为String数组，供ES使用
     * @param searchClass
     * @param fields
     * @return
     */
    protected String[] convertReturnField(String searchClass , List<ZsEntityField> fields){
        String [] fieldArray = new String[fields.size()+1];
        for (int i = 0; i < fields.size(); i++) {
            fieldArray[i] = searchClass +"_"+fields.get(i).getEname();
        }
        fieldArray[fields.size()] = "entity_id";
        return  fieldArray;
    }
    /**
     *
     * @return
     */
    protected List<ZsEntityField>  getAllReturnField( List<SearchConditionBO> searchConditionBOs){
        List<ZsEntityField> fields = new ArrayList<>();
        for (int i = 0; i < searchConditionBOs.size(); i++) {
            fields.addAll(searchConditionBOs.get(i).getReturnField());
        }
        return  fields;
    }
    /**
     *
     * @return
     */
    protected List<ZsEntityField>  getAllSearchField( List<SearchConditionBO> searchConditionBOs){
        List<ZsEntityField> fields = new ArrayList<>();
        for (int i = 0; i < searchConditionBOs.size(); i++) {
            fields.addAll(searchConditionBOs.get(i).getSearchField());
        }
        return  fields;
    }
    /**
     * 转化字段变为String数组，供ES使用
     * @param searchConditionBO
     * @return
     */
    protected String[] convertField(SearchConditionBO searchConditionBO){
        String [] fieldArray = new String[searchConditionBO.getReturnField().size()];
        for (int i = 0; i < fieldArray.length; i++) {
            fieldArray[i] = searchConditionBO.getSearchClass().name() +"_"+searchConditionBO.getReturnField().get(i).getEname().toLowerCase();
        }
        return  fieldArray;
    }


    /**
     * 过滤查询
     * @param searchFilterBO
     */
    protected static BoolQueryBuilder parseFilter(SearchFilterBO searchFilterBO){

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //字段
        if (searchFilterBO.getExtFields() != null && searchFilterBO.getExtFields().length > 0){
            for (int i = 0; i < searchFilterBO.getExtFields().length; i++) {
                QueryBuilder queryBuilder = null;
                if(searchFilterBO.getCondition()[i].equals("gt")){
                    //大于
                    //query.addFilterQuery("fq",fieldSuffer+"_"+extFields[i] + ":" + "["+parseKey(values[i],jyZd,collectionName,searchClass)+" TO *]");
                }else if(searchFilterBO.getCondition()[i].equals("lt")){
                    //小于
                    //query.set("fq",fieldSuffer+"_"+extFields[i] + ":" + "[ * TO "+parseKey(values[i],jyZd,collectionName,searchClass)+"]");
                }else if(searchFilterBO.getCondition()[i].equals("eq")){
                    //等于
                    queryBuilder = QueryBuilders.matchQuery("STR" + "_" + searchFilterBO.getExtFields()[i], searchFilterBO.getValues()[i]);
                }else if(searchFilterBO.getCondition()[i].equals("in")){
                    //包含
                    queryBuilder = QueryBuilders.wildcardQuery("STR"+"_"+searchFilterBO.getExtFields()[i],"*"+searchFilterBO.getValues()[i]+"*");
                }else if(searchFilterBO.getCondition()[i].equals("notIn")){
                    //不包含
                }
                // 如果是 And  就用must   如果是  其他  就用 should
                if ("AND".equalsIgnoreCase(searchFilterBO.getExtLink())){
                    boolQueryBuilder.must(queryBuilder);
                }else{
                    boolQueryBuilder.should(queryBuilder);
                }

            }
        }
        return boolQueryBuilder;
    }


    /**
     * 开启高亮查询
     */
    protected void startHl(SearchRequestBuilder searchRequestBuilder,SearchConditionBO searchConditionBO){
        HighlightBuilder hlb = new HighlightBuilder();
        //高亮样式
        hlb.preTags(EsConfigureUtil.HIGHLIGHT_PRETAGS);
        hlb.postTags(EsConfigureUtil.HIGHLIGHT_POSTTAGS);
        hlb.field(searchConditionBO.getSearchClass().name()+"_*");
        searchRequestBuilder.highlighter(hlb);
    }

    /**
     * 拼接查询文件中的关键字
     * @param keys
     * @return
     */
    protected String getFileKey(Set<String> keys){
        StringBuilder sb = new StringBuilder();

        for (String key: keys) {
            sb.append(key).append(" ");
        }

        return sb.toString();
    }


    /**
     * 解析查询结果，封装成BO对象
     * @param searchResponse
     * @param searchConditionBO
     * @return
     */
    protected SearchResultBO analysisResult(SearchResponse searchResponse,SearchConditionBO searchConditionBO){
        List<Map<String,Object>> ls = new ArrayList<>();
        //结果
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.totalHits; //总数
        //解析结果集
        Iterator<SearchHit> resultIterator = hits.iterator();
        while (resultIterator.hasNext()){
            //实际返回的map
            Map<String,Object> realResult = new HashMap<>();

            //ES返回的map
            SearchHit searchHit = resultIterator.next();

            realResult.put("id",searchHit.getId());
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            //遍历返回map 解析封装到

            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();


            for (ZsEntityField rf:searchConditionBO.getReturnField() ) {
                if(searchConditionBO.isHl()){
                    //封装高亮集合
                    if(highlightFields.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()) != null){
                        Text[] fragments = highlightFields.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()).fragments();
                        String hlvalue = fragments[0].toString();
                        realResult.put(rf.getEname(),hlvalue);
                        continue;
                    }
                }
                //普通非高亮集合
                realResult.put(rf.getEname(),sourceAsMap.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()));

            }
            //添加到数据集合
            ls.add(realResult);
        }
        SearchResultBO searchResultBO = new SearchResultBO();
        searchResultBO.setData(ls);
        searchResultBO.setTotal(totalHits);

        return  searchResultBO;
    }
    /**
     * 解析查询结果，封装成BO对象
     * @param searchResponse
     * @param searchConditionBO
     * @return
     */
    protected SearchResultBO analysisBatchResult(SearchResponse searchResponse,List<SearchConditionBO> searchConditionBOs,SearchConditionBO searchConditionBO){
        Map<String,SearchConditionBO> searchConditionBOMap = new HashMap<>();
        for (int i = 0; i < searchConditionBOs.size(); i++) {
            searchConditionBOMap.put(searchConditionBOs.get(i).getEntity().getId(),searchConditionBOs.get(i));
        }
        List<Map<String,Object>> ls = new ArrayList<>();
        //结果
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.totalHits; //总数
        //解析结果集
        Iterator<SearchHit> resultIterator = hits.iterator();
        while (resultIterator.hasNext()){
            //实际返回的map
            Map<String,Object> realResult = new HashMap<>();

            //ES返回的map
            SearchHit searchHit = resultIterator.next();

            realResult.put("id",searchHit.getId());
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            //遍历返回map 解析封装到

            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            Object entityCode = sourceAsMap.get("entity_id");
            if (entityCode == null)
                continue;
            realResult.put("ENTITY_ID",sourceAsMap.get("entity_id"));
            SearchConditionBO tempSearchConditionBo = searchConditionBOMap.get(entityCode);
            for (ZsEntityField rf: tempSearchConditionBo.getReturnField()) {
                if(searchConditionBO.isHl()){
                    //封装高亮集合
                    if(highlightFields.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()) != null){
                        Text[] fragments = highlightFields.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()).fragments();
                        String hlvalue = fragments[0].toString();
                        realResult.put(rf.getEname().toLowerCase(),hlvalue);
                        continue;
                    }
                }
                //普通非高亮集合
                realResult.put(rf.getEname().toLowerCase(),sourceAsMap.get(searchConditionBO.getSearchClass().name() + "_" + rf.getEname()));

            }
            //添加到数据集合
            ls.add(realResult);
        }
        SearchResultBO searchResultBO = new SearchResultBO();
        searchResultBO.setData(ls);
        searchResultBO.setTotal(totalHits);

        return  searchResultBO;
    }

    private String[] getCollectionsBySearchCondition(List<SearchConditionBO> searchConditionBOS){
        List<String> collections = new ArrayList<>();
        for (int i = 0; i < searchConditionBOS.size(); i++) {
            collections.add(searchConditionBOS.get(i).getEntity().getCollection_name());
        }
        return collections.toArray(new String[collections.size()]);
    }
}
