package com.fas.search.util.common;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/8.
 */
public class DateParseUtil {

    private static SimpleDateFormat sdfString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 字符日期转为日期
     * @param dateStr
     * @return
     */
    public static Date getDate(String dateStr){
        if (StringUtils.isEmpty(dateStr)){
            return null;
        }
        Date date = null;
        try {
            date = sdfDate.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转为字符串
     * @param date
     * @return
     */
    public static String getDate(Date date){
        if(date == null){
            return null;
        }
        String dateStr = null;
        dateStr = sdfString.format(date);
        return dateStr;
    }

    /**
     * 获取日志类型的时间戳 yyyyMMddHHmmssSSS
     * @param date
     * @return
     */
    public static String getTimeStampString(Date date){
        String dateStr = null;
        dateStr = sdfDate.format(date);
        return dateStr;
    }
}
