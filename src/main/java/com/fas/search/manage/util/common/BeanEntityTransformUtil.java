package com.fas.search.manage.util.common;

import com.fas.base.shiro.UserVO;
import com.fas.search.manage.entity.ZsEntityCategory;
import com.fas.search.manage.util.user.UserVOUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 实体转换工具类
 */
public class BeanEntityTransformUtil {
    //创建人
    private static final String CREATOR = "setCreator";
    //修改人
    private static final String UPDATOR = "setUpdator";
    //id
    private static final String ID = "setId";
    //状态
    private static final String ENABLE = "setEnable";

    /**
     * 新增java实体的时候新增上创建人，修改人信息
     * @param bean
     */
    public static void initCreateEntity(Object bean){
        Class<?> clazz = bean.getClass();
        setEntityId(bean,clazz);
        setEntityCreator(bean,clazz);
        setEntityUpdator(bean,clazz);
    }

    /**
     * 初始化javabean逻辑删除时候的修改人，和状态
     * @param entity
     */
    public static void initDeleteEntity(Object entity){
        Class<?> clazz = entity.getClass();
        setEntityUpdator(entity,clazz);
        setEntityEnable(entity,clazz);
    }

    /**
     * 设置实体的id属性
     * @param entity    实体对象
     * @param clazz 实体类对象
     */
    public static void setEntityId(Object entity,Class<?> clazz) {
        if (clazz == null)
            clazz = entity.getClass();
        invokeSetEntityField(clazz,entity,CreateDataUtil.getUUID(),ID);
    }
    /**
     * 设置实体的创建人属性
     * @param entity    实体对象
     * @param clazz 实体类对象
     */
    public static void setEntityCreator(Object entity,Class<?> clazz) {
        if (clazz == null)
            clazz = entity.getClass();
        invokeSetEntityField(clazz,entity, UserVOUtil.getUserID(),CREATOR);
    }
    /**
     * 设置实体的修改人属性
     * @param entity    实体对象
     * @param clazz 实体类对象
     */
    public static void setEntityUpdator(Object entity,Class<?> clazz) {
        if (clazz == null)
            clazz = entity.getClass();
        invokeSetEntityField(clazz,entity, UserVOUtil.getUserID(),UPDATOR);
    }
    /**
     * 设置实体的修改人属性
     * @param entity    实体对象
     * @param clazz 实体类对象
     */
    public static void setEntityEnable(Object entity,Class<?> clazz) {
        if (clazz == null)
            clazz = entity.getClass();
        invokeSetEntityField(clazz,entity, "1",ENABLE);
    }


    /**
     * 通过反射调用对象的指定方法
     * @param clazz 实体类对象
     * @param entity  实体对象
     * @param value 传入参数
     * @param methodName 执行方法
     */
    public static Object invokeSetEntityField(Class<?> clazz,Object entity,String value,String methodName){
        try {
            Method declaredMethod = clazz.getDeclaredMethod(methodName, new Class[]{String.class});
            return declaredMethod.invoke(entity, new Object[]{value});

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
            System.out.println("没有找到"+methodName+"方法");
            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }


}
