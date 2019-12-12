package com.fas.search.util.common;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatchStringUtil {
	//solr搜索解析key,对key进行处理
	public static String parse(String key){
		if(key==null||key.trim().length()==0){
			return key;
		}
		key=convertString(key);
		if(chinese(key)){
			key="\""+key+"\"";
		}else{
			key=key+"*";
		}
		return key;
	}
	//去除关键词中的特殊字符
	public static String convertString(String key){
		String regex="[`~!@#$%^&()+=|{}':;',\\[\\].<>/~！@#￥%……&（）——+|{}【】‘；：”“’。，、？-]";
		Pattern pat=Pattern.compile(regex);
		Matcher matcher= pat.matcher(key);
		return matcher.replaceAll("");
	}
	//验证是否包含特殊字符
	public static boolean checkString(String key){
		String regex="[`~!@#$%^&()+=|{}':;',\\[\\].<>/~！@#￥%……&（）——+|{}【】‘；：”“’。，、？-]";
		Pattern pat=Pattern.compile(regex);
		Matcher matcher= pat.matcher(key);
		return matcher.find();
	}
	//是否包含中文
	public static boolean isChinese(String key){
		String regex="[\\u4e00-\\u9fa5]";
		Pattern pat=Pattern.compile(regex);
		Matcher matcher= pat.matcher(key);
		return matcher.find();
	}
	//是否包含数字
	public static boolean isNum(String key){
		String regex="[0-9]";
		Pattern pat=Pattern.compile(regex);
		Matcher matcher= pat.matcher(key);
		return matcher.find();
	}


	/**
	 * 校验是否是纯数字
	 * @param key
	 * @return
	 */
	public static boolean checkNum(String key){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		for (int i = 0; i < key.length(); i++) {
			if(key.charAt(i) < '0' || key.charAt(i) > '9'){
				return false;
			}
		}
		return true;
	}
	//是否包含字母
	public static boolean isEglish(String key){
		String regex="[a-zA-Z]";
		Pattern pat=Pattern.compile(regex);
		Matcher matcher= pat.matcher(key);
		return matcher.find();
	}
	//是否纯中文
	public static boolean chinese(String key){
		if(isChinese(key)&&!isNum(key)&&!isEglish(key)){
			return true;
		}
		return false;
	}
	/**
	 * 判断是否是身份证号码
	 * @param key
	 * @return
     */
	public static boolean isIdentification(String key){
		String sfz = "([1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
		return Pattern.matches(sfz,key);
	}

	/**
	 * 判断手机号
	 * @param key
	 * @return
     */
	public static boolean isPhone(String key){
		String sjhm = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\d{8}$";
		return Pattern.matches(sjhm,key);
	}

	/**
	 * 判断银行卡号
	 * @param key
	 * @return
     */
	public static boolean isBankCard(String key){
		String sjhm = "[\\d]{14,19}";
		return Pattern.matches(sjhm,key);
	}
	//格式化日期类
	public static String convertDateFormat(String dateStr,String format,String toFormat){
		SimpleDateFormat stod=new SimpleDateFormat(format);
		SimpleDateFormat dtos=new SimpleDateFormat(toFormat);
		String converStr="";
		if(dateStr==null){
			return converStr;
		}
		
		try {
			Date d=stod.parse(dateStr);
			converStr=dtos.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return converStr;
	}

	//根据所给字符生成特殊的id
	public static String generateId(String str){
		String s1="";
		if(str==null){
			s1="0123456";
			return s1;
		}else{
			str=convertString(str.replace(" ", ""));
		}
		for(int i=0;i<str.length();i++){
			if(isChinese(""+str.charAt(i))){
				s1+=Integer.toHexString(str.charAt(i)&0xffff).substring(0,2);
			}else{
				//中文空格
				if(isNum(str.charAt(i)+"")||isEglish(str.charAt(i)+"")){
					s1+=""+str.charAt(i);
				}else{
					String code=Integer.toHexString(str.charAt(i)&0xffff);
					if(code.length()>2){
						s1+=Integer.toHexString(str.charAt(i)&0xffff).substring(0,2);
					}else{
						s1+=Integer.toHexString(str.charAt(i)&0xffff);
					}
				}
			}
			
		}
		if(s1.length()>100){
			s1= s1.replace("", "").substring(0,99);
//			System.out.println("id==="+s1+"  ====="+s1.indexOf(""));
			return s1;
		}
		return s1;
	}

	//判断是不是身份证
	public static boolean isId(String id){
		Pattern pattern = Pattern.compile("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)");
		Matcher matcher = pattern.matcher(id);
		return matcher.matches();
	}


	/**
	 * 匹配 0% - 100% 的整数 百分数
	 * @param percent
	 * @return
	 */
	public static boolean isPercent(String percent){
		Pattern pattern = Pattern.compile("(^\\d{1,2}%$)|100%");
		Matcher matcher = pattern.matcher(percent);
		return matcher.matches();
	}

}
