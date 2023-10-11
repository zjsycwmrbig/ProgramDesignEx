package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.SemanticAnalysisConstant;
import lombok.Data;

@Data
public class CompileResult implements java.io.Serializable{
    private Integer type;
    private String value;
    private Object data;

    public CompileResult(Integer type, String value, Object data) {
        this.type = type;
        this.value = value;
        this.data = data;
    }

    @Override
    public String toString() {
        return "CompileResult{" +
                "type=" + SemanticAnalysisConstant.getResultType(type) +
                ", value='" + value + '\'' +
                ", data=" + data +
                '}';
    }
}
