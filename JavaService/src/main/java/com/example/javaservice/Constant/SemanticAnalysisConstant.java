package com.example.javaservice.Constant;

public class SemanticAnalysisConstant {
    public static final Integer CONSTANT_RESULT = 1001;
    public static final Integer COMPLEX_RESULT = 1002;
    public static final Integer FUNCTION_RESULT = 1003; // 表明这个输入是通过输入得到的
    public static final Integer INPUT_VARIABLE_RESULT = 1004;

    // 结果ID指向的情况，可以用来表示某个变量是否已经求解成功
    public static final Integer UNKNOWN_RESULT = -1;
    public static final Integer COMPUTING_RESULT = -2; // 正在求解的结果，如果在求解的时候遇到这个ID，说明有循环依赖


    // DEBUG函数
    public static String getResultType(Integer type) {
        switch (type) {
            case 1001:
                return "CONSTANT_RESULT";
            case 1002:
                return "COMPLEX_RESULT";
            case 1003:
                return "INPUT_RESULT";
            case -1:
                return "UNKNOWN_RESULT";
            case -2:
                return "COMPUTING_RESULT";
            default:
                return "UNKNOWN_RESULT";
        }
    }

    public static final Integer UNKNOWN_STATE_ID = -1;
    public static final Integer COMPUTING_STATE_ID = -1;

}
