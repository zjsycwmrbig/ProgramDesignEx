package com.example.javaservice.Pojo.Entity;

import lombok.Data;

@Data
public class CompileWarning {
    private String msg; // 哪一行出现问题
    private int warningType;

    public CompileWarning(String msg, Integer warningType) {
        this.msg = msg;
        this.warningType = warningType;
    }
}
