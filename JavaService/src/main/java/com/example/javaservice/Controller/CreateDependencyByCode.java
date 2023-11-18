package com.example.javaservice.Controller;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Core.DSLCompiler;
import com.example.javaservice.Pojo.Dto.NewDependencyDto;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDependencyByCode {
    @Autowired
    DSLCompiler dslCompiler;
    @Autowired
    DependencyManager dependencyManager;

    // 处理纯文本
    @PostMapping( value = "/createByCode")
    public Result createByCode(@RequestBody NewDependencyDto newDependencyDto) {
        Result result = dslCompiler.compileByCode(newDependencyDto);

        if(result.getState() != ResultConstant.ERROR &&  newDependencyDto.getId() != null){
            // 需要销毁原来的依赖
            dependencyManager.destroyDependency(newDependencyDto.getId());
            // 更新客服机器人状态
            dependencyManager.updateDependencyState();
        }
        return result;


    }
}
