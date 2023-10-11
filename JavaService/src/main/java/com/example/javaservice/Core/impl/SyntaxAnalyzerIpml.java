package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.AbstractSyntaxConstant;
import com.example.javaservice.Constant.TokensConstant;
import com.example.javaservice.Exception.LexiAnalyseException;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxNode;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Pojo.Entity.Token;
import com.example.javaservice.Result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SyntaxAnalyzerIpml implements com.example.javaservice.Core.SyntaxAnalyzer{
    Integer pointer;
    @Override
    public AbstractSyntaxTree analyze(List<Token> tokens) {
        AbstractSyntaxTree ast = new AbstractSyntaxTree();
        pointer = 0;// 顺序识别tokens的指针
        int length = tokens.size();// tokens的长度
        Result res = null;
        while(pointer < length) { //
            if ((res = variableDeclaration(tokens.subList(pointer, length))) != null) {
                ast.getRoot().addChild((AbstractSyntaxNode) res.getData());
                pointer += res.getState();
                continue;
            } else if ((res = stateDeclaration(tokens.subList(pointer, length))) != null) {
                ast.getRoot().addChild((AbstractSyntaxNode) res.getData());
                pointer += res.getState();
                continue;
            } else {
                ast.logPrint();
                throw new LexiAnalyseException("从第" + pointer + "个token开始识别失败！ " + "发生在原始->变量|状态", tokens.get(pointer).lineIndex);
            }
        }
        return ast;
    }

    @Override
    public Result variableDeclaration(List<Token> tokens) {
        // 父节点
        int IDENTIFIER_INDEX = 0;
        int EQUAL_INDEX = 1;
        int RESULT_POINTER = 2;
        // 标识符 = 结果
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.VARIABLE_DEFINE,"");
        if(tokens.get(IDENTIFIER_INDEX).type == TokensConstant.IDENTIFIER) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(IDENTIFIER_INDEX).token,tokens.get(IDENTIFIER_INDEX).lineIndex));
            if(tokens.get(EQUAL_INDEX).type == TokensConstant.OPERATOR && tokens.get(EQUAL_INDEX).token.equals("=")){
                node.addChild(new AbstractSyntaxNode(TokensConstant.OPERATOR, tokens.get(EQUAL_INDEX).token,tokens.get(EQUAL_INDEX).lineIndex));
                // 识别结果
                Result res = null;
                if((res = resultRepresentation(tokens.subList(RESULT_POINTER,tokens.size()))) != null){
                    node.addChild((AbstractSyntaxNode) res.getData());
                    return new Result(res.getState() + 2,node,null);
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public Result stateDeclaration(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.STATE_DEFINE,"");

        int son_length = 0;
        if(tokens.get(0).type == TokensConstant.KEYWORD && tokens.get(0).token.equals("global")){
            node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(0).token,tokens.get(0).lineIndex));
            son_length++;
        }
        if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length++;
            if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals("{")){
                node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                son_length++;
                // 识别逻辑，可能存在很多条逻辑
                Result res = null;
                while((res = logicDefinition(tokens.subList(son_length,tokens.size()))) != null){
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                }

                if(son_length > 3){ // 识别了不止一条逻辑
                    if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals("}")){
                        node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        son_length++;
                        return new Result(son_length,node,null);
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            }else {
                return null;
            }
        }else{
            return null;
        }
    }

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
        }else if(tokens.get(POINTER).type == TokensConstant.NUMBER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.NUMBER, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
            son_length = 1;
        }else if(tokens.get(POINTER).type == TokensConstant.FUNCTION && (res = functionUse(tokens.subList(POINTER,tokens.size()))) != null){
            // 识别成功
            node.addChild((AbstractSyntaxNode) res.getData());
            son_length = res.getState();
        } else if(tokens.get(POINTER).type == TokensConstant.IDENTIFIER){
            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(POINTER).token,tokens.get(POINTER).lineIndex));
            son_length = 1;
        }else{
            return null;
        }
        POINTER = POINTER + son_length;
        // 剪枝
        if (POINTER >= tokens.size()) {
            return null;
        }
        // 写死了是加号
        if(tokens.get(POINTER).type == TokensConstant.OPERATOR && tokens.get(POINTER).token.equals("+")) {
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
                return null;
            }
        }else{
            // 有后续没加号的情况
            return new Result(son_length, node, null);
        }
    }

    @Override
    public Result logicDefinition(List<Token> tokens) {
        int MATCH_INDEX = 0;
        int son_length = 0;
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.LOGIC_DEFINE, "");
        if (tokens.get(MATCH_INDEX).type == TokensConstant.MATCHMAKER) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.MATCHMAKER, tokens.get(MATCH_INDEX).token,tokens.get(MATCH_INDEX).lineIndex));
            son_length++;
        }else if(tokens.get(MATCH_INDEX).type == TokensConstant.INPUT_FLAG){
            // 识别输入
            Result res = null;
            if((res = inputTemplate(tokens.subList(MATCH_INDEX,tokens.size()))) != null) {
                node.addChild((AbstractSyntaxNode) res.getData());
                son_length += res.getState();
            }else{
                return null;
            }
        }else{
            return null;
        }

        // 识别冒号后的内容
        if (tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(":")) {
            node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length++;
            if (tokens.get(son_length).type == TokensConstant.KEYWORD && tokens.get(son_length).token.equals("return")) {
                node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                son_length++;
                // 识别结果
                Result res = null;
                if ((res = resultRepresentation(tokens.subList(son_length, tokens.size()))) != null) {
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                    if(tokens.get(son_length).type == TokensConstant.KEYWORD && tokens.get(son_length).token.equals("goto")){
                        node.addChild(new AbstractSyntaxNode(TokensConstant.KEYWORD, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        son_length++;
                        if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
                            node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                            son_length++;
                            return new Result(son_length, node, null);
                        }else{
                            return null;
                        }
                    }
                    return new Result(son_length, node, null);
                } else {
                    return null;
                }
                // 可选，可能有跳转

            } else {
                return null;
            }
        }else{
            return null;
        }

    }

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
            if (tokens.get(LEFT_BRACKET).type == TokensConstant.DELIMITER && tokens.get(LEFT_BRACKET).token.equals("(")) {
                node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(LEFT_BRACKET).token,tokens.get(LEFT_BRACKET).lineIndex));
                // 识别参数
                son_length++;
                Result res = null;
                if ((res = parameterRepresentation(tokens.subList(PARAMETER_INDEX, tokens.size()))) != null) {
                    node.addChild((AbstractSyntaxNode) res.getData());
                    son_length += res.getState();
                    if (tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(")")) {
                        node.addChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                        return new Result(son_length + 1, node, null);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

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
            if(tokens.get(IDENTIFIER_INDEX).type == TokensConstant.IDENTIFIER){
                node.addChild(new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(IDENTIFIER_INDEX).token,tokens.get(IDENTIFIER_INDEX).lineIndex));
                son_length ++;
                if(tokens.get(EQUAL_INDEX).type == TokensConstant.OPERATOR && tokens.get(EQUAL_INDEX).token.equals("=")){
                    node.addChild(new AbstractSyntaxNode(TokensConstant.OPERATOR, tokens.get(EQUAL_INDEX).token,tokens.get(EQUAL_INDEX).lineIndex));
                    son_length ++;
                    Result res = null;
                    if((res = resultRepresentation(tokens.subList(RESULT_POINTER,tokens.size()))) != null){
                        node.addChild((AbstractSyntaxNode) res.getData());
                        son_length += res.getState();
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else {
            return null;
        }

        // 递归
        if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(",")){
            Result res = null;

            if((res = parameterRepresentation(tokens.subList(son_length + 1,tokens.size()))) != null){
                AbstractSyntaxNode newNode = (AbstractSyntaxNode) res.getData();
                newNode.insertChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                // 一个个加上去
                for(int i = node.getChildren().size() - 1; i >= 0; i--){
                    newNode.insertChild(node.getChildren().get(i));
                }
                return new Result(son_length + 1 + res.getState(),newNode,null);
            }else{
                return null;
            }
        }else{
            return new Result(son_length,node,null);
        }
    }

    @Override
    public Result inputTemplate(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.INPUT_TEMPLATE,"");
        int son_length = 0;
        if(tokens.get(son_length).type == TokensConstant.INPUT_FLAG && tokens.get(son_length).token.equals("[")){
            node.addChild(new AbstractSyntaxNode(TokensConstant.INPUT_FLAG, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
            son_length ++;
            // 递归匹配输入定义式子
            Result res = null;
            if((res = inputDefinition(tokens.subList(son_length,tokens.size()))) != null){
                node.addChild((AbstractSyntaxNode) res.getData());
                son_length += res.getState();
                if(tokens.get(son_length).type == TokensConstant.INPUT_FLAG && tokens.get(son_length).token.equals("]")){
                    node.addChild(new AbstractSyntaxNode(TokensConstant.INPUT_FLAG, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                    return new Result(son_length + 1,node,null);
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public Result inputDefinition(List<Token> tokens) {
        AbstractSyntaxNode node = new AbstractSyntaxNode(AbstractSyntaxConstant.INPUT_DEFINE,"");
        int son_length = 0;
        AbstractSyntaxNode son = null;
        if(tokens.get(son_length).type == TokensConstant.STRING){
            son = new AbstractSyntaxNode(TokensConstant.STRING, tokens.get(son_length).token,tokens.get(son_length).lineIndex);
            son_length ++;
        }else if(tokens.get(son_length).type == TokensConstant.IDENTIFIER){
            son = new AbstractSyntaxNode(TokensConstant.IDENTIFIER, tokens.get(son_length).token,tokens.get(son_length).lineIndex);
            son_length ++;
        }else{
            return null;
        }
        if(tokens.get(son_length).type == TokensConstant.DELIMITER && tokens.get(son_length).token.equals(".")){
            // 还有递归的可能
            Result res = null;
            if((res = inputDefinition(tokens.subList(son_length + 1,tokens.size()))) != null){
                AbstractSyntaxNode newNode = (AbstractSyntaxNode) res.getData();
                newNode.insertChild(new AbstractSyntaxNode(TokensConstant.DELIMITER, tokens.get(son_length).token,tokens.get(son_length).lineIndex));
                newNode.insertChild(son);
                return new Result(son_length + 1 + res.getState(),newNode,null);
            }else{
                // 有.没后续
                return null;
            }
        }else{
            node.addChild(son);
            return new Result(son_length,node,null);
        }

    }
}
