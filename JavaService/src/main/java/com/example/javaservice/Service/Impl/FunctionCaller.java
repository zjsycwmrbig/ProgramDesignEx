package com.example.javaservice.Service.Impl;
import com.example.javaservice.Mapper.TelephoneMapMapper;

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
        Object res = null;

        // 挑选表的名称
        switch (tb){
            case "telephone":
                // 得到TelephoneMapMapper
                res = telephoneMapMapper.selectById(id).getValue();
                break;
            default:
                return "tb" + tb + "不存在";
        }

        if(res == null) {
            return "Query " + tb + " " + id + " Failed";
        }else{
            return res.toString();
        }
    }
}
