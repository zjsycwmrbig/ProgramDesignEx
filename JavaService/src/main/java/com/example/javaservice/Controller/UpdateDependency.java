package com.example.javaservice.Controller;

import com.example.javaservice.Pojo.Dto.NewDependencyDto;
import com.example.javaservice.Result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateDependency {
    @PostMapping("/updateCode")
    public Result updateCode(@RequestBody NewDependencyDto newDependencyDto) {
        // 重新编译code，如果成功，更新相关数据
        return null;
    }
}
