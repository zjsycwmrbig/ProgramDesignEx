package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Data
public class RobotDependency implements Serializable {
    private String dependencyId; // 依赖id

    private List<Integer> globalState; // 全局状态集合
    private Integer defaultState; // 默认状态
    private Map<Integer, TransferList> transMap; // 转移函数
    private ResultDictionary resultDictionary; // 结果字典
    private Map<String,Integer> stateMap; // 状态映射
    private Map<Integer,WaitResult> waitResultMap; // 等待结果
    private Map<Integer,TransferNode> defaultResultMap; // 默认结果
    private Map<Integer,TransferNode> HelloMap; // 问好映射
    private Boolean suggestion_when_check; // 是否在check的时候给出建议
    private Boolean suggestion_when_pass; // 是否在pass的时候给出建议

    public RobotDependency() {
        transMap = new java.util.HashMap<>(); // 初始化一下
        defaultState = -1; // 默认没有init
    }
}
