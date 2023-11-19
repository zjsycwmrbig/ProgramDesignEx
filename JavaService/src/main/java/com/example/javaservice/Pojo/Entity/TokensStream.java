package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.util.List;

@Data
public class TokensStream {
    public List<Token> stream;
    public int getLine(Integer index){
        if(index >= stream.size()){
            return stream.get(stream.size()-1).lineIndex;
        }else{
            return stream.get(index).lineIndex;
        }
    }
}
