package com.example.javaservice.Core;

import com.example.javaservice.Pojo.Entity.AbstractSyntaxNode;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Pojo.Entity.RobotDependency;
import com.example.javaservice.Pojo.Entity.TransferList;
import com.example.javaservice.Result.Result;

import java.util.List;
import java.util.Map;

/**
 * 语义分析
 */
public interface SemanticAnalyzer {

    // 这里返回的warning中的location应该都是token的位置

    /**
     * 语义分析
     * @param ast
     * @return 返回警告和错误信息，以及产生的Dependency(成功了）
     */
    public Map<String,Object> analyse(AbstractSyntaxTree ast);


    // pre处理
    /**
     * 语义分析前的预处理,标识符映射处理,更新成两个映射表，并且其中的index指向的在ast根目录处的位置
     *
     */
    public Result identifierScan(AbstractSyntaxNode node);


    // 支柱
    /**
     * 变量分析
     * @param node
     * @return 返回警告和错误信息
     */
    public Result variableAnalysis(AbstractSyntaxNode node,int index);


    /**
     * 状态分析 包含
     * @param node
     * @return 返回警告和错误信息
     */
    public Result stateAnalysis(AbstractSyntaxNode node);

    // 分支


    /**
     * 匹配分析
     * @param node
     * @return 返回警告和错误信息 以及成功返回 Condition
     */
    public Result matchAnalysis(AbstractSyntaxNode node);


    /**
     * 输入分析
     * @param node
     * @return 返回警告和错误信息 以及成功返回 输入的Result
     */
    public Result inputAnalysis(AbstractSyntaxNode node);

    /**
     * 结果分析 plus版本 ，结合变量结果分析和普通的结果分析，通过附加传参的方式提供供给参数
     * @param node 结果节点 index
     * @param index 结果节点在ast中的位置，保证参数依赖的顺序性
     * @params params 针对函数分析附加的参数
     *
     */
    public Result resultAnalysis(AbstractSyntaxNode node, Integer index, List<String> params);

    /**
     * 逻辑
     * @param node
     * @param index
     * @return
     */
    public Result logicAnalysis(AbstractSyntaxNode node,int index);


    /**
     * 生成含有Pattern串的Condition
     *
     */
    public Map<Integer, TransferList> conditionProcessor(Map<Integer,TransferList> transMap);
}
