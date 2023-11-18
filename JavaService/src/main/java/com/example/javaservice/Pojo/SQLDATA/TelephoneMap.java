package com.example.javaservice.Pojo.SQLDATA;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("telephone")
public class TelephoneMap {
    @TableId(value = "id")
    String id;
    int value;
    public TelephoneMap(String id, Integer value) {
        this.id = id;
        this.value = value;
    }
}
