package com.fas.search.search.common.pool.solr;

import com.fas.base.model.FasConstants;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *版权声明 中科金审（北京）有限公司 版权所有 违者必究
 *<br> Company：中科金审
 *<br> @version 1.0
 *<br> 获取solr配置文件
 *
 *
 */
public class SolrPropertiesUtil {
	//solr zk地址
	public static String zkHost;
	//solr 客户端连接超时
	public static Integer zkClientTimeout = 10000000;
	//solr 客户端连接时间
	public static Integer zkConnectTimeout = 10000000;

	static{
		//读取solr配置文件
		zkHost = FasConstants.PROPERTIES.PROMAP.get("search.solr.zkHost");
		String tempZkClientTimeout = FasConstants.PROPERTIES.PROMAP.get("search.solr.zkClientTimeout");
		//判断配置zk超时是否为空，如果为空则设置默认值
		if (!StringUtils.isEmpty(tempZkClientTimeout)){
			zkClientTimeout = Integer.parseInt(tempZkClientTimeout);
		}
		//判断配置solr连接超时是否为空，如果为空则设置默认值
		String tempZkConnectTimeout = FasConstants.PROPERTIES.PROMAP.get("search.solr.zkConnectTimeout");
		if (!StringUtils.isEmpty(tempZkConnectTimeout)){
			zkConnectTimeout = Integer.parseInt(tempZkConnectTimeout);
		}
	}
}
