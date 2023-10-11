package com.example.javaservice.Utils;

import java.util.Map;

public class FunctionCaller {
    public static String Caller(String functionName, Map params) {
        switch (functionName) {
            case "Query":
                return Query(params);
            default:
                return null;
        }
    }

    // TODO 这里函数的更改必然需要更改
    public static String Query(Map params) {
        // 查看Map中的键
        String tb = (String) params.get("tb");
        String id = (String) params.get("id");
        // 这里应该调用数据库
        return "Query " + tb + " " + id + " Success";
    }
}
