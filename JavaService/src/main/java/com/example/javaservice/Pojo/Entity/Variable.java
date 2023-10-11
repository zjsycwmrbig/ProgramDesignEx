package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.util.List;

@Data
public class Variable {

    private int length = 0;
    List<VariableSlice> variableSliceList;


    public Variable() {
        variableSliceList = new java.util.ArrayList<>();
    }

    public Variable addVariable(VariableSlice variableSlice){
        variableSliceList.add(variableSlice);
        length++;
        return this;
    }
    // 找到这个index之前的最后一个变量，作为
    public VariableSlice getVariableSlice(int index){
        for(int i = variableSliceList.size() - 1;i >=0 ;i--){
            if(variableSliceList.get(i).getIndex() <= index){
                return variableSliceList.get(i);
            }
        }
        // 可能找不到
        return null;
    }
}
