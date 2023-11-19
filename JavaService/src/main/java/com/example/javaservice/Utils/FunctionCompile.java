package com.example.javaservice.Utils;

import com.example.javaservice.Constant.FunctionCompileConstant;
import com.example.javaservice.Result.Result;

import java.util.List;

public class FunctionCompile {
    // 编译比较函数
    public static Result compile(String functionName, List<String> params){
        if(FunctionCompileConstant.FunctionMap.containsKey(functionName)){
            Integer paramNum = FunctionCompileConstant.FunctionMap.get(functionName);
            List<String> paramList = FunctionCompileConstant.ParamsList.get(paramNum);
            if(paramList.size() != params.size()){
                // 参数数量不匹配
                return Result.error("参数数量不匹配");
            }else{
                // 比较参数 只查看参数的类型
                for(int i = 0; i < paramList.size(); i++){
                    if(!params.contains(paramList.get(i))){
                        // 参数类型不匹配
                        return Result.error("参数类型不匹配");
                    }
                }
                return Result.success();
            }
        }else{
            // 函数不存在
            return Result.error("函数不存在");
        }
    }
}
