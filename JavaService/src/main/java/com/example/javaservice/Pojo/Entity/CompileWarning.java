package com.example.javaservice.Pojo.Entity;

import lombok.Data;

@Data
public class CompileWarning {
    private String msg; // 哪一行出现问题
    private int warningLine;

    public CompileWarning(String msg, Integer warningLine) {
        this.msg = msg;
        this.warningLine = warningLine;
    }
}
