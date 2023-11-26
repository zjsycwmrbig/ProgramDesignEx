package com.example.javaservice.Service.Impl;
import com.example.javaservice.Mapper.TelephoneMapMapper;
import com.example.javaservice.Pojo.SQLDATA.TelephoneMap;

import java.util.Map;

public class FunctionCaller {
    static private TelephoneMapMapper telephoneMapMapper;

    public static void init(TelephoneMapMapper telephoneMapMapper) {
        FunctionCaller.telephoneMapMapper = telephoneMapMapper;
    }

    public static String Caller(String functionName, Map params) {
        switch (functionName) {
            case "Query":
                return Query(params);
            default:
                return null;
        }
    }

    // 二元查询函数
    private static String Query(Map params) {
        // 查看Map中的键
        String tb = (String) params.get("tb");
        String id = (String) params.get("id");
        // 挑选表的名称
        switch (tb){
            case "telephone":
                TelephoneMap res;
                // 得到TelephoneMapMapper
                res = telephoneMapMapper.selectById(id);
                if(res == null) {
                    return "查询结果不存在";
                }
                else {
                    return String.valueOf(res.getValue());
                }
            default:
                return "tb" + tb + "不存在";
        }
    }
}
