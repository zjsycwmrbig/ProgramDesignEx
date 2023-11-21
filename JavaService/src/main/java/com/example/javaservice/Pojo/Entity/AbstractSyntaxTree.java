package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.AbstractSyntaxConstant;
import com.example.javaservice.Constant.SystemConstant;
import lombok.Data;

import java.io.FileOutputStream;

@Data
public class AbstractSyntaxTree {
    public AbstractSyntaxNode root;

    public AbstractSyntaxTree() {
        root = new AbstractSyntaxNode(AbstractSyntaxConstant.BEGIN_NODE, "root");
    }

    public void logPrint(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SystemConstant.SYNTAX_ANALYSIS_PATH);
            fileOutputStream.write("AbstractSyntaxTree:\n".getBytes());
            root.logPrint(fileOutputStream,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
