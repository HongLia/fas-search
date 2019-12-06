package com.fas.search.search.job;

import com.alibaba.fastjson.JSON;
import com.fas.metadata.service.MetaDataInterfaceService;
import com.fas.search.manage.entity.ZsEntity;
import com.fas.search.manage.entity.ZsEtlTask;
import com.fas.search.manage.mapper.ZsEntityMapper;
import com.fas.search.manage.mapper.ZsEtlTaskMapper;
import com.fas.search.search.bo.EngineInfoBO;
import com.fas.search.search.engine.SearchEngineService;
import com.fas.search.util.jdbc.ConnectionUtil;
import com.fas.search.util.jdbc.SqlUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 数据抽取任务
 * @date 2019/8/15
 */
@Component
public class DataExtractJob implements JobRunner {
    protected final Logger log = Logger.getLogger(getClass());
    @Autowired
    private ZsEtlTaskMapper zsEtlTaskMapper;

    @Autowired
    private ZsEntityMapper zsEntityMapper;

    @Autowired
    private MetaDataInterfaceService metaDataInterfaceService;

    @Autowired
    private SearchEngineService searchEngineService;

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        if (true){
            return null;
        }
        String param = jobContext.getJob().getParam("taskId");
        //通过任务id获取任务，
        ZsEtlTask task = zsEtlTaskMapper.selectByPrimaryKey(param);
        if (task == null){
            log.error("任务异常，请确认任务id再执行："+param);
            System.out.println("任务异常，请确认任务id再执行");
            return null;
        }
        //日期格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //找到对应的实体
        ZsEntity entity = zsEntityMapper.selectByPrimaryKey(task.getEntity_id());
        //找到数据源信息
        String datasourceStr = metaDataInterfaceService.getDataSourceById(entity.getDatasource_id());
        //转化为map
        Map<String,Object> datasourceTemp = JSON.parseObject(datasourceStr, Map.class);
        Map<String,String> datasource = convertDatasource(datasourceTemp) ;
        //获取数据库连接
        Connection connection = getConnection(datasource);
        //获取最大值
        Long maxShardId = getMaxShardId(connection, entity);
        //获取最小值
        Long minValue = getMinValue(connection, entity, task);
        if(maxShardId <= minValue){
            //条件错误   任务终止
            log.error("没有大于最大值的数据");
            System.out.println("没有大于最大值的数据");
            return new Result(Action.EXECUTE_LATER, "没有大于最大值的数据");
        }

