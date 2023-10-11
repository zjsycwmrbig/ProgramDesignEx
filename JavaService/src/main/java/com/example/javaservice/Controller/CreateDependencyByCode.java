package com.example.javaservice.Controller;

import com.example.javaservice.Core.DSLCompiler;
import com.example.javaservice.Result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDependencyByCode {
    @Autowired
    DSLCompiler dslCompiler;

    // 处理纯文本
    @PostMapping( value = "/createByCode",consumes = MediaType.TEXT_PLAIN_VALUE)
    public Result createByCode(@RequestBody String code) {
        return dslCompiler.compileByCode(code);
    }
}
