package com.example.javaservice.Pojo.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.javaservice.Pojo.Entity.Condition;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 转移节点类
 * 1. 转移条件
 * 2. 目标状态
 */

@Data
public class TransferNode implements Serializable{
    private Condition condition; // 对应着 <>
    private Integer resultID;
    private Integer targetState; // 对应着 goto
    public TransferNode(){
        condition = null;
        resultID = -1;
        targetState = -1;
    }

    public TransferNode(Condition condition, Integer resultID, Integer targetState) {
        this.condition = condition;
        this.resultID = resultID;
        this.targetState = targetState;
    }

    public Boolean LogicEquals(TransferNode transferNode){
        if(this.condition.equals(transferNode.condition)){
            return true;
        }else{
            return false;
        }
    }

    public Boolean HasGotoState(){
        if(this.targetState == -1){
            return false;
        }else{
            return true;
        }
    }

    public int getGotoState() {
        return targetState;
    }
}
