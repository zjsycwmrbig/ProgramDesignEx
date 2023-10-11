package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 转移列表类，一个状态下含有多个转移列表
 */
@Data
public class TransferList implements Serializable {
    private List<TransferNode> transferList;
    public TransferList() {
        this.transferList = new ArrayList<>();
    }

    public Integer addTransfer(TransferNode transferNode) {
        transferList.add(transferNode);
        return transferList.size() - 1;
    }

}
