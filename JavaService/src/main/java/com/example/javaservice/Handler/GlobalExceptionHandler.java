package com.example.javaservice.Handler;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Exception.CompileErrorException;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CompileErrorException.class)
    public Result errorHandler(HttpServletRequest request, HttpServletResponse response, CompileErrorException e) {
        e.printStackTrace();
        LOG.INFO("捕获到编译错误" + e.errorMsg);
        // 根据ErrorException的信息，返回特定的Error信息
        return new Result(ResultConstant.ERROR,e.warnings,e.line + ":" + e.errorMsg);
    }

    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        System.out.println("未知错误");
        e.printStackTrace();
        return Result.error("未知错误");
    }


    // 捕获 错误异常


}
