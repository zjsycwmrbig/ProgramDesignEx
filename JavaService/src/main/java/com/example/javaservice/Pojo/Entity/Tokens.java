package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 词法分析器返回的tokens，包含stream和map，方便后续处理
 */

@Data
public class Tokens {
    private List<Token> stream;
    private Map<Integer,List<String>> map;

    public Tokens(List<Token> stream, Map<Integer, List<String>> map) {
        this.stream = stream;
        this.map = map;
    }
}
