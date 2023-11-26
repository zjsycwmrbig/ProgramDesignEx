package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.AbstractSyntaxConstant;
import com.example.javaservice.Constant.TokensConstant;
import lombok.Data;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Data
public class AbstractSyntaxNode {
    private Integer tokenIndex; // 标识这个的顺序
    private Integer length; // 这个节点的长度
    private Integer type;
    private String value;
    // 行数，应该是在ast节点生成的时候就确定了，多个节点的时候应该是第一个节点的行数
    private Integer line;
    private List<AbstractSyntaxNode> children;

    public AbstractSyntaxNode(Integer type,String value){
        this.type = type;
        this.value = value;
        // 这种情况下，意味着是ast特有节点
        this.line = -1;
        children = new ArrayList<>();
        tokenIndex = -1;
        length = 1;
    }

    public AbstractSyntaxNode(Integer type,String value,Integer line){
        this.type = type;
        this.value = value;
        // 这种情况下，意味着是ast特有节点
        this.line = line;
        children = new ArrayList<>();
        tokenIndex = -1;
        length = 1;
    }

    public void addChild(AbstractSyntaxNode node){
        children.add(node);
        value += " " + node.getValue();
        if(line == -1){
            line = node.getLine();
        }else{
            line = Math.min(line,node.getLine());
        }
    }

    public void insertChild(AbstractSyntaxNode node) {
        children.add(0,node);
        value = node.getValue() + " " + value;
        if(line == -1){
            line = node.getLine();
        }else{
            line = Math.min(line,node.getLine());
        }
    }

    public void logPrint(FileOutputStream fileOutputStream,int level){
        try {
            for(int i = 0;i < level*10;i++){
                fileOutputStream.write((" ").getBytes());
            }
            String type_name = "";
            if(type < 100){
                type_name = TokensConstant.TOKENS[type];
            }else{
                type_name = AbstractSyntaxConstant.ABSTRACT_SYNTAX_NODE[type - 101];
            }
            fileOutputStream.write(("|__"+"type:" + type_name + " value:" + value +" line:"+ line  +" children:" + children.size() + "\n").getBytes());
            for (AbstractSyntaxNode node : children) {
                node.logPrint(fileOutputStream,level + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
