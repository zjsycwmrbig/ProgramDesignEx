package com.example.javaservice.Pojo.Entity;

import lombok.Data;

/**
 * 建议类
 */
@Data
public class Suggestion {
    Integer robotDependencyId;
    String suggestion;
    Integer state;
    Integer nodeID;
}
