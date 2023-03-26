package com.example.demo.web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler({Throwable.class})
    public String handleSqlException(Throwable e){
        return "error";
    }
}
