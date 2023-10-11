package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.*;
import com.example.javaservice.Exception.CompileErrorException;
import com.example.javaservice.Pojo.Entity.*;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.FunctionCompile;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service

public class SemanticAnalyzerImpl implements com.example.javaservice.Core.SemanticAnalyzer{

    // TODO 这里的 id 先不赋值，等到analyse返回后再给id赋值

    // 这个是生成的默认状态
    private Integer defaultState;

    List<CompileWarning> compileWarnings;

    // 这个是生成的结果字典
    private ResultDictionary resultDictionary;
    private Map<String,Integer> resultMap; // 主要是看是否有重复的结果可以利用

    Map<String, Variable> variableMap;
    Map<String,State> stateMap;

    private List<Integer> globalState; // 全局状态集合

    // 这个是生成的转换表
    private Map<Integer, TransferList> transMap;
    private Integer stateIndex; // 递增的ID

    // 构造函数，需要tokens流
    public SemanticAnalyzerImpl(){
        defaultState = -1;
        compileWarnings = new java.util.ArrayList<>();
        resultDictionary = new ResultDictionary();
        resultMap = new java.util.HashMap<>();
        variableMap = new java.util.HashMap<>();
        stateMap = new java.util.HashMap<>();
        stateIndex = 0;
        transMap = new java.util.HashMap<>();
        globalState = new java.util.ArrayList<>();
    }

    @Override
    public Map<String,Object> analyse(AbstractSyntaxTree ast) {
        Result res = null;
        // 先扫描 出现错误 就 直接抛出 理论上不返回 生成映射表
        if((res = identifierScan(ast.getRoot())) != null){

            // 根据是变量还是状态来分析判断
            List<AbstractSyntaxNode> children = ast.getRoot().getChildren();
            for(int i = 0;i < children.size();i++){
                AbstractSyntaxNode child = children.get(i);
                if(child.getType() == AbstractSyntaxConstant.VARIABLE_DEFINE) {
                    // 变量定义

                    if ((variableAnalysis(child,i)) == null) {
                        logPrint();
                        throw new CompileErrorException(CompileErrorConstant.SEMANTIC_ANALYZE_VARIABLE_DEFEAT,child.getLine(),compileWarnings);
                    }
                }else if(child.getType() == AbstractSyntaxConstant.STATE_DEFINE) {

                    // 状态定义
                    if((res = stateAnalysis(child)) == null){
                        throw new CompileErrorException(CompileErrorConstant.SEMANTIC_ANALYZE_STATE_DEFEAT,child.getLine(),compileWarnings);
                    }
                }else{
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,child.getLine(),compileWarnings);
                }
            }
        }else{
            return null;
        }
        // 生成依赖
        RobotDependency dependency = new RobotDependency();
        dependency.setDefaultState(defaultState);
        dependency.setGlobalState(globalState);
        // 处理transMap 中的condition
        transMap = conditionProcessor(transMap);
        dependency.setTransMap(transMap);
        dependency.setResultDictionary(resultDictionary);

        Map<String,Object> complexResult = new java.util.HashMap<>();
        complexResult.put("robotDependency",dependency);
        complexResult.put("compileWarnings",compileWarnings);

