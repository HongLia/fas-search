package com.fas.search.manage.util.view;

import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;

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
}
