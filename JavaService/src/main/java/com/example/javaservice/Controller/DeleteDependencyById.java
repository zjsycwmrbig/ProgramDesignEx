package com.example.javaservice.Controller;

import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteDependencyById {
    @Autowired
    DependencyManager dependencyManager;

    @GetMapping("/delete")
    public Result deleteDependency(Integer id) {
        // 操作数据库找到依赖并且装配 设定规定的状态
        return dependencyManager.destroyDependency(id);
    }
}
