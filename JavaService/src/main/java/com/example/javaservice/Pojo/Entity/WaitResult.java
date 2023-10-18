package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WaitResult implements Serializable {
    private Integer waitTime;
    private Integer resultID;
    private Integer targetState;
    public WaitResult(){
        waitTime = -1;
        resultID = -1;
        targetState = -1;
    }

    public WaitResult(Integer waitTime, Integer resultID, Integer targetState) {
        this.waitTime = waitTime;
        this.resultID = resultID;
        this.targetState = targetState;
    }
}
