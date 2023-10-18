package com.example.javaservice.Controller;

import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import com.example.javaservice.Utils.LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

@RestController
public class CodeOffer {

    @Autowired
    DependencyManager dependencyManager;

    @GetMapping("/code")
    public Result codeOffer(Integer id){
        LOG.INFO("收到获取代码请求" + id);
        return dependencyManager.getDependencyCode(id);
    }
}
