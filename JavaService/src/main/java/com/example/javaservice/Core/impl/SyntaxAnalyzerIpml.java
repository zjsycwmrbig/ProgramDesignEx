package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.AbstractSyntaxConstant;
import com.example.javaservice.Constant.CompileErrorConstant;
import com.example.javaservice.Constant.TokensConstant;
import com.example.javaservice.Exception.SyntaxAnalyseException;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxNode;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Pojo.Entity.Token;
import com.example.javaservice.Pojo.Entity.TokensStream;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SyntaxAnalyzerIpml implements com.example.javaservice.Core.SyntaxAnalyzer{
    Integer pointer; // 目前已经没有问题的指针指向
    Integer tempPointer; // 临时指针指向，指向最新位置
    AbstractSyntaxTree ast;
    TokensStream tokensStream;

    @Override
    public AbstractSyntaxTree analyze(List<Token> tokens) {
        ast = new AbstractSyntaxTree();
        tokensStream = new TokensStream();
        tokensStream.setStream(tokens);

        pointer = 0;// 顺序识别tokens的指针
        tempPointer = 0;// 临时指针
        int length = tokens.size();// tokens的长度
        Result res = null;

        while(pointer < length) {
            // 先尝试识别下一条大语句是是什么类型的
            if(pointer + 1 >= length){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokens.get(pointer).lineIndex);
            }
            if(tokens.get(pointer + 1).token.equals("=") ) {
                // 变量声明
                if((res = variableDeclaration(tokens.subList(pointer, length))) != null) {
                    ast.getRoot().addChild((AbstractSyntaxNode) res.getData());
                    pointer += res.getState();
                    tempPointer = pointer; // 回滚
                }
            }else{
                // 状态声明
                if((res = stateDeclaration(tokens.subList(pointer, length))) != null) {
                    ast.getRoot().addChild((AbstractSyntaxNode) res.getData());
                    pointer += res.getState();
                    tempPointer = pointer; // 回滚
                }
            }
        }
        return ast;
    }

    // 识别类 除了返回结果，就是抛出异常!
    @Override
    public Result variableDeclaration(List<Token> tokens) {
        // 父节点
        int IDENTIFIER_INDEX = 0;
        int EQUAL_INDEX = 1;
        int RESULT_POINTER = 2;

        if(tokens.size() < 3){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
        }

        // 标识符 = 结果
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.VARIABLE_DEFINE,"");
        if(tokens.get(IDENTIFIER_INDEX).type == TokensConstant.IDENTIFIER) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(IDENTIFIER_INDEX).token,tokens.get(IDENTIFIER_INDEX).lineIndex));
            tempPointer++;
            if(tokens.get(EQUAL_INDEX).type == TokensConstant.OPERATOR && tokens.get(EQUAL_INDEX).token.equals("=")){
                tempPointer++;
                node.addChild(new AbstractSyntaxNode(TokensConstant.OPERATOR, tokens.get(EQUAL_INDEX).token,tokens.get(EQUAL_INDEX).lineIndex));
                // 识别结果
                Result res = null;
                if((res = resultRepresentation(tokens.subList(RESULT_POINTER,tokens.size()))) != null){
                    node.addChild((AbstractSyntaxNode) res.getData());
                    return new Result(res.getState() + 2,node,null);
                }else{
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_VARIABLE_RESULT_FAIL,tokensStream.getLine(tempPointer));
                }
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_VARIABLE_SEC_NOT_EQUAL,tokensStream.getLine(tempPointer));
            }
        }else{
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_VARIABLE_FIRST_NOT_IDENTIFIER,tokensStream.getLine(tempPointer));
        }
    }

    // 识别类 除了返回结果，就是抛出异常!
    @Override
    public Result stateDeclaration(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.STATE_DEFINE,"");
        if(tokens.size() == 0){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
        }
        int son_length = 0;
        if(tokens.get(0).type == TokensConstant.KEYWORD && tokens.get(0).token.equals("global")){
            node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(0).token,tokens.get(0).lineIndex));
            son_length++;
            tempPointer++;
        }
        if(son_length >= tokens.size()){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
        }

        if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length++;
            tempPointer++;
            if(son_length >= tokens.size()){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
            }
            if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals("{")){
                node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                son_length++;
                tempPointer++;
                if(son_length >= tokens.size()){
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
                }
                // 识别逻辑，可能存在很多条逻辑
                Result res = null;
                // 存在链式递归的情况
                while((res = logicDefinition(tokens.subList(son_length,tokens.size()))) != null){
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                }

                if(son_length > 3){ // 识别了不止一条逻辑
                    if(son_length >= tokens.size()){
                        throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LESS_TOKEN,tokensStream.getLine(tempPointer));
                    }
                    if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals("}")){
                        node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        son_length++;
                        tempPointer++;
                        return new Result(son_length,node,null);
                    }else{
                        throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_STATE_UNFINISHED,tokensStream.getLine(tempPointer));
                    }
                }else{
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_STATE_LOGIC_LESS,tokensStream.getLine(tempPointer));
                }
            }else {
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_STATE_SEC_NOT_LEFT_BRACE,tokensStream.getLine(tempPointer));
            }
        }else{
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_STATE_FIRST_NOT_IDENTIFIER,tokensStream.getLine(tempPointer));
        }
    }

    // 判别类 在第一层返回结果，第二层返回异常
    @Override
    public Result resultRepresentation(List<Token> tokens) {
        // 自递归
        int POINTER = 0;
        int son_length = 0;
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.RESULT_DEFINE,"");
        Result res = null;

        if(tokens.get(POINTER).type == TokensConstant.STRING ){
            node.addChild(new AbstractSyntaxNode(TokensConstant.STRING, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
            son_length = 1;
            tempPointer++;
        }else if(tokens.get(POINTER).type == TokensConstant.NUMBER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.NUMBER, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
            son_length = 1;
            tempPointer++;
        }else if(tokens.get(POINTER).type == TokensConstant.FUNCTION && (res = functionUse(tokens.subList(POINTER,tokens.size()))) != null){
            // 识别成功
            node.addChild((AbstractSyntaxNode) res.getData());
            son_length = res.getState();
        } else if(tokens.get(POINTER).type == TokensConstant.IDENTIFIER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
            son_length = 1;
            tempPointer++;
        }else{
            return null;
        }

        POINTER = POINTER + son_length;
        // 剪枝
        if (POINTER >= tokens.size()) {
            // 当做识别完成处理
            return new Result(son_length, node, null);
        }
        // 写死了是加号
        if(tokens.get(POINTER).type == TokensConstant.OPERATOR && tokens.get(POINTER).token.equals("+")) {
            tempPointer++;
            // 进入递归调用
            if((res = resultRepresentation(tokens.subList(POINTER + 1, tokens.size()))) != null) {
                // res 当作是已经组合好的结果
                AbstractSyntaxNode newNode = (AbstractSyntaxNode) res.getData();
                newNode.insertChild(new AbstractSyntaxNode(TokensConstant.OPERATOR, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
                // 将当前节点的子节点加入到新节点中
                newNode.insertChild(node.getChildren().get(0));
                return new Result(son_length + 1 + res.getState(), newNode, null);
            }else{
                // 有加号没后续的情况
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_RESULT_ADD_NOAFTER,tokensStream.getLine(tempPointer));
            }
        }else{
            // 有后续没加号的情况
            return new Result(son_length, node, null);
        }
    }

    // 判别类 在第一层返回结果，第二层返回异常
    @Override
    public Result logicDefinition(List<Token> tokens) {
        int MATCH_INDEX = 0;
        int son_length = 0;
        int length = tokens.size();
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.LOGIC_DEFINE, "");
        if(MATCH_INDEX >= length){
            return null;
        }
        if (tokens.get(MATCH_INDEX).type == TokensConstant.MATCHMAKER) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.MATCHMAKER, tokens.get(MATCH_INDEX).token,tokens.get(MATCH_INDEX).lineIndex));
            son_length++;
            tempPointer++;
        }else if(tokens.get(MATCH_INDEX).type == TokensConstant.INPUT_FLAG){
            tempPointer++;
            // 识别输入
            Result res = null;
            if((res = inputTemplate(tokens.subList(MATCH_INDEX,tokens.size()))) != null) {
                node.addChild((AbstractSyntaxNode) res.getData());
                son_length += res.getState();
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_INPUT_UNFINISHED,tokensStream.getLine(tempPointer));
            }
        }else if(tokens.get(MATCH_INDEX).type == TokensConstant.KEYWORD){
            if(tokens.get(MATCH_INDEX).token.equals("wait")){
                tempPointer++;
                // 后面应该有数字
                if(tokens.get(MATCH_INDEX + 1).type == TokensConstant.NUMBER) {
                    tempPointer++;
                    AbstractSyntaxNode child = new AbstractSyntaxNode(AbstractSyntaxConstant.WAIT_DEFINE, "",tokens.get(MATCH_INDEX).lineIndex);
                    child.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(MATCH_INDEX).token,tokens.get(MATCH_INDEX).lineIndex));
                    child.addChild(new AbstractSyntaxNode(TokensConstant.NUMBER, tokens.get(MATCH_INDEX + 1).token,tokens.get(MATCH_INDEX + 1).lineIndex));
                    son_length+=2;
                    node.addChild(child);
                }else{
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_WAIT_UNFINISHED,tokensStream.getLine(tempPointer));
                }
            }else if(tokens.get(MATCH_INDEX).token.equals("default")){
                tempPointer++;
                // 针对wait 和 default
                node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(MATCH_INDEX).token,tokens.get(MATCH_INDEX).lineIndex));
                son_length++;
            }else if(tokens.get(MATCH_INDEX).token.equals("begin")) {
                tempPointer++;
                son_length++;
                node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(MATCH_INDEX).token,tokens.get(MATCH_INDEX).lineIndex));
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_ILLEGAL_KEYWORD,tokensStream.getLine(tempPointer));
            }
        } else{
            // 这里可能真的就不是逻辑定义 ， 返回null 把问题抛给上面判别 ！
            return null;
        }

        if(son_length >= tokens.size()){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_ENOUGH,tokensStream.getLine(tempPointer));
        }

        // 识别冒号后的内容
        if (tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(":")) {

            node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length++;
            if(son_length >= tokens.size()){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_ENOUGH,tokensStream.getLine(tempPointer));
            }
            tempPointer++;
            if (tokens.get(son_length).type == TokensConstant.KEYWORD && tokens.get(son_length).token.equals("return")) {
                node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                son_length++;
                if(son_length >= tokens.size()){
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_ENOUGH,tokensStream.getLine(tempPointer));
                }
                tempPointer++;
                // 识别结果
                Result res = null;
                if ((res = resultRepresentation(tokens.subList(son_length, tokens.size()))) != null) {
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                    if(son_length >= tokens.size()){
                        // 识别到最后一个token 返回已有的结果
                        return new Result(son_length, node, null);
                    }
                    if(tokens.get(son_length).type == TokensConstant.KEYWORD && tokens.get(son_length).token.equals("goto")){
                        node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        son_length++;
                        if(son_length >= tokens.size()){
                            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_ENOUGH,tokensStream.getLine(tempPointer));
                        }
                        tempPointer++;
                        if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
                            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                            son_length++;
                            return new Result(son_length, node, null);
                        }else{
                            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_GOTO_ERROR,tokensStream.getLine(tempPointer));
                        }
                    }
                    return new Result(son_length, node, null);
                } else {
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_ILLEGAL_RESULT,tokensStream.getLine(tempPointer));
                }
                // 可选，可能有跳转

            } else {
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_RETURN,tokensStream.getLine(tempPointer));
            }
        }else{
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_LOGIC_NO_COLON,tokensStream.getLine(tempPointer));
        }

    }

    // 判别类 在第一层返回结果，第二层返回异常
    @Override
    public Result functionUse(List<Token> tokens) {
        int FUNCTION_NAME = 0;
        int LEFT_BRACKET = 1;
        int PARAMETER_INDEX = 2;
        int son_length = 0;
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.FUNCTION_DEFINE,"");

        if(tokens.get(FUNCTION_NAME).type == TokensConstant.FUNCTION) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.FUNCTION, tokens.get(FUNCTION_NAME).token,tokens.get(FUNCTION_NAME).lineIndex));
            son_length ++;
            tempPointer++;
            if (tokens.get(LEFT_BRACKET).type == TokensConstant.DELIMITER && tokens.get(LEFT_BRACKET).token.equals("(")) {
                node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(LEFT_BRACKET).token,tokens.get(LEFT_BRACKET).lineIndex));
                // 识别参数
                son_length++;
                tempPointer++;
                Result res = null;
                if ((res = parameterRepresentation(tokens.subList(PARAMETER_INDEX, tokens.size()))) != null) {
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                    if(son_length>=tokens.size()){
                        throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_FUNCTION_NO_ENOUGH,tokensStream.getLine(tempPointer));
                    }
                    if (tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(")")) {
                        node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        return new Result(son_length + 1, node, null);
                    } else {
                        throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_FUNCTION_NO_RIGHT_BRACKET,tokensStream.getLine(tempPointer));
                    }
                } else {
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_FUNCTION_NO_PARAMETER,tokensStream.getLine(tempPointer));
                }
            } else {
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_FUNCTION_NO_LEFT_BRACKET,tokensStream.getLine(tempPointer));
            }
        }else{
            return null;
        }
    }

    // 判别类 在第一层返回结果，第二层返回异常
    @Override
    public Result parameterRepresentation(List<Token> tokens) {
        // 自递归
        int BEGIN_FLAG = 0;
        int IDENTIFIER_INDEX = 1;
        int EQUAL_INDEX = 2;
        int RESULT_POINTER = 3;
        int son_length = 0;
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.PARAMETER_DEFINE,"");

        if(tokens.get(BEGIN_FLAG).type == TokensConstant.PARAMETER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.PARAMETER, tokens.get(BEGIN_FLAG).token,tokens.get(BEGIN_FLAG).lineIndex));
            son_length ++;
            tempPointer ++;
            if(tokens.get(IDENTIFIER_INDEX).type == TokensConstant.IDENTIFIER){
                node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(IDENTIFIER_INDEX).token,tokens.get(IDENTIFIER_INDEX).lineIndex));
                son_length ++;
                tempPointer ++;
                if(tokens.get(EQUAL_INDEX).type == TokensConstant.OPERATOR && tokens.get(EQUAL_INDEX).token.equals("=")){
                    node.addChild(new AbstractSyntaxNode(TokensConstant.OPERATOR, tokens.get(EQUAL_INDEX).token,tokens.get(EQUAL_INDEX).lineIndex));
                    son_length ++;
                    tempPointer ++;
                    Result res = null;
                    if((res = resultRepresentation(tokens.subList(RESULT_POINTER,tokens.size()))) != null){
                        node.addChild((AbstractSyntaxNode) res.getData());
                        son_length += res.getState();
                    }else{
                        throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NO_RESULT,tokensStream.getLine(tempPointer));
                    }
                }else{
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NO_EQUAL,tokensStream.getLine(tempPointer));
                }
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NOT_IDENTIFIER,tokensStream.getLine(tempPointer));
            }
        }else {
            return null;
        }
        if(son_length >= tokens.size()){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NO_ENOUGH,tokensStream.getLine(tempPointer));
        }
        // 递归
        if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(",")){
            Result res = null;
            tempPointer ++;
            if(son_length + 1 >= tokens.size()){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NO_PARAMETER,tokensStream.getLine(tempPointer));
            }
            if((res = parameterRepresentation(tokens.subList(son_length + 1,tokens.size()))) != null){
                AbstractSyntaxNode newNode = (AbstractSyntaxNode) res.getData();
                newNode.insertChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                // 一个个加上去
                for(int i = node.getChildren().size() - 1; i >= 0; i--){
                    newNode.insertChild(node.getChildren().get(i));
                }
                return new Result(son_length + 1 + res.getState(),newNode,null);
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_PARAMETER_NO_PARAMETER,tokensStream.getLine(tempPointer));
            }
        }else{
            return new Result(son_length,node,null);
        }
    }

    // 识别类
    @Override
    public Result inputTemplate(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.INPUT_TEMPLATE,"");
        int son_length = 0;
        if(tokens.size() == 0){
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_ENOUGH,tokensStream.getLine(tempPointer));
        }
        if(tokens.get(son_length).type == TokensConstant.INPUT_FLAG && tokens.get(son_length).token.equals("[")){
            node.addChild(new AbstractSyntaxNode(TokensConstant.INPUT_FLAG, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length ++;
            tempPointer ++;
            // 递归匹配输入定义式子
            Result res = null;
            if(son_length >= tokens.size()){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_ENOUGH,tokensStream.getLine(tempPointer));
            }
            if((res = inputDefinition(tokens.subList(son_length,tokens.size()))) != null){
                node.addChild((AbstractSyntaxNode) res.getData());
                son_length += res.getState();
                if(son_length >= tokens.size()){
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_ENOUGH,tokensStream.getLine(tempPointer));
                }
                if(tokens.get(son_length).type == TokensConstant.INPUT_FLAG && tokens.get(son_length).token.equals("]")){
                    node.addChild(new AbstractSyntaxNode(TokensConstant.INPUT_FLAG, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                    return new Result(son_length + 1,node,null);
                }else{
                    throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_FLAG,tokensStream.getLine(tempPointer));
                }
            }else{
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_DEFINITION,tokensStream.getLine(tempPointer));
            }
        }else{
            throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_NO_FLAG,tokensStream.getLine(tempPointer));
        }
    }

    // 判别类
    @Override
    public Result inputDefinition(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.INPUT_DEFINE,"");
        int son_length = 0;
        AbstractSyntaxNode son = null;
        if(tokens.get(son_length).type == TokensConstant.STRING){
            son = new AbstractSyntaxNode(TokensConstant.STRING, tokens.get(son_length).token,tokens.get(son_length).lineIndex);
            son_length ++;
            tempPointer ++;
        }else if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
            son = new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex);
            son_length ++;
            tempPointer ++;
        }else{
            return null;
        }

        if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(".")){
            // 还有递归的可能
            Result res = null;
            tempPointer ++;
            if(son_length + 1 >= tokens.size()){
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_TEMP_ERROR,tokensStream.getLine(tempPointer));
            }
            if((res = inputDefinition(tokens.subList(son_length + 1,tokens.size()))) != null){
                AbstractSyntaxNode newNode = (AbstractSyntaxNode) res.getData();
                newNode.insertChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                newNode.insertChild(son);
                return new Result(son_length + 1 + res.getState(),newNode,null);
            }else{
                // 有.没后续
                throw new SyntaxAnalyseException(CompileErrorConstant.SYNTAX_INPUT_TEMP_ERROR,tokensStream.getLine(tempPointer));
            }
        }else{
            node.addChild(son);
            return new Result(son_length,node,null);
        }
    }
}
