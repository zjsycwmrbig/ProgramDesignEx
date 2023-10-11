package com.example.javaservice.Pojo.Entity;

import java.io.Serializable;
import java.util.List;

public class ResultDictionary implements Serializable {
    private List<CompileResult> compileResults;

    public ResultDictionary(){
        compileResults = new java.util.ArrayList<>();
    }

    public CompileResult getCompileResult(Integer id){
        if(id < 0 || id >= compileResults.size())
            return null;
        return compileResults.get(id);
    }

    public Integer addCompileResult(CompileResult compileResult){
        compileResults.add(compileResult);
        return compileResults.size() - 1;
    }

    public void print(){
        for (CompileResult compileResult : compileResults) {
            System.out.println(compileResult.toString());
        }
    }
}
