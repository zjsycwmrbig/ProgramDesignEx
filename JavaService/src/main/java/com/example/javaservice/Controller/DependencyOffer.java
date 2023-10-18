package com.example.javaservice.Controller;

import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Pojo.Vo.DependencyVo;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DependencyOffer {

    @Autowired
    DependencyManager dependencyManager;

    @GetMapping("/dependency")
    public Result getDependency() {
        // 操作数据库找到依赖列表
        return dependencyManager.getDependencyList();
    }
}
