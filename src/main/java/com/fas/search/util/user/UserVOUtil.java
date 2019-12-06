package com.fas.search.util.user;

import com.fas.base.shiro.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserVOUtil {

    /**
     * 获取登陆用户信息
     * @return
     */
    public static UserVO getUserVO(){
        UserVO user = null;
        try {
            //获得用户信息
            Subject subject = SecurityUtils.getSubject();
            user = (UserVO) subject.getPrincipal();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 获取当前登陆用户的用户名
     * @return
     */
    public static String getUserName(){
        return getUserVO().getOPERATORNAME();
    }

    /**
     * 获取当前登陆用户的用户名
     * @return
     */
    public static String getUserID(){
        return getUserVO().getUSERID();
    }


}
