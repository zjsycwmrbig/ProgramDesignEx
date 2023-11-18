package com.example.javaservice.Service;

import com.example.javaservice.Result.Result;

public interface DependencyManager {
    /**
     * 切换装配文件
     */
    public Result checkOutDependency(Integer id,Integer state);


    /**
     * 获取代码文件
     */
    public Result getDependencyCode(Integer id);


    /**
     * 找到依赖列表
     */
    public Result getDependencyList();


    /**
     * 销毁依赖
     */
    public Result destroyDependency(Integer id);

    /**
     * 针对更新依赖的状态不回滚情况进行处理
     */
    public Result updateDependencyState();
}
