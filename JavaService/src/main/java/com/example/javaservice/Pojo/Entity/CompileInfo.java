package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.util.List;

@Data
public class CompileInfo {

    private List<CompileWarning> compileWarnings;
    private List<CompileError> compileError;
    public CompileInfo() {
        compileWarnings = new java.util.ArrayList<>();
    }

    public void addCompileWarning(String msg, Integer warningType) {
        compileWarnings.add(new CompileWarning(msg, warningType));
    }

    public void addCompileError(Integer location, Integer errorType) {
        compileError.add(new CompileError(location, errorType));
    }

}
