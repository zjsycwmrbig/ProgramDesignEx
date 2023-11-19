package com.example.javaservice.Constant;

public class CompileWarningConstant {
        public static final String SEMANTIC_IDENTIFIER_SCAN_V_S_REPEAT = "该变量标识符已经用作状态码,请减少重复使用,以免造成混淆!";
        public static final String SEMANTIC_IDENTIFIER_SCAN_GLOBAL_REPEAT = "已经存在全局状态,请尽量使用一个全局关键字";
        public static final String SEMANTIC_IDENTIFIER_SCAN_S_V_REPEAT = "该标识符已经用作变量名,请减少重复使用";
        public static final String SEMANTIC_INPUT_IDENTIFIER_WARING = "输入符号连续，可能导致无法读取输入，导致状态不可达";
        public static final String SEMANTIC_INPUT_STRING_WARING = "输入字符串无效，可能导致无法读取输入，导致状态不可达";
        public static final String SEMANTIC_INPUT_NORMAL_WARING = "没有按照推荐的交叉方式，进行输入，可能导致无法读取输入成功！";
        public static final String SEMANTIC_INPUT_NOT_USE_ALL = "输入符号没有使用完毕，请移除冗余的输入符号";
        public static final String SEMANTIC_IDENTIFIER_SCAN_MUTI_VARIABLE = "该标识符已经用作变量名,请尽量减少这样的容易造成混淆的用法!";
        public static final String GOTO_SELF = "该转移会跳转到本身状态，该转移无效!";
        public static final String SEMANTIC_ANALYZE_STATE_DEFAULT = "该状态存在default,但是没有设置转移状态，可能导致成为终止僵尸状态！";
        public static final String SEMANTIC_ANALYZE_STATE_UNREACHABLE = "该状态不可达，可能导致成为终止僵尸状态！";
}
