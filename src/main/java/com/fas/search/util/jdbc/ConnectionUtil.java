package com.fas.search.util.jdbc;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.net.URI;
import java.sql.*;
import java.util.Map;

/**
 * JDBC工具类
 */
public class ConnectionUtil {

    // 释放数据库连接相关信息
    public static void releaseConn(Connection conn, Statement stm, ResultSet rs)
            throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stm != null) {
            stm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

}
