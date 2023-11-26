package com.example.javaservice.Constant;

public class TestUnitConstant {
    public static final int TEST_RIGHT = 0;
    public static final int TEST_WRONG = 1;
    public static final int TEST_UNKNOWN = 2;

    public static String getTestResultString(int testResult){
        switch (testResult){
            case TEST_RIGHT:
                return "测试通过";
            case TEST_WRONG:
                return "测试失败";
            case TEST_UNKNOWN:
                return "等待核验";
            default:
                return "未知";
        }
    }
}
