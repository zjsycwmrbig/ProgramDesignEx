package com.example.javaservice.Core;

import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Pojo.Entity.Token;
import com.example.javaservice.Pojo.Entity.Tokens;
import com.example.javaservice.Result.Result;

import java.util.List;

public interface SyntaxAnalyzer {
    /**
     * 语法分析 返回ast树
     */
    public AbstractSyntaxTree analyze(List<Token> tokens);

    // 递归调用分析

    /**
     * 识别变量声明,识别不出来就返回null
     */
    public Result variableDeclaration(List<Token> tokens);


    /**
     * 识别状态声明,识别不出来就返回null
     */
    public Result stateDeclaration(List<Token> tokens);


    /**
     * 识别结果表示
     */
    public Result resultRepresentation(List<Token> tokens);

    /**
     * 识别逻辑定义
     */
    public Result logicDefinition(List<Token> tokens);

    /**
     * 识别函数使用
     */
    public Result functionUse(List<Token> tokens);

    /**
     * 识别参数表示
     */
    public Result parameterRepresentation(List<Token> tokens);

    /**
     * 识别输入模板
     */
    public Result inputTemplate(List<Token> tokens);

    /**
     * 识别输入定义，需要递归实现
     */
    public Result inputDefinition(List<Token> tokens);
}
