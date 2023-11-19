package com.example.javaservice.Pojo.Entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 判断条件类
 * 1. 正则匹配式
 *
 */
@Data
public class Condition implements java.io.Serializable{
    Integer type; // type
    // data，这里的REGEX可能存在为空的情况，或者仅仅存在一个REGEX,但是想要提取出一个input，我们必须要有两个REGEX夹逼
    List<String> REGEX;
    Integer input_num; // input_num 应该理论上需要多少个input

    // 编译后填充
    Pattern pattern; // 正则表达式
    // 参数列表
    List<String> params;

    public  Condition(Integer type, List<String> REGEX, Integer input_num){
        this.type = type;
        this.REGEX = REGEX;
        this.input_num = input_num;
        pattern = null;
        params = new ArrayList<>();
    }

    public Boolean equals (Condition condition){
        if(this.type != condition.type){
            return false;
        }
        if(this.REGEX.size() != condition.REGEX.size()){
            return false;
        }
        for(int i = 0; i < this.REGEX.size(); i++){
            if(!this.REGEX.get(i).equals(condition.REGEX.get(i))){
                return false;
            }
        }
        return true;
    }

}
