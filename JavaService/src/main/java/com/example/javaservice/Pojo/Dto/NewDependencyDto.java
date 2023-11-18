package com.example.javaservice.Pojo.Dto;

import lombok.Data;

@Data
public class NewDependencyDto {
    Integer id;
    String code;
    String name;
    String description;
    Boolean suggestion_when_check;
    Boolean suggestion_when_pass;
}
