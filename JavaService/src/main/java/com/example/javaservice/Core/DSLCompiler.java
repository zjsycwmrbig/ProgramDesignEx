package com.example.javaservice.Core;

import com.example.javaservice.Pojo.Dto.NewDependencyDto;
import com.example.javaservice.Pojo.Entity.RobotDependency;
import com.example.javaservice.Result.Result;

public interface DSLCompiler {

    /**
     * 保存依赖文件,通过自动生成ID保存到本地
     * @return
     */
    public Result SaveDependency(RobotDependency robotDependency, String code);



    Result compileByCode(NewDependencyDto newDependencyDto);
}
