package com.fas.search.util.jdbc;

import com.fas.search.manage.entity.ZsEntity;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import java.sql.*;

/**
 * Created by Administrator on 2017/12/11.
 */
public class SqlUtil {

    public static final int SQL_PAGE_SIZE = 5000;

    /**
     * 分页查询的sql
     * @param
     * @param pageNum
     * @return
     */
    public static String getPageSql(Map<String,String> datasource, ZsEntity entity,Integer pageNum){
        String type = datasource.get("dic_id");
        String tableName = entity.getTablename();
        String userName = datasource.get("db_user_name");
        String condition = entity.getFilter_sql();
        if ("oracle".equals(type)){
            //Oracle
            return "SELECT\n" +
                    "\t"+ " * "+"\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\tSELECT\n" +
                    "\t\t\tA.*,\n" +
                    "\t\t\trownum rn\n" +
                    "\t\tFROM\n" +
                    "\t\t\t(\n" +
                    "\t\t\t\tSELECT\n" +
                    "\t\t\t\t\t*\n" +
                    "\t\t\t\tFROM\n" +
                    "\t\t\t\t\t"+userName+"."+tableName+"\n" +
                    "\t\t\t where 1=1 "+condition+") A\n" +
                    "    where rownum <= "+SQL_PAGE_SIZE * (pageNum + 1) + "\n" +
                    "\t)\n" +
                    "where rn > " + SQL_PAGE_SIZE * pageNum +" ";

        }else if("mysql".equals(type)){
            //Mysql
            return  "select " + " * " + " from " + tableName + " where 1=1 " + condition  + "  limit " + pageNum * SQL_PAGE_SIZE + "," + SQL_PAGE_SIZE;
        }else if("sqlserver".equals(type)){
            //SqlServer
            return "";
        }else if("greenplum".equals(type)||"mpp".equals(type)){
            //Gp
            return  "select " + " * " + " from " + tableName + " where 1=1 " + condition;
        }else{
            return "";
        }
    }
    /**
     * 分页查询的sql
     * @param
     * @param pageNum
     * @return
     */
    public static String getConditionSql(Map<String,String> datasource, ZsEntity entity , Integer pageNum){
        String selectSql = entity.getExecute_sql();
        if ("oracle".equals(datasource)){
            //Oracle
            return "SELECT\n" +
                    "\t"+ " * "+"\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\tSELECT\n" +
                    "\t\t\tA.*,\n" +
                    "\t\t\trownum rn\n" +
                    "\t\tFROM\n" +
                    "\t\t\t(\n" +
                    selectSql
                    +") A\n" +
                    "    where rownum <= "+SQL_PAGE_SIZE * (pageNum + 1) + "\n" +
                    "\t)\n" +
                    "where rn > " + SQL_PAGE_SIZE * pageNum +" ";



            /*"    where rownum > "++"\n" +
                    "\t)\n" +
                    "where rn <= " + SQL_PAGE_SIZE * (pageNum + 1)+" ";*/
        }else if("mysql".equals(datasource)){
            //Mysql
            return  selectSql  + "  limit " + pageNum * SQL_PAGE_SIZE + "," + SQL_PAGE_SIZE;
        }else if("sqlserver".equals(datasource)){
            //SqlServer
            return "";
        }else if("greenplum".equals(datasource)){
            //Gp
            return  selectSql ;
        }else{
            return "";
        }
    }


    /**
     * 判断时间 字段是日期类型还是字符串类型
     * @param type
     * @return
     */
    public static boolean isString(String type){

        if ( type == null ){
            return false;
        }

       String temp = type.toLowerCase();

       if (temp.indexOf("char") > -1 || temp.indexOf("string") >  -1 ){
           return true;
       }

        return false;
    }


    /**
     * 拼接查询的条件
     * @param condition 用户输入的条件
     * @param sourceType   数据源类型
     * @param data  增量数据
     * @param type  时间字段类型 3，日期形字符串，2，日期类型，1，自增的整型
     * @param fieldCode 字段名
     * @return
     */
    public static String paseCondition(String condition,String sourceType,String data,String type,String fieldCode){
        String function = "";
        String myformat = "";
        String sql = "";
        if (!StringUtils.isEmpty(condition)){
            sql += " AND " + condition + " ";
        }
        if (StringUtils.isEmpty(data)){
            return sql;
        }

        if (sourceType.equals("1")) {
            //oracle
            function = "to_date";
            myformat = ",'yyyy-mm-dd hh24:mi:ss')";
        } else if (sourceType.equals("001")) {
            //mysql
            function = "str_to_date";
            myformat=",'%Y-%m-%d %H:%i:%S.%f')";
        } else if (sourceType.equals("3")) {
            Driver driver =null;//TODO 废弃     //new com.cloudera.impala.jdbc41.Driver();
        } else if (sourceType.equals("002")) {
            //gp
            function = "to_timestamp";
            myformat=",'YYYY-MM-DD HH24:MI:SS.MS')";
        } else {
            //默认给个gp
            function = "to_timestamp";
            myformat=",'YYYY-MM-DD HH24:MI:SS.MS')";
        }



        if (StringUtils.isEmpty(data)) {
            //第一次执行 全部查询
            /*if (isString(relation.getSJZDTYPE())){

            } else {
                *//*sql += " and "
                        + relation.getSJZD()
                        + " > "+function+"('1949-10-01 00:00:00'"+myformat+" or "
                        + relation.getSJZD() + " is null ";*//*
            }*/
        } else {
            //非第一次执行
            // 3，日期形字符串，2，日期类型，1，自增的整型
            if (type.equals("3")){
                sql += " and " + fieldCode + " > '" + data + "' ";
            } else if(type.equals("2")){
                sql += " and " +fieldCode + " > "+function+"('" + data + "'"+myformat + " ";
            } else if(type.equals("1")){
                sql += " and " + fieldCode + " > " + data + " ";
            }
        }


        return sql;
    }


}
