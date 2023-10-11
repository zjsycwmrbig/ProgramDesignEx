package com.example.javaservice.Controller;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckOutDependency {
    @Autowired
    DependencyMapMapper dependencyMapMapper;

    @GetMapping("/checkout")
    public Result checkOutDependency(Integer id,Integer state) {
        // 操作数据库找到依赖并且装配 设定规定的状态
        if(id == null) {
            return Result.error("id为空");
        }
        DependencyMap dependencyMap = dependencyMapMapper.selectById(id);
        if(dependencyMap == null) {
            return Result.error("id不存在");
        }
        if(CustomerService.AssembleDependency(dependencyMap.getPath(),state) == ResultConstant.SUCCESS){
            return Result.success("装配成功");
        }else{
            return Result.error("装配失败");
        }
    }
}