        return complexResult;
    }

    @Override
    public Result identifierScan(AbstractSyntaxNode node) {
        if(node.getType() != AbstractSyntaxConstant.BEGIN_NODE){
            // 默认第一行出现这个错误
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,1,compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();

        for(int i = 0;i < children.size();i++){
            AbstractSyntaxNode child = children.get(i);
            if(child.getType() == AbstractSyntaxConstant.VARIABLE_DEFINE){
                if(child.getChildren() == null || child.getChildren().size() == 0){
                    // 变量定义的子节点只能有一个
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,child.getLine(),compileWarnings);
                }
                String identifier = child.getChildren().get(0).getValue();

                // 变量定义
                if(variableMap.containsKey(identifier)){
                    // TODO 这个地方能不能直接get然后put？
                    variableMap.put(identifier,variableMap.get(identifier).addVariable(new VariableSlice(i,child.getType(),SemanticAnalysisConstant.UNKNOWN_RESULT)));
                }else{
                    variableMap.put(identifier,new Variable().addVariable(new VariableSlice(i,child.getType(),SemanticAnalysisConstant.UNKNOWN_RESULT)));
                }
                // 检查是否存在状态定义 已经存在状态定义在先就增加警告
                if(stateMap.containsKey(identifier)){
                    compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_IDENTIFIER_SCAN_V_S_REPEAT,child.getLine()));
                }
            }else if(child.getType() == AbstractSyntaxConstant.STATE_DEFINE){
                if(child.getChildren() == null || child.getChildren().size() < 2){
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,child.getLine(),compileWarnings);
                }
                boolean is_global = child.getChildren().get(0).getValue().equals("global");
                String identifier = null;
                if(is_global){
                    identifier = child.getChildren().get(1).getValue();
                }else{
                    identifier = child.getChildren().get(0).getValue();
                }
                // 状态定义
                if(stateMap.containsKey(identifier)){
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_STATE_REPEAT,child.getLine(),compileWarnings);
                }else{
                    // 判断状态是否是全局状态
                    stateMap.put(identifier,new State(i,is_global));
                    // 在这里直接给状态赋值ID，后面就不用再赋值了
                    stateMap.get(identifier).setId(stateIndex++);
                    if(is_global){
                        // 给global赋值
                        globalState.add(stateIndex-1);
                        // 全局状态
                        if(defaultState == -1){
                            defaultState = stateMap.get(identifier).getId();// 使用id来标识，同时也是状态的唯一标识
                        }else{ // 多重全局状态
                            compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_IDENTIFIER_SCAN_GLOBAL_REPEAT,child.getLine()));
                        }
                    }
                }
                // 检查是否存在变量定义 已经存在变量定义在先就增加警告
                if(variableMap.containsKey(identifier)){
                    compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_IDENTIFIER_SCAN_S_V_REPEAT,child.getLine()));
                }
            } else{
                throw  new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,1,compileWarnings);
            }
        }
        return Result.success();
    }

    // 这里需要提供index，这里的index是父节点的index
    @Override
    public Result variableAnalysis(AbstractSyntaxNode father,int index) {
        int IDENTIFIER = 0;
        int OPERATOR_EQUAL = 1;
        int RESULT_INDEX = 2;
        // 给出的应该都是变量定义节点，我们要把变量对应的resultID给找出来
        if(father.getType() != AbstractSyntaxConstant.VARIABLE_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,1,compileWarnings);
        }
        List<AbstractSyntaxNode> children = father.getChildren();
        // 判断是否存在 标识符
        AbstractSyntaxNode node = children.get(IDENTIFIER);
        if(!variableMap.containsKey(node.getValue())){
            // 不管index都不存在，直接错误抛出
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_NOT_FOUND,node.getLine(),compileWarnings);
        }else{
            // 存在标识符，但是index不知道是否满足
            VariableSlice variableSlice = variableMap.get(node.getValue()).getVariableSlice(index);
            if(variableSlice == null) {
                // 当前index没有合适的变量
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_LOCATION_ERROR,node.getLine(),compileWarnings);
            }else{
                // 对variableSlice进行分析
                if(variableSlice.getResultId() >= 0){
                    // 如果变量对应的已经被计算过了 直接返回结果
                    return new Result(variableSlice.getResultId(),null,null);
                }else if(variableSlice.getResultId() == SemanticAnalysisConstant.COMPUTING_RESULT){
                    // 计算中，说明存在循环依赖
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_DEPENDENCY_LOOP,node.getLine(),compileWarnings);
                }else if(variableSlice.getResultId() == SemanticAnalysisConstant.UNKNOWN_RESULT){
                    // 未定义的结果,需要递归分析出结果,返回的是一个结果ID,这里的index也是父节点的index
                    Result result = resultAnalysis(children.get(RESULT_INDEX),index,null);
                    if(result != null){
                        // 更新 variableMap 把state的值放进去
                        variableMap.get(node.getValue()).getVariableSlice(index).setResultId(result.getState());
                    }else{
                        throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_RESULT_DEFEAT,node.getLine(),compileWarnings);
                    }
                }else{
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_UNBELIEVABLE_ERROR,node.getLine(),compileWarnings);
                }
            }
        }
        // 走到这里一般是成功的
        return Result.success();
    }





    private Result functionAnalysis(AbstractSyntaxNode node,int index,List<String> inputs) {
        if(node.getType() != AbstractSyntaxConstant.FUNCTION_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();
        if(children == null || children.size() < 3){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }

        // 函数名 以及 参数 属性名 - ID
        Map<String,Integer> parameterMap = new java.util.HashMap<>();
        // 我们期望生成一个 map 0 : input0,1 : input1,2 : input2.. 5 : constant5

        int functionNameIndex = 0;
        int parameterIndex = 2;
        if(children.get(functionNameIndex).getType() != TokensConstant.FUNCTION){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        String functionName = children.get(functionNameIndex).getValue();
        if(children.get(parameterIndex).getType() != AbstractSyntaxConstant.PARAMETER_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        // 解析参数
        List<AbstractSyntaxNode> parameterList = children.get(parameterIndex).getChildren();
        if(parameterList == null || parameterList.size() < 2){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }


        // 查看是否存在输入，并且判断是否产生警告
        Map<String,Boolean> inputFlagMap = new java.util.HashMap<>();
        Boolean hasInput = false;
        if(inputs != null && inputs.size() > 0){
            hasInput = true;
            for(int i = 0;i < inputs.size();i++){
                inputFlagMap.put(inputs.get(i),false);
            }
        }

        // 指向的就是标识符，也就是属性值
        for(int i = 1;i < parameterList.size();i+=5){
            AbstractSyntaxNode parameter = parameterList.get(i);
            if(parameter.getType() != TokensConstant.IDENTIFIER){
                // ast错误
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
            }else {
                if(parameterMap.containsKey(parameter.getValue())){
                    // 重复定义
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_FUNCTION_PARAM_REPEAT,node.getLine(),compileWarnings);
                }else{
                    parameterMap.put(parameter.getValue(),null);
                    AbstractSyntaxNode parameterValue = parameterList.get(i+2);// 解析参数
                    if(parameterValue.getType() == TokensConstant.IDENTIFIER){
                        // 判断是否是输入
                        if(inputs.contains(parameterValue.getValue())){ // 是输入的话，直接创建一个结果，返回id
                            // 创建一个新的结果
                            Integer id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.INPUT_VARIABLE_RESULT, parameterValue.getValue(), null));
                            parameterMap.put(parameter.getValue(),id);
                            // 标记输入已经被使用
                            inputFlagMap.put(parameterValue.getValue(),true);
                        }else{// 不是输入 ， 查询变量映射表
                            if(variableMap.containsKey(parameterValue.getValue()) && variableMap.get(parameterValue.getValue()).getVariableSlice(index) != null){
                                if(variableMap.get(parameterValue.getValue()).getVariableSlice(index).getResultId() >= 0) {
                                    parameterMap.put(parameter.getValue(), variableMap.get(parameterValue.getValue()).getVariableSlice(index).getResultId());
                                }else{
                                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_NOT_COMPUTED,node.getLine(),compileWarnings);
                                }
                            }else{
                                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_NOT_FOUND,node.getLine(),compileWarnings);
                            }
                        }
                    }else if (parameterValue.getType() == TokensConstant.STRING){
                        // 查询结果映射表
                        if(resultMap.containsKey(parameterValue.getValue())){
                            parameterMap.put(parameter.getValue(),resultMap.get(parameterValue.getValue()));
                        }else{
                            String resultString = parameterValue.getValue();
                            resultString = resultString.substring(1,resultString.length()-1);
                            Integer id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.CONSTANT_RESULT,resultString,null));
                            resultMap.put(parameterValue.getValue(),id);
                            parameterMap.put(parameter.getValue(),id);
                        }
                    }else if(parameterValue.getType() == TokensConstant.NUMBER){
                        // 查询结果映射表
                        if(resultMap.containsKey(parameterValue.getValue())){
                            parameterMap.put(parameter.getValue(),resultMap.get(parameterValue.getValue()));
                        }else{
                            Integer id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.CONSTANT_RESULT,parameterValue.getValue(),null));
                            resultMap.put(parameterValue.getValue(),id);
                            parameterMap.put(parameter.getValue(),id);
                        }
                    }else if(parameterValue.getType() == AbstractSyntaxConstant.FUNCTION_DEFINE){
                        // 递归调用
                        Result result = functionAnalysis(parameterValue,index,inputs);
                        if(result == null){
                            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_RESULT_FUNCTION_ERROR,node.getLine(),compileWarnings);
                        }else{
                            // 把结果存起来
                            parameterMap.put(parameter.getValue(),result.getState());
                        }
                    }else if(parameterValue.getType() == AbstractSyntaxConstant.RESULT_DEFINE) {
                        // 递归调用
                        Result result = resultAnalysis(parameterValue, index, inputs);
                        if (result == null) {
                            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_RESULT_FUNCTION_ERROR, node.getLine(), compileWarnings);
                        } else {
                            // 把结果存起来
                            parameterMap.put(parameter.getValue(), result.getState());
                        }
                    }else{
                        logPrint();
                        throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
                    }
                }
            }
        }
        // 检查是否输入和参数相匹配
        if(hasInput){
            for(String input:inputs){
                if(!inputFlagMap.get(input)){
                    compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_INPUT_NOT_USE_ALL,node.getLine()));
                }
            }
        }

        List<String> parameterNameList = new ArrayList<>();

        for(String key:parameterMap.keySet()){
            parameterNameList.add(key);
        }

        // 对于函数的参数，调用系统的工具来检查是否正确
        if(!FunctionCompile.compile(functionName, parameterNameList)){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_FUNCTION_COMPILE_ERROR,node.getLine(),compileWarnings);
        }else{
            // TODO 这里需要把函数的结果也存起来 下面这个map有没有必要？
            // 找到函数的名称
            AbstractSyntaxNode FunctionNameNode = node.getChildren().get(0);
            if(FunctionNameNode.getType() != TokensConstant.FUNCTION){
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
            }
            Integer id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.FUNCTION_RESULT,FunctionNameNode.getValue(),parameterMap));
            resultMap.put(functionName,id);
            return new Result(id,null,null);
        }
    }

    @Override
    public Result stateAnalysis(AbstractSyntaxNode node) {
        if(node.getType() != AbstractSyntaxConstant.STATE_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();
        if(children == null || children.size() < 2){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        boolean is_global = children.get(0).getValue().equals("global");

        String identifier = null;

        if(is_global) {
            identifier = children.get(1).getValue();
        }else{
            identifier = children.get(0).getValue();
        }

        // 获取state状态
        State state = stateMap.get(identifier);
        if(state == null){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_STATE_NOT_FOUND,node.getLine(),compileWarnings);
        }else{
            transMap.put(state.getId(),null); // 初始化一个空的转移列表
        }

        // 分析逻辑

        TransferList logicList = new TransferList();
        for(int i = 0;i < children.size();i++){
            AbstractSyntaxNode child = children.get(i);
            if(child.getType() == AbstractSyntaxConstant.LOGIC_DEFINE) {
                // 分析逻辑 返回Result
                Result result = logicAnalysis(child,state.getIndex());
                if(result == null) {
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST, node.getLine(), compileWarnings);
                }else{
                    // 处理返回的transfer
                    logicList.addTransfer((TransferNode) result.getData());
                }
            }else{
                continue;
            }
        }

        // 填充转移列表
        transMap.put(state.getId(),logicList);
        return Result.success();
    }

    @Override
    public Result logicAnalysis(AbstractSyntaxNode node, int index) {
        // 匹配符 关键字 结果表示
        if(node.getType() != AbstractSyntaxConstant.LOGIC_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();
        if(children == null || children.size() < 3){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        // 目标节点

        // 匹配符和输入符
        AbstractSyntaxNode match_input_Node = children.get(0);
        // RETURN
        AbstractSyntaxNode keyWord_Node = children.get(2);
        // 结果节点
        AbstractSyntaxNode result_Node = children.get(3);
        //
        AbstractSyntaxNode goto_Node = null;
        AbstractSyntaxNode targetState_Node = null;

        if(children.size() > 5){
             goto_Node = children.get(4);
            // targetState
            targetState_Node = children.get(5);
        }

        TransferNode transferNode = new TransferNode();

        // 参数列表填充到Condition里面
        List<String> paramList = null; // 可能是空的

        // 生成 一个 Condition ： type，string
        Result res = null;

        // 处理匹配符相关
        if(match_input_Node.getType() == AbstractSyntaxConstant.INPUT_TEMPLATE){
            // 输入 返回Condition外，还需要返回标识符列表
            if((res = inputAnalysis(match_input_Node)) == null ){
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_INPUT_ERROR,match_input_Node.getLine(),compileWarnings);
            }else{
                transferNode.setCondition((Condition) res.getData());
                // 遍历Condition
                List<String> regex = ((Condition) res.getData()).getREGEX();

                paramList = new ArrayList<>(); // 这里才给出一个初始化，其余的情况下是空的

                for(int i = 0;i < regex.size();i++){
                    if(regex.get(i).charAt(0) != '"') { // 说明是字符串
                        paramList.add(regex.get(i));
                    }
                }
            }
        }else if(match_input_Node.getType() == TokensConstant.MATCHMAKER) {
            // 匹配
            if((res = matchAnalysis(match_input_Node)) == null ){
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_MATCH_ERROR,match_input_Node.getLine(),compileWarnings);
            }else{
                // 生成一个条件转移节点，后续没有别的处理
                     transferNode.setCondition((Condition) res.getData());
            }
        }else{
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,match_input_Node.getLine(),compileWarnings);
        }

        // 获得结果ID
        Result result = resultAnalysis(result_Node,index,paramList);
        if(result == null){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_LOGIC_RESULT_FAIL,result_Node.getLine(),compileWarnings);
        }else{

            // 设定状态
            transferNode.setResultID(result.getState());
        }
        // 处理关键字 goto 这里需要递归定义状态
        if(goto_Node != null){
            // 存在GOTO关键字 进行状态转移处理
            if(goto_Node.getValue().equals("goto")) {
                // 递归定义状态
                if (targetState_Node == null) {
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST, keyWord_Node.getLine(), compileWarnings);
                }
                // 一定是一个标识符
                if(stateMap.get(targetState_Node.getValue()) == null) {
                    // 说明还没有定义
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_STATE_NOT_FOUND, targetState_Node.getLine(), compileWarnings);
                }else{
                    transferNode.setTargetState(stateMap.get(targetState_Node.getValue()).getId());
                }
            }
        }

        // 这里的transferNode已经填充完毕
        return Result.success(transferNode);
    }

    @Override
    public Map<Integer, TransferList> conditionProcessor(Map<Integer, TransferList> transMap) {
        // 遍历所有的转移列表
        for(Map.Entry<Integer,TransferList> entry : transMap.entrySet()){
            // 遍历每一个转移列表
            List<TransferNode> transferNodeList = entry.getValue().getTransferList();
            for(TransferNode transferNode : transferNodeList){
                // 遍历每一个转移节点
                Condition condition = transferNode.getCondition();
                // 遍历每一个条件
                List<String> regex = condition.getREGEX();
                if(regex == null || regex.size() == 0){
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_CONDITION_ERROR,0,compileWarnings);
                }
                if(condition.getType() == ConditionConstant.JUDGE){
                    Pattern pattern = Pattern.compile(regex.get(0));
                    condition.setPattern(pattern);
                }else if(condition.getType() == ConditionConstant.INPUT){
                    List<String> params = new ArrayList<>();
                    // 装配
                    String regexStr = "";
                    for(int i = 0;i < regex.size();i++){
                        String subStr = regex.get(i);
                        if(subStr.charAt(0) == '"'){
                            subStr = subStr.substring(1,subStr.length() - 1);
                            regexStr += subStr;
                        }else{
                            // 字符串
                            regexStr += "(.*)";
                            params.add(subStr); // 标识一下参数列表
                        }
                    }
                    Pattern pattern = Pattern.compile(regexStr);
                    condition.setPattern(pattern);
                    condition.setParams(params);
                }else{
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_CONDITION_ERROR,0,compileWarnings);
                }
            }
        }
        return transMap;
    }

    @Override
    public Result inputAnalysis(AbstractSyntaxNode node) {
        // 目的是返回两个东西，一个是Condition，一个是标识符列表 自定义

        // 把标识符填充到Condition中，并且做出区别处理
        if(node.getType() != AbstractSyntaxConstant.INPUT_TEMPLATE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();
        if(children == null || children.size() < 2){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }

        List<String> identifierList = new ArrayList<>();
        Map<String,Boolean> identifierMap = new HashMap<>();
        // 对于是标识符的我们也要使用一个填充
        List<String> regexList = new ArrayList<>();

        // 我们一般要求至少有一个字符串，并且不能出现两个标识符连接的情况
        boolean last_is_identifier = false;
        AbstractSyntaxNode paramNode = null;
        if(children != null && children.size() > 2){
            paramNode = children.get(1);
        }
        if(paramNode == null){
            // 说明没有参数
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        children = paramNode.getChildren();
        if(children == null || children.size() == 0){
            // 说明没有参数
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }

        for(int i = 0;i < children.size();i++){
            AbstractSyntaxNode child = children.get(i);
            if(child.getType() == TokensConstant.STRING) {
                last_is_identifier = false;
                // String 创建一个结果
                String regex = child.getValue();
                if(regex.length() < 3){
                    // 这是一个错误的字符串，也就是“”这种
                    compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_INPUT_STRING_WARING,child.getLine()));
                }
                // 这里不处理两端的字符串，用来表示这个是个常量，是需要匹配的值，不是标识符
                regexList.add(regex);
            }else if(child.getType() == TokensConstant.IDENTIFIER){
                if(last_is_identifier){ // 增加警告
                    compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_INPUT_IDENTIFIER_WARING,child.getLine()));
                }
                last_is_identifier = true;
                // IDENTIFIER 增加一个参数，保证不会出现重复的标识符
                if(identifierMap.containsKey(child.getValue())) {
                    // 重复了
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_INPUT_IDENTIFIER_REPEAT, child.getLine(), compileWarnings);
                }else{
                    // Condition中的标识符列表
                    identifierList.add(child.getValue());
                    identifierMap.put(child.getValue(),true);
                    // 上面的主要用来看重复等问题，下面的主要用来填充
                    regexList.add(child.getValue());// 使用这个占位
                }
            } else if (child.getType() == TokensConstant.DELIMITER && child.getValue().equals(".")){
                continue;
            } else if(child.getType() == TokensConstant.INPUT_FLAG){
                continue;
            } else {
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,child.getLine(),compileWarnings);
            }
        }
        Condition condition;
        // 生成一个Condition 和 一个标识符列表
        if(identifierList.size() == 0){
            // 没有输入，相当于使用输入符号识别了一个JUDGE类型的
            condition = new Condition(ConditionConstant.JUDGE,regexList,0);
        }else if(!(2*identifierList.size() + 1 == regexList.size() || 2*identifierList.size() -1 == regexList.size())){
            // 只有一个输入，相当于使用输入符号识别了一个JUDGE类型的
            condition = new Condition(ConditionConstant.INPUT,regexList,identifierList.size());
            compileWarnings.add(new CompileWarning(CompileWarningConstant.SEMANTIC_INPUT_NORMAL_WARING,node.getLine()));
        }else {
            // 正常的输入
            condition = new Condition(ConditionConstant.INPUT,regexList,identifierList.size());
        }
        return new Result(1,condition,null);
    }

    @Override
    public Result resultAnalysis(AbstractSyntaxNode node, Integer index, List<String> params) {

        if(node.getType() != AbstractSyntaxConstant.RESULT_DEFINE){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<AbstractSyntaxNode> children = node.getChildren();
        if(children == null){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }

        // 结果列表
        List<Integer> resultList = new java.util.ArrayList<>();
        for(int i = 0;i < children.size();i++){
            AbstractSyntaxNode child = children.get(i);
            // String, NUMBER, IDENTIFIER,FUNCTION_USE
            if(child.getType() == TokensConstant.IDENTIFIER){
                // IDENTIFIER 涉及递归
                if(variableMap.containsKey(child.getValue()) && variableMap.get(child.getValue()).getVariableSlice(index) != null){// 变量已经定义 理论上必须定义
                    // 变量已经定义，并且求解成功
                    if(variableMap.get( child.getValue()).getVariableSlice(index).getResultId() >= 0) {
                        // 变量是结果类型,添加到结果列表中
                        resultList.add(variableMap.get(child.getValue()).getVariableSlice(index).getResultId());
                    }else{
                        throw new CompileErrorException(CompileErrorConstant.SEMANTIC_RESULT_IDENTIFIER_NOT_SOLVED,child.getLine(),compileWarnings);
                    }
                }
                // TODO 存在未定义变量，但是params中含有这个变量
                else if(params.contains(child.getValue())){ // 理论上只要需要的在params里面就可以实现
                    // 代表是一个代填充结果
                    Integer id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.INPUT_VARIABLE_RESULT, child.getValue(), null));
                    resultList.add(id);
                } else {
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_RESULT_IDENTIFIER_NOT_FOUND,child.getLine(),compileWarnings);
                }
            }else if(child.getType() == TokensConstant.STRING){
                // String
                if(resultMap.containsKey(child.getValue())) {// 结果已经定义
                    resultList.add(resultMap.get(child.getValue()));
                }else{ // 结果未定义,添加到结果字典中
                    String resultString = child.getValue();
                    resultString = resultString.substring(1,resultString.length()-1); // 去掉引号
                    int id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.CONSTANT_RESULT,resultString,null));
                    resultMap.put(child.getValue(),id);
                    resultList.add(id);
                }
            }else if(child.getType() == TokensConstant.NUMBER){
                // Number
                if(resultMap.containsKey(child.getValue())) {// 结果已经定义
                    resultList.add(resultMap.get(child.getValue()));
                }else{ // 结果未定义,添加到结果字典中
                    int id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.CONSTANT_RESULT,child.getValue(),null));
                    resultMap.put(child.getValue(),id);
                    resultList.add(id);
                }
            }else if(child.getType() == AbstractSyntaxConstant.FUNCTION_DEFINE){

                Result res = functionAnalysis(child,index,params);
                if(res != null){
                    resultList.add((Integer) res.getState());
                }else{
                    throw new CompileErrorException(CompileErrorConstant.SEMANTIC_RESULT_FUNCTION_ANALYSIS_ERROR,child.getLine(),compileWarnings);
                }
            } else if(child.getType() == TokensConstant.OPERATOR && child.getValue().equals("+")) {
                continue;
            }else{
                throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
            }
        }
        // 整合resultList
        if(resultList.size() == 0){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_VARIABLE_UNBELIEVABLE_ERROR,node.getLine(),compileWarnings);
        }else if(resultList.size() != 1){ // 创建复合结果 不添加到map映射中
            int id = resultDictionary.addCompileResult(new CompileResult(SemanticAnalysisConstant.COMPLEX_RESULT,null,resultList));
            return new Result(id,null,null);
        }else{ // 单个结果
            return new Result(resultList.get(0),null,null);
        }
    }

    @Override
    public Result matchAnalysis(AbstractSyntaxNode node) {
        if(node.getType() != TokensConstant.MATCHMAKER){
            throw new CompileErrorException(CompileErrorConstant.SEMANTIC_IDENTIFIER_SCAN_AST,node.getLine(),compileWarnings);
        }
        List<String> regexList = new java.util.ArrayList<>();
        String regex = node.getValue();
        Character c = regex.charAt(0);
        regex = regex.substring(1,regex.length()-1);
        if(c == '\''){
            regexList.add(regex);
        }else if(c == '<'){
            regexList.add(".*" + regex + ".*");
        }else if(c == '`'){
            regexList.add(regex);
        }
        //
        Condition condition = new Condition(ConditionConstant.JUDGE,regexList,0);
        // 返回一个condition
        return new Result(1,condition,null);
    }

    public void logPrint(){
        // 将所有的已生成结构打印到文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(SystemConstant.SEMANTIC_ANAYSIS_PATH + "debug.txt");
            PrintStream printStream = new PrintStream(fileOutputStream);
            // 打印stateMap
            printStream.println("stateMap:");
            for(Map.Entry<String,State> entry : stateMap.entrySet()){
                printStream.println(entry.getKey() + " " + entry.getValue().toString());
            }
            printStream.println("=========================================");
            // 打印variableMap
            printStream.println("variableMap:");
            for(Map.Entry<String,Variable> entry : variableMap.entrySet()){
                printStream.println(entry.getKey() + " " + entry.getValue().toString());
            }
            // 打印resultMap
            printStream.println("resultMap:");
            for(Map.Entry<String,Integer> entry : resultMap.entrySet()){
                printStream.println("key:" + entry.getKey() + " " + entry.getValue());
            }
            printStream.println("=========================================");

            // 打印resultDictionary
            printStream.println("resultDictionary:");

            for(int i = 0;;i++){
                CompileResult compileResult = resultDictionary.getCompileResult(i);
                if(compileResult == null){
                    break;
                }
                printStream.println(i + " " + compileResult.toString());
            }
            printStream.println("=========================================");
            // 打印transMap
            printStream.println("transMap:");
            for(Integer key : transMap.keySet()){
                printStream.println("state" + key);
                printStream.print("      ");
                printStream.print("|-");

                TransferList transferList = transMap.get(key);
                printStream.println(transferList.getTransferList().size());

                for(TransferNode transfer : transferList.getTransferList()){
                    printStream.print("                |__");
                    printStream.println(transfer.toString());
                }
            }





            printStream.println("=========================================");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
