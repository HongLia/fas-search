package com.fas.search.search.common.pool;

import com.fas.base.model.FasConstants;
import org.apache.commons.lang.StringUtils;

/**
 *版权声明 中科金审（北京）有限公司 版权所有 违者必究
 *<br> Company：中科金审
 *<br> @version 1.0
 *<br> 连接池配置信息
 *
 *
 */
public class EnginePoolConfigUtil {

	//solr 最大的连接时间
	public static Integer CONNECT_TIME_MILLIS = 8000;
	//solr 最大连接数
	public static Integer MAX_TOTAL = 50 ;
	//solr 最大等待时间
	public static Integer MAX_WAIT_MILLIS = 50000;

	static{
		//最大的连接时间
		if(!StringUtils.isEmpty(FasConstants.PROPERTIES.PROMAP.get("search.pool.connectTimeMillis"))){
			CONNECT_TIME_MILLIS = Integer.parseInt(FasConstants.PROPERTIES.PROMAP.get("search.pool.connectTimeMillis"));
		}

		//最大连接数
		if (!StringUtils.isEmpty(FasConstants.PROPERTIES.PROMAP.get("search.pool.maxTotal"))){
			MAX_TOTAL = Integer.parseInt(FasConstants.PROPERTIES.PROMAP.get("search.pool.maxTotal"));
		}
		//最大等待时间
		if (!StringUtils.isEmpty(FasConstants.PROPERTIES.PROMAP.get("search.pool.maxWaitMillis"))){
			MAX_WAIT_MILLIS = Integer.parseInt(FasConstants.PROPERTIES.PROMAP.get("search.pool.maxWaitMillis"));
		}
	}

}
