package com.example.javaservice.Controller;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import com.example.javaservice.Utils.LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckOutDependency {
    @Autowired
    DependencyManager dependencyManager;

    @GetMapping("/checkout")
    public Result checkOutDependency(Integer id,Integer state) {
        // 操作数据库找到依赖并且装配 设定规定的状态
        LOG.INFO("收到装配请求" + id + "状态为" + state);
        return dependencyManager.checkOutDependency(id, state);
    }
}
