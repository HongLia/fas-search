package com.fas.search.search.controller.annotation;

import java.lang.annotation.*;

/**
 * 搜索记录日志
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchControllerLog {

    //搜索关键字位置
    int keyIndex() default -1;
/*    //查询主键ID
    int objectIdIndex() default -1;*/
    //数据id位置
    int dataIdIndex() default -1;
    //实体id位置
    int entityIdIndex() default  -1;
    //主题id位置
    int subjectIdIndex() default  -1;
    //操作类型
    String type() default "";
}
