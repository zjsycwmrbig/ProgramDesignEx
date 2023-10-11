package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.SemanticAnalysisConstant;
import lombok.Data;

@Data
public class State {
    private Integer index;
    private Boolean isGlobal;
    private Integer id;
    public State(Integer index, Boolean isGlobal) {
        this.index = index;
        this.isGlobal = isGlobal;
        id = SemanticAnalysisConstant.UNKNOWN_STATE_ID;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "State{" +
                "index=" + index +
                ", isGlobal=" + isGlobal +
                ", id=" + id +
                '}';
    }
}
