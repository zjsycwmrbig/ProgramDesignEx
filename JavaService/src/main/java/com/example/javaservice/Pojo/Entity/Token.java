package com.example.javaservice.Pojo.Entity;

import com.example.javaservice.Constant.TokensConstant;
import lombok.Data;


public class Token {
    public Token(int type, String token, int lineIndex) {
        this.type = type;
        this.token = token;
        this.lineIndex = lineIndex;
    }
    public int type;
    public String token;
    public int lineIndex;
    @Override
    public String toString() {
        return "Token{" +
                "type=" + TokensConstant.TOKENS[type] +
                ", token='" + token + '\'' +
                ", lineIndex=" + lineIndex +
                '}';
    }
}
