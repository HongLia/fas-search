import com.fas.search.search.common.pool.es.ElasticSearchConnectionPool;
import com.fas.search.search.common.pool.es.EsConfigureUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
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
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


public class WuzyTestEs {

    static TransportClient createTransportClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", EsConfigureUtil.ES_CLUSTER_NAME)
                .put("client.transport.sniff",true)
                .build();


        String[] hosts = "10.1.1.31:24147,10.1.1.32:24147,10.1.1.33:24147,10.1.1.34:24147".split(",");
        TransportAddress[] ADDRESSES = new TransportAddress[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String[] hostAndPort = hosts[i].split(":");
            if (hostAndPort.length<2){
                continue;
            }
            ADDRESSES[i] = new TransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1]));
        }
        return new PreBuiltTransportClient(settings).addTransportAddresses(ADDRESSES);
    }

    static TransportClient client = null;
    String collectionName = "gas_person_f6e198064b7848fa8bfacf577bbea830";
    static {
        try {
            client = createTransportClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryData () {

        QueryBuilder queryBuilder = null;
       /* queryBuilder = QueryBuilders.matchQuery("all_STR.keyword","张三");*/
      /*  queryBuilder = QueryBuilders.multiMatchQuery("企业", new String[]{"STR_khyhmc"
                ,"STR_zgbmmc"
                ,"STR_dbrxm"
                ,"STR_dbrzjhm"
                ,"STR_khmc"
                ,"STR_zzhm"
                ,"STR_nl"
                ,"STR_hjdzxzqh"
                ,"STR_zzlx"
                ,"STR_xb"
                , "STR_ryid"
                , "STR_hyzk"});*/
        //设置必须满足条件  //设置过滤查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("IK_khmc","企业1"));
        //指定查询index 和 分页信息
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(collectionName)
                .setTypes(collectionName)
                .setQuery(boolQueryBuilder)
                .setFetchSource(new String[]{"STR_khyhmc"
                        ,"STR_zgbmmc"
                        ,"STR_dbrxm"
                        ,"STR_dbrzjhm"
                        ,"STR_khmc"
                        ,"STR_zzhm"
                        ,"STR_crrq"},null)
                .setFrom(0)
                //.setPostFilter(boolQueryBuilder)
                .setSize(10);

        //查询
        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.totalHits);
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            System.out.println(sourceAsMap);

        }
    }


    @Test
    public void cleanData(){
        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).source(collectionName).filter(QueryBuilders.matchAllQuery()).get();
    }

    static List<Map<String,Object>> datas = new ArrayList<>();

    @Test
    public void writeData() throws IOException {
        BulkRequestBuilder bulkRequestBuilder = null;
        bulkRequestBuilder = client.prepareBulk();

        for (int i = 0; i < datas.size(); i++) {

            XContentBuilder onceEsData = XContentFactory.jsonBuilder().startObject();
            //一条数据
            Map<String, Object> data = datas.get(i);
            Set<String> keys = data.keySet();
            //设置主键为为id值
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(collectionName, collectionName, String.valueOf(data.get("id")));
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
    }


    @Test
    public void deleteCollection(){
        client.admin().indices().prepareDelete(collectionName).execute().actionGet();
        System.out.println("删除成功："+collectionName);
    }

    @Test
    public void countCollectionNum(){
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
        System.out.println(hits.totalHits);
    }


    @Test
    public void createCollection(){
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
            }else{
                System.out.println("创建" + collectionName + "失败");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static {
        Map<String,Object> d1= new HashMap<>();
        d1.put("entity_id","f6e198064b7848fa8bfacf577bbea830");
        d1.put("STR_khmc","企业1");
        datas.add(d1);
    }
}
