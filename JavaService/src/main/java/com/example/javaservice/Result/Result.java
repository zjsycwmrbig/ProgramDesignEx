package com.example.javaservice.Result;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Pojo.Entity.TransferNode;
import lombok.Data;

@Data
public class Result<T> {
    private int state;
    private T data;
    private String msg;

    public Result(int state, T data, String msg) {
        this.state = state;
        this.data = data;
        this.msg = msg;
    }

    public static Result success() {
        return new Result(ResultConstant.SUCCESS, null, null);
    }

    public static Result error() {
        return new Result(ResultConstant.ERROR, null, "服务器出错，请稍后再试");
    }

    public static Result success(Object data, String msg) {
        return new Result(ResultConstant.SUCCESS, data, msg);
    }

    public static Result success(Object data) {
        return new Result(ResultConstant.SUCCESS, data,null);
    }


    public static Result error(String 依赖不存在) {
        return new Result(ResultConstant.ERROR, null, 依赖不存在);
    }
}
