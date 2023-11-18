package com.example.javaservice.Service.Impl;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Mapper.TelephoneMapMapper;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Pojo.Vo.DependencyVo;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
public class DependencyManagerImpl implements com.example.javaservice.Service.DependencyManager {

    @Autowired
    DependencyMapMapper dependencyMapMapper;

    @Autowired
    TelephoneMapMapper telephoneMapMapper; // 从这里注入

    @Override
    public Result checkOutDependency(Integer id, Integer state) {
        FunctionCaller.init(telephoneMapMapper);
        if(id == null) {
            return Result.error("id为空");
        }
        DependencyMap dependencyMap = dependencyMapMapper.selectById(id);
        if(dependencyMap == null) {
            return Result.error("id不存在");
        }
        if(CustomerService.AssembleDependency(dependencyMap.getPath(),state) == ResultConstant.SUCCESS){
            LOG.INFO("装配成功: " + dependencyMap.getName() + " 状态编号为" + state);

            // 重置等待响应
            CustomerService.resetWaitResponse();

            return Result.success("装配成功");
        }else{
            return Result.error("装配失败");
        }
    }

    @Override
    public Result getDependencyCode(Integer id) {
        if(id == null) {
            return Result.error("id为空");
        }else{
            DependencyMap dependencyMap = dependencyMapMapper.selectById(id);
            if(dependencyMap == null) {
                return Result.error("id不存在");
            }else{
                String codePath = dependencyMap.getCode();
                // 打开文件
                String Code = "";
                try {
                    FileInputStream fileInputStream = new FileInputStream(SystemConstant.INPUT_PATH + codePath);
                    byte[] bytes = fileInputStream.readAllBytes();
                    Code = new String(bytes);
                    fileInputStream.close();
                    LOG.INFO("获取代码成功" + id);
                    return Result.success(Code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Result.error("获取代码失败");
            }
        }
    }

    @Override
    public Result getDependencyList() {
        List<DependencyVo> dependencyVos = dependencyMapMapper.selectList(null).stream().map(
                dependencyMap -> {
                    return new DependencyVo(dependencyMap.getId(), dependencyMap.getName(), dependencyMap.getDescription(),dependencyMap.getDefaltState());
                }
        ).toList();
        return Result.success(dependencyVos);
    }

    @Override
    public Result destroyDependency(Integer id) {
        // 找到依赖
        DependencyMap dependencyMap = dependencyMapMapper.selectById(id);
        if(dependencyMap == null) {
            return Result.error("id不存在");
        }
        // 执行销毁 删除文件
        File file = new File(SystemConstant.INPUT_PATH + dependencyMap.getCode());
        if(file.exists()) {
            file.delete();
        }
        // 删除依赖文件
        file = new File(SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyMap.getPath());
        if(file.exists()) {
            file.delete();
        }
        // 删除数据库
        dependencyMapMapper.deleteById(id);
        return Result.success("销毁成功");
    }

    @Override
    public Result updateDependencyState() {
        CustomerService.resetState();
        return Result.success("更新成功");
    }
}