        boolean endJobFlag = true;
        //搜索引擎信息
        EngineInfoBO engineInfoBO = new EngineInfoBO();
        //设置操作集合名称
        engineInfoBO.setCollectionName(entity.getCollection_name());
        //创建BO
        if("1".equals(task.getDelete_old()) && "2".equals(task.getType())){
            searchEngineService.clearnCollectionData(engineInfoBO);
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int pageNum = 0 ;
            ArrayList<String> columnlist = new ArrayList<String>();
            do{
                //拼接执行的查询语句
                String pageSql = "";
                if(StringUtils.isEmpty(entity.getExecute_sql())){ //没有手动输入执行语句
                    pageSql =   SqlUtil.getPageSql(datasource,entity,pageNum);
                }else{//输入了执行语句
                    pageSql = SqlUtil.getConditionSql(datasource,entity,pageNum);
                }
                if ("greenplum".equals(String.valueOf(datasource.get("dic_id"))) || "mpp".equals(String.valueOf(datasource.get("dic_id")))){//gp需要特殊处理分页查询
                    pageSql += " AND " + entity.getIncrement_field() +" >= "+(minValue+pageNum*SqlUtil.SQL_PAGE_SIZE) + " AND " +  entity.getIncrement_field()+"<" + (minValue+(pageNum+1)*SqlUtil.SQL_PAGE_SIZE + " order by "+ entity.getIncrement_field());
                }
                System.out.println(pageSql);
                //4, 查询
                ps = connection.prepareStatement(pageSql);
                rs = ps.executeQuery();
                // 获取字段信息
                ResultSetMetaData md = rs.getMetaData();

                //查询语句一样，判断如果已经处理过了，就不需要在处理
                if (CollectionUtils.isEmpty(columnlist)){
                    int count = md.getColumnCount();
                    for (int i = 0; i < count; i++) {
                        columnlist.add(md.getColumnName(i + 1));
                    }
                }

                //3解析结果集
                long startTime = System.currentTimeMillis();
                long shard_id =0L;
                List<Map<String,Object>> datas = new ArrayList<>();
                while (rs.next()) {
                    //4创建document 加入list
                    shard_id = rs.getLong(entity.getIncrement_field());
                    Map<String,Object> data = new HashMap<>();
                    //自增字段作为索引的id
                    data.put("id",String.valueOf(shard_id));
                    //初始化属性，索引属于哪个实体
                    data.put("entity_id",task.getEntity_id());
                    //处理一条结果
                    for (int i = 0; i < columnlist.size(); i++) {
                        //列名
                        String name = columnlist.get(i);
                        //类型
                        String type = md.getColumnTypeName(i + 1).toUpperCase();
                        //没有数据不建立索引
                        if(rs.getObject(name) == null || "".equals(String.valueOf(rs.getObject(name)).trim())){
                            continue;
                        }
                        //处理结果，放入doc里
                        //handleDocument(type,name,rs,sdf,writeDocumentBo.getPrefix(),data);
                        handleDocument(type,name,rs,sdf,data);
                    }

                    datas.add(data);

                    //大于最大值  结束
                    if (shard_id >= maxShardId){
                        endJobFlag = false;
                        break;
                    }
                }

                //向搜索引擎中写入索引
                //3解析结果集
                if (datas.size() > 0){
                    searchEngineService.writeDocument(engineInfoBO,datas);
                }
                System.out.println("use time:"+(System.currentTimeMillis() - startTime)/1000);
                //关闭没用的结果集和ps
                ConnectionUtil.releaseConn(null,ps,rs);

                //查询总数，记录到实体表中
                long total = searchEngineService.countCollecitonNumber(entity.getCollection_name());
                //更新总数
                entity.setScount(total);
                zsEntityMapper.updateByPrimaryKeySelective(entity);
                task.setQuery_last_value(String.valueOf(shard_id));
                zsEtlTaskMapper.updateByPrimaryKeySelective(task);
                pageNum ++;
            }while(endJobFlag);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Action.EXECUTE_LATER, e.getMessage());
        }finally {
            //释放数据库资源
            ConnectionUtil.releaseConn(connection,ps,rs);
            //释放搜索引擎资源
            searchEngineService.releaseClient(engineInfoBO);
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功");
    }


    public Map<String,String> convertDatasource(Map<String,Object> datasource){
        List<Map<String,String>> msg = (List<Map<String, String>>) datasource.get("data");
        if (CollectionUtils.isEmpty(msg)){
            log.error("数据库类型为空，请联系管理员："+datasource);
            throw new RuntimeException("数据库类型为空，请联系管理员");
        }
        return msg.get(0);
    }

    public Connection getConnection(Map<String,String> datasource) throws SQLException {

        String dic_id =  datasource.get("dic_id");
        datasource.get("data");
        if (dic_id == null){
            log.error("数据库类型为空，请联系管理员"+datasource);
            throw new RuntimeException("数据库类型为空，请联系管理员");
        }
        dic_id = dic_id.toLowerCase();
        String dbUrl = "";
        switch (dic_id){
            case "mysql":{
                dbUrl = getMysqlUrl(datasource);
                break;
            }
            case "greenplum" :
            case  "mpp":{
                dbUrl = getGPUrl(datasource);
                break;
            }
            default:
                log.error("没有找到数据库类型，请确认数据源信息是否正确:"+dic_id);
                throw new RuntimeException("没有找到数据库类型，请确认数据源信息是否正确:"+dic_id);

        }
        Connection connection = DriverManager.getConnection(dbUrl, datasource.get("db_user_name"), datasource.get("db_user_password"));
        return connection;
    }

    /**
     * 根据数据源连接信息，获取mysql url信息
     * @param datasource
     * @return
     */
    private String getMysqlUrl(Map<String,String> datasource) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://"+datasource.get("db_ipaddress")+":"+datasource.get("db_port")+"/"+datasource.get("db_name");
            return url;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据数据源连接信息，获取greenplum url信息
     * @param datasource
     * @return
     */
    private String getGPUrl(Map<String,String> datasource) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://"+datasource.get("db_ipaddress")+":"+datasource.get("db_port")+"/"+datasource.get("db_name") + "?currentSchema=" + datasource.get("db_schema");
            return url;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取最大值
     * @param conn 数据库连接
     * @param entity 实体信息
     * @return
     */
    public static Long getMaxShardId(Connection conn,ZsEntity entity){
        //String sql = "select max(shard_id) from"+ tableName + " where " + queryCondition;
        StringBuffer sb = new StringBuffer();
        //拼接查询最大值得sql
        sb.append("select max(").append(entity.getIncrement_field()).append(") from").append(" ").append(entity.getTablename()).append(" ").append("where").append(" ").append("1=1").append(" ");
        if (!StringUtils.isEmpty(entity.getFilter_sql())){
            sb.append(" ").append("AND").
                    append(" ").append(entity.getFilter_sql());
        }

        return executeQuery(conn,sb.toString());
    }


    /**
     * 获取最小值
     * @return
     */
    public static Long getMinValue(Connection conn, ZsEntity entity, ZsEtlTask task){
        //索引类型  1增量，2全量
        String indexType = task.getType();
        //如果是增量，判断之前上一次的值
        if ("1".equals(indexType) && StringUtils.isEmpty(task.getQuery_last_value())){
            return Long.parseLong(task.getQuery_last_value());
        }

        StringBuffer sb = new StringBuffer();
        sb.append("select min(").append(entity.getIncrement_field()).
                append(") from").append(" ").append(entity.getTablename()).
                append(" ").append("where").
                append(" ").append("1=1");
        if (!StringUtils.isEmpty(entity.getFilter_sql())){
            sb.append(" ").append("AND").
                    append(" ").append(entity.getFilter_sql());
        }


        return executeQuery(conn,sb.toString());
    }

    /**
     * 执行查询最大值或最小值的sql
     * @param conn
     * @param sql
     * @return
     */
    public static Long executeQuery(Connection conn ,String sql){
        Long result = 0L;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()){
                result = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ConnectionUtil.releaseConn(null,pstm,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 处理结果集
     * @param type
     * @param name
     * @param rs
     * @param sdf
     * @param data
     * @throws SQLException
     */
    private void handleDocument(String type, String name, ResultSet rs, SimpleDateFormat sdf,  Map<String,Object> data) throws SQLException {
        if ("VARCHAR2".equalsIgnoreCase(type) || "VARCHAR".equalsIgnoreCase(type) || "CHAR".equalsIgnoreCase(type)||"string".equalsIgnoreCase(type) ||"NVARCHAR2".equalsIgnoreCase(type)|| type.contains("CHAR")) {
            //字符类型
            //ik
            data.put(
                    "IK_" + name,
                    rs.getString(name).trim()
            );
            //string
            data.put(
                    "STR_" + name,
                    rs.getString(name)
            );
        } else if ("DATE".equalsIgnoreCase(type) ||"DATETIME".equalsIgnoreCase(type) || "timestamp".equalsIgnoreCase(type)  ) {
            //如果字段是日期类型，则如索引的时候需要把索引改成日期类型字段
            data.put(
                    "IK_" + name,
                    sdf .format(rs.getTimestamp(name)));
            data.put(
                    "STR_" + name,
                    sdf.format(rs.getTimestamp(name)));
            data.put(
                    "DATE_" + name,
                    sdf.format(rs.getTimestamp(name)));
        } else if ("FLOAT".equalsIgnoreCase(type)) {
            //fload类型
            data.put("IK_" + name, rs.getFloat(name));
            /*  doc.addField(collectionName + "_PY_" + name, rs.getFloat(name));*/
            data.put("STR_" + name, rs.getFloat(name));
            data.put("FLOAT_" + name, rs.getFloat(name));
        } else if ("DOUBLE".equalsIgnoreCase(type)) {
            //dubbo类型
            data.put("IK_" + name, rs.getDouble(name));
            /*  doc.addField(collectionName + "_PY_" + name, rs.getDouble(name));*/
            data.put("STR_" + name, rs.getDouble(name));
        } else if ("LONG".equalsIgnoreCase(type)) {
            //long 类型
            data.put("IK_" + name, rs.getLong(name));
            /* doc.addField(collectionName + "_PY_" + name, rs.getLong(name));*/
            data.put("STR_" + name, rs.getLong(name));
            data.put("LONG_" + name, rs.getLong(name));
        } else if (type != null && (type.contains("INT") || "INT".equalsIgnoreCase(type) || "INTEGER".equalsIgnoreCase(type) || "BIGINT".equalsIgnoreCase(type) || "NUMBER".equalsIgnoreCase(type) || "NUMERIC".equalsIgnoreCase(type) || "DECIMAL".equalsIgnoreCase(type)  || "BIGSERIAL".equalsIgnoreCase(type))) {

            data.put(
                    "IK_" + name,
                    rs.getString(name)
            );
                       /* doc.addField(
                                collectionName + "_PY_" + name,
                                rs.getString(name) == null ? "" : rs.getString(name)
                        );*/
            data.put(
                    "STR_" + name,
                    rs.getString(name)
            );
            data.put(
                    "INT_" + name,
                    rs.getString(name)
            );
        } else if("BLOB".equalsIgnoreCase(type) || "CLOB".equalsIgnoreCase(type) ){
            //blob 类型也需要做特殊处理
                       /* Blob blob = rs.getBlob(name);
                        doc.addField(collectionName + "_IK_" + name,blob);
                        doc.addField(collectionName + "_PY_" + name,blob);
                        doc.addField(collectionName + "_STR_" + name,blob);*/
        }else{
            //未知类型
            data.put(
                    "IK_" + name,
                    rs.getObject(name)
            );
            data.put(
                    "STR_" + name,
                    rs.getObject(name)
            );
        }
    }
}
