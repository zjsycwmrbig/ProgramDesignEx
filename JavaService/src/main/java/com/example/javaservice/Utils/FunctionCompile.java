package com.example.javaservice.Utils;

import com.example.javaservice.Constant.FunctionCompileConstant;

import java.util.List;

public class FunctionCompile {
    // 编译比较函数
    public static Boolean compile(String functionName, List<String> params){
        if(FunctionCompileConstant.FunctionMap.containsKey(functionName)){
            Integer paramNum = FunctionCompileConstant.FunctionMap.get(functionName);
            List<String> paramList = FunctionCompileConstant.ParamsList.get(paramNum);
            if(paramList.size() != params.size()){
                // 参数数量不匹配
                return false;
            }else{
                // 比较参数 只查看参数的类型
                for(int i = 0; i < paramList.size(); i++){
                    if(!params.contains(paramList.get(i))){
                        return false;
                    }
                }
                return true;
            }
        }else{
            // 函数不存在
            return false;
        }
    }
}
