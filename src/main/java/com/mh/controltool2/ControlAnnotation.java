package com.mh.controltool2;


import java.lang.annotation.*;

/*
*  @Retention --> 注解要在运行时被成功提取，那么 @Retention(RetentionPolicy.RUNTIME) 是必须的
*/

public class ControlAnnotation {


    /*
    * @Target
    * 设定注解使用范围
    *
    * */

    //前置控制器注解 方便进行扫描
    @Deprecated
    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RestController {}

    @Deprecated
    @Target({ElementType.METHOD,ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestMapping {

        String value();//注册当前的Mapping
//        public String value2() default "aa";//缺省值

    }

    @Deprecated
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PathVariable {

        String value();

    }

    @Deprecated
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestParam {
        String value();
    }



}
