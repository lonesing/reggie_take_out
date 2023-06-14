package com.reggie.common;

/**
 * @author Lyx
 * @date 2023-06-09-23:56
 * @description
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
