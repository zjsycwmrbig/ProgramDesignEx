package com.example.javaservice.Pojo.Entity;

import lombok.Data;

@Data
public class CompileError {
    private int location; // 哪一行出现错误
    private int errorType;

    public CompileError(Integer location, Integer errorType) {
        this.location = location;
        this.errorType = errorType;
    }
}
