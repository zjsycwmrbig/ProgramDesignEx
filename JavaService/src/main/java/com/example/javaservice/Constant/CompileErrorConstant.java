package com.example.javaservice.Constant;

public class CompileErrorConstant {

    public static final String SEMANTIC_IDENTIFIER_SCAN_AST = "AST语法树错误，无法继续进行语义分析（正常情况下看不到该条错误）";
    public static final String SEMANTIC_IDENTIFIER_STATE_REPEAT = "状态定义重复";
    public static final String SEMANTIC_VARIABLE_NOT_FOUND = "出现未标识变量";
    public static final String SEMANTIC_VARIABLE_LOCATION_ERROR = "变量有标识，但变量位置错误";
    public static final String SEMANTIC_VARIABLE_DEPENDENCY_LOOP = "变量依赖循环";
    public static final String SEMANTIC_VARIABLE_UNBELIEVABLE_ERROR = "出现了逻辑上不存在的错误";
    public static final String SEMANTIC_VARIABLE_NOT_COMPUTED = "依赖变量未求解，请检查是否依赖顺序存在问题";
    public static final String SEMANTIC_VARIABLE_RESULT_DEFEAT = "依赖变量求解失败，请检查是否依赖顺序存在问题";
    public static final String SEMANTIC_ANALYZE_VARIABLE_DEFEAT = "变量求解失败，请检查逻辑等问题";
    public static final String SEMANTIC_STATE_NOT_FOUND = "初始化状态时，找不到对应的状态";
    public static final String SEMANTIC_MATCH_ERROR = "匹配符解析失败";
    public static final String SEMANTIC_INPUT_ERROR = "输入符解析失败";
    public static final String SEMANTIC_INPUT_IDENTIFIER_REPEAT = "输入时，同一个标识符出现两次，错误";
    public static final String SEMANTIC_RESULT_IDENTIFIER_NOT_FOUND = "结果分析时，发现未定义的标识符";
    public static final String SEMANTIC_RESULT_IDENTIFIER_NOT_SOLVED = "结果分析时，发现未求解的标识符";
    public static final String SEMANTIC_FUNCTION_PARAM_REPEAT = "函数使用参数重复";
    public static final String SEMANTIC_RESULT_FUNCTION_ERROR = "解析结果时，出现嵌套的函数调用解析出错";
    public static final String SEMANTIC_FUNCTION_COMPILE_ERROR = "函数编译出错，建议检查函数是否正确";
    public static final String SEMANTIC_RESULT_FUNCTION_ANALYSIS_ERROR = "解析结果时，函数调用解析出错";
    public static final String SEMANTIC_LOGIC_RESULT_FAIL = "逻辑求解时，结果分析失败";
    public static final String SEMANTIC_ANALYZE_STATE_DEFEAT = "逻辑分析失败";
    public static final String SEMANTIC_CONDITION_ERROR = "条件解析失败";
}
