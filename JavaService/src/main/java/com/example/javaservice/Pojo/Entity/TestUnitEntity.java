package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.TestUnitConstant;
import lombok.Data;

@Data
public class TestUnitEntity implements java.io.Serializable{
    private Integer state;
    private String name;
    private String input;
    private String response;
    private Integer targetId;
    private String targetName;
    private Integer checkState; // 是否检查状态
    @Override
    public String toString() {
        return "{" +
                "状态ID=" + state +
                ", 状态名称='" + name + '\'' +
                ", 输入='" + input + '\'' +
                ", 响应='" + response + '\'' +
                ", 跳转ID=" + targetId +
                ", 跳转状态名称='" + targetName + '\'' +
                ", 检查状态=" + TestUnitConstant.getTestResultString(checkState) +
                '}';
    }
}
