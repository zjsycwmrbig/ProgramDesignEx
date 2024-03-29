package com.example.javaservice.Constant;

public class CompileErrorConstant {

    public static final String SEMANTIC_IDENTIFIER_SCAN_AST = "AST语法树错误，无法继续进行语义分析（正常情况下看不到该条错误）";
    public static final String SEMANTIC_IDENTIFIER_STATE_REPEAT = "状态定义重复";
    public static final String SEMANTIC_VARIABLE_NOT_FOUND = "出现未标识变量";
    public static final String SEMANTIC_VARIABLE_LOCATION_ERROR = "变量有标识，但变量位置错误,请尽量不要使用一个变量赋值多次的情况!";
    public static final String SEMANTIC_VARIABLE_DEPENDENCY_LOOP = "变量依赖循环,请重新检查代码逻辑!";
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
    public static final String STATE_MAP_ERROR = "状态映射结构错误";
    public static final String THERE_ARE_NOT_ONE_DEFAULT = "不只存在一个默认状态";
    public static final String THERE_ARE_NOT_ONE_WAIT = "不只存在一个超时响应";
    public static final String TIME_ERROR = "时间定义错误";

    public static final String ILLEGAL_CHAR = "代码中存在非法字符";
    public static final String ILLEGAL_IDENTIFIER = "在尝试将词法识别为标识符时，发现错误";
    public static final String ILLEGAL_NUMBER = "在尝试将词法识别为数字时，发现错误";
    public static final String ILLEGAL_STRING = "在尝试将词法识别为字符串时，发现错误";
    public static final String ILLEGAL_MATCHER = "在尝试将词法识别为匹配符时，发现错误";
    public static final String ILLEGAL_CODE = "非法编码，请检查代码编码格式";

    public static final String UNKNOW_SYNTAX_ERROR = "未知语法分析错误";
    public static final String FAIL_IN_SYNTAX = "在识别变量和状态时，未能成功";

    public static final String SYNTAX_LESS_TOKEN = "语句不完整，请重新检查";
    public static final String SYNTAX_VARIABLE_FIRST_NOT_IDENTIFIER = "识别变量时，第一个词法单元不是标识符";
    public static final String SYNTAX_VARIABLE_SEC_NOT_EQUAL = "识别变量时，第二个词法单元不是等号";
    public static final String SYNTAX_VARIABLE_RESULT_FAIL = "识别变量时，变量的值分析失败";
    public static final String SYNTAX_STATE_FIRST_NOT_IDENTIFIER = "识别状态时，未识别到标识符";
    public static final String SYNTAX_STATE_SEC_NOT_LEFT_BRACE = "识别状态时，未识别到左大括号,请检查是否有遗漏";
    public static final String SYNTAX_STATE_LOGIC_LESS = "识别状态时，未识别到逻辑表达式,或者逻辑表达式不完整";
    public static final String SYNTAX_STATE_UNFINISHED = "识别状态时，未识别到右大括号,状态定义不完整";
    public static final String SYNTAX_RESULT_UNILLEGAL = "识别结果时，未识别到合法的结果表达式";
    public static final String SYNTAX_RESULT_UNKNOW_ERROR = "识别结果时，未知错误";
    public static final String SYNTAX_RESULT_ADD_NOAFTER = "识别结果时，未识别到加号后的表达式";
    public static final String SYNTAX_LOGIC_UNILLEGAL = "识别逻辑表达式时，未识别到合法的逻辑表达式";
    public static final String SYNTAX_LOGIC_INPUT_UNFINISHED = "识别逻辑表达式时，未识别到正确的输入表达式";
    public static final String SYNTAX_LOGIC_WAIT_UNFINISHED = "识别逻辑表达式时，未识别到正确的等待表达方式";
    public static final String SYNTAX_LOGIC_ILLEGAL_KEYWORD = "识别逻辑表达式时，识别到非法关键字";
    public static final String SYNTAX_LOGIC_NO_ENOUGH = "识别逻辑表达式时，不能识别到完整的逻辑表达式";
    public static final String SYNTAX_RESULT_LESS_TOKEN = "识别结果时，不能识别到完整的结果表达式";
    public static final String SYNTAX_FUNCTION_NO_ENOUGH = "识别函数时，不能识别到完整的函数表达式";
    public static final String SYNTAX_FUNCTION_NOT_FUNCTION = "识别函数时，不能识别到函数名";
    public static final String SYNTAX_FUNCTION_NO_LEFT_BRACKET = "识别函数时，不能识别到左括号";
    public static final String SYNTAX_FUNCTION_NO_PARAMETER = "识别函数时，不能识别到函数参数";
    public static final String SYNTAX_FUNCTION_NO_RIGHT_BRACKET = "识别函数时，不能识别到右括号";
    public static final String SYNTAX_PARAMETER_NO_ENOUGH = "识别函数参数时，不能识别到完整的参数表达式";
    public static final String SYNTAX_PARAMETER_NOT_PARAMETER = "识别函数参数时，不能识别到参数符号";
    public static final String SYNTAX_PARAMETER_NOT_IDENTIFIER = "识别函数参数时，不能识别到参数标识符";
    public static final String SYNTAX_PARAMETER_NO_EQUAL = "识别函数参数时，不能识别到参数赋值符号";
    public static final String SYNTAX_PARAMETER_NO_RESULT = "识别函数参数时，不能识别到参数赋值结果";
    public static final String SYNTAX_PARAMETER_NO_PARAMETER = "识别函数参数时，存在参数分隔符,但是参数不完整";
    public static final String SYNTAX_INPUT_NO_ENOUGH = "识别输入表达式时，不能识别到完整的输入表达式";
    public static final String SYNTAX_INPUT_NO_FLAG = "识别输入表达式时，不能识别到输入分隔";
    public static final String SYNTAX_INPUT_NO_DEFINITION = "识别输入表达式时，不能识别到输入定义";
    public static final String SYNTAX_INPUT_ILLEGAL = "识别输入表达式时，不能识别到合法的输入表达式";
    public static final String SYNTAX_INPUT_TEMP_ERROR = "识别输入表达式时，输入格式错误";
    public static final String SYNTAX_LOGIC_GOTO_ERROR = "识别逻辑表达式时，不能识别到合法的跳转表达式";
    public static final String SYNTAX_LOGIC_NO_COLON = "识别逻辑表达式时，不能识别到冒号";
    public static final String SYNTAX_LOGIC_NO_RETURN = "识别逻辑表达式时，不能识别到返回表达式";
    public static final String SYNTAX_LOGIC_ILLEGAL_RESULT = "识别逻辑表达式时，不能识别到合法的返回表达式";
    public static final String THERE_ARE_NOT_ONE_BEGIN = "存在多个欢迎语句";
    public static final String SEMANTIC_IDENTIFIER_SCAN_FAIL = "语义分析时，扫描标识符失败";
    public static final String SEMANTIC_LOGIC_REDEFINE = "语义分析时，分析该状态，发现一个状态下的逻辑表达式中存在多个歧义匹配符！";

}
