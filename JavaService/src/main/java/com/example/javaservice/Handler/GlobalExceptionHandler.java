package com.example.javaservice.Handler;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Exception.CompileErrorException;
import com.example.javaservice.Result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = CompileErrorException.class)
    public Result errorHandler(HttpServletRequest request, HttpServletResponse response, CompileErrorException e) {
        e.printStackTrace();
        // 根据ErrorException的信息，返回特定的Error信息
        return new Result(ResultConstant.ERROR,e.warnings,e.line + " " + e.errorMsg);
    }

    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        return Result.error("未知错误");
    }


    // 捕获 错误异常


}
