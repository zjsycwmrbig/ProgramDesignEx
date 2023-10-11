package com.example.javaservice.Core;

import com.example.javaservice.Pojo.Entity.AbstractSyntaxNode;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Result.Result;

/**
 * 语义分析
 */
public interface SemanticAnalysis {

    // 这里返回的warning中的location应该都是token的位置

    /**
     * 语义分析
     * @param ast
     * @return 返回警告和错误信息，以及产生的Dependency(成功了）
     */
    public Result analyse(AbstractSyntaxTree ast);


    // pre处理
    /**
     * 语义分析前的预处理,标识符映射处理
     *
     */
    public Result identifierScan(AbstractSyntaxNode node);


    // 支柱
    /**
     * 变量分析
     * @param node
     * @return 返回警告和错误信息
     */
    public Result variableAnalysis(AbstractSyntaxNode node);

    /**
     * 状态分析 包含
     * @param node
     * @return 返回警告和错误信息
     */
    public Result stateAnalysis(AbstractSyntaxNode node);

    // 分支
    /**
     * 结果分析 非常重要
     * @param node
     * @return 返回警告和错误信息 成功返回 成功的抽象的整合函数以及参数的Result
     */
    public Result resultAnalysis(AbstractSyntaxNode node);

    /**
     * 匹配分析
     * @param node
     * @return 返回警告和错误信息 以及成功返回 Condition
     */
    public Result matchAnalysis(AbstractSyntaxNode node);

    /**
     * 跳转分析
     * @param node
     * @return 返回警告和错误信息 以及成功返回 状态跳转的Integer 这个Integer是状态的id
     */
    public Result gotoAnalysis(AbstractSyntaxNode node);

}
