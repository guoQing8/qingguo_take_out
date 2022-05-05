package com.superli.qingguo.common;

/**
 * @Author superli
 * @Description 基于ThreadLocal封装工具类
 * @Date 2022/4/30 19:43
 */
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
