package com.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Lyx
 * @date 2023-06-09-3:06
 * @description 全局异常处理
 */

/*
    @ControllerAdvice：使用@ControllerAdvice注解标记为异常处理器，它的作用是捕获处理异常。
    它的参数可以是注解类型，表示捕获使用了指定注解的类出现的异常。

    @RestControllerAdvice：使用@RestControllerAdvice注解标记为异常处理器，该注解相较于上一个包含了@ResponseBody的功能。
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // 该注解用于标识该方法用于处理指定的异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
