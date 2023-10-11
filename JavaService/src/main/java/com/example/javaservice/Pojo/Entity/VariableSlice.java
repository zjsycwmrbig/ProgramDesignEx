package com.example.javaservice.Pojo.Entity;

import lombok.Data;

@Data
public class VariableSlice {
    private Integer index; // 在ast树中的位置
    private Integer type; // 定义
    private Integer resultId;// 指向结果id，默认是未知的结果，随着对变量的解析，才能赋值

    public VariableSlice(Integer index, Integer type, Integer resultId) {
        this.index = index;
        this.type = type;
        this.resultId = resultId;
    }

    @Override
    public String toString() {
        return "VariableSlice{" +
                "index=" + index +
                ", type=" + type +
                ", resultId=" + resultId +
                '}';
    }
}
