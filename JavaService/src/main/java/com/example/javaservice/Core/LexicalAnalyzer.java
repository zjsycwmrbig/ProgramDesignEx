package com.example.javaservice.Core;

import com.example.javaservice.Pojo.Entity.Tokens;
import com.example.javaservice.Result.Result;

/**
 * 词法分析器
 */
public interface LexicalAnalyzer {
    /**
     * 分析字符流
     */
    public Tokens analyze(String code);

    /**
     * 识别标识符和关键字
     * @param code
     * @return
     */
    public Result identifierMach(String code);

    /**
     * 识别数字
     * @return
     */
    public Result numberMach(String code);

    /**
     * 识别字符串
     */
    public Result stringMach(String code);

    /**
     * 识别操作符
     */
    public Result operatorMach(String code);

    /**
     * 识别匹配符
     */
    public Result matchmakerMach(String code);


    /**
     * 日志打印
     */
    public void logPrint(Tokens tokens);
}
