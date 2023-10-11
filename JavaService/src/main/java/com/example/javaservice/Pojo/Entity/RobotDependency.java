package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Data
public class RobotDependency implements Serializable {
    private String dependencyId;

    private List<Integer> globalState; // 全局状态集合
    private Integer defaultState;
    private Map<Integer, TransferList> transMap;
    private ResultDictionary resultDictionary;
    private Map<String,Integer> stateMap;

    public RobotDependency() {
        transMap = new java.util.HashMap<>(); // 初始化一下
        defaultState = -1; // 默认没有init
    }
}
