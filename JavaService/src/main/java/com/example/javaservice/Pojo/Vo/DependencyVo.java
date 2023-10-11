package com.example.javaservice.Pojo.Vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DependencyVo {
    public DependencyVo(Integer id, String name, String description,Integer defaltState) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaltState = defaltState;
    }

    Integer id;
    String name;
    String description;
    Integer defaltState;
}
