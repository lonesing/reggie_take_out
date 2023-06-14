package com.reggie.common;

/**
 * @author Lyx
 * @date 2023-06-09-21:27
 * @description 封装ThreadLocal的操作
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    //每个ThreadLocal实例在每个线程中只会存储一个值，因此调用get()方法时，会获取到自己线程对应的值
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
