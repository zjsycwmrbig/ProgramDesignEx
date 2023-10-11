package com.example.javaservice.Pojo.Dto;

import lombok.Data;

@Data
public class ReceiveData {
    private int robot;
    private String message;
//    后期可以通过增加选择的code编码来实现不同的操作
}
