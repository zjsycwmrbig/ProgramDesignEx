package com.example.javaservice.Pojo.Entity;
// 映射到表 dependency_map
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("dependency_map")
@Data
public class DependencyMap {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    Integer id;
    String name;
    String description;
    String path;
    String code;
    @TableField("default_state")
    Integer defaltState;
}
