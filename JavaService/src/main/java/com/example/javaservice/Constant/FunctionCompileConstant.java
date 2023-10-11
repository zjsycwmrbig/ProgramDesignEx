package com.example.javaservice.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionCompileConstant {
    // 函数名 - 对应的参数
    public static final Map<String, Integer> FunctionMap = new HashMap<String, Integer>() {{
        put("Query", 0);
    }};

    public static final List<List<String>> ParamsList = List.of(
        List.of("tb","id")
    );

}
