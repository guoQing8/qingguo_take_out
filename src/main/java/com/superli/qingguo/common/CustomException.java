package com.superli.qingguo.common;

/**
 * @Author superli
 * @Description 自定义业务异常
 * @Date 2022/4/30 23:12
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
