package com.fas.search.util.view;

import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.vo.PageDataVO;

/**
 * 向前端返回数据处理工具类
 */
public class ReturnDataUtil {

    /**
     * 新增或者修改方法 成功或者失败，向前台返回相应的数据
     * @param i
     * @return
     */
    public static String saveOrUpdateOrDel(int i){
        if(i>0){
            return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",i);
        }
        return FasReturn.getFasReturn(ResultEnum.FAILED,"操作失败",i);
    }


    /**
     * 返回封装json
     * @param o
     * @return
     */
    public static String getData(Object o){
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",o);
    }
    /**
     * 请求异常，返回异常JSON
     * @param msg
     * @return
     */
    public static String getFailData(String msg){
        return FasReturn.getFasReturn(ResultEnum.FAILED,msg);
    }


    /**
     * 返回分页查询数据请求封装json
     * @param count
     * @param o
     * @return
     */
    public static String getPageData(Integer count,Object o){
        return FasReturn.getFasReturn(ResultEnum.SUNCESS,"操作成功!",new PageDataVO(count,o));
    }
}
