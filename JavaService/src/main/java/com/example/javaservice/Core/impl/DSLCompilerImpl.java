package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.LexicalAnalyzer;
import com.example.javaservice.Core.SemanticAnalyzer;
import com.example.javaservice.Core.SyntaxAnalyzer;
import com.example.javaservice.Exception.SaveException;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Entity.*;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Service
public class DSLCompilerImpl implements com.example.javaservice.Core.DSLCompiler{

    @Autowired
    DependencyMapMapper dependencyMapMapper;

    // 这个地方才有原始的数据，包含tokens等数据，如果有警告，错误，也会在这里处理，然后返回

    @Autowired
    LexicalAnalyzer lexicalAnalyzer;

    @Autowired
    SyntaxAnalyzer syntaxAnalyzer;

    // 语义分析 返回警告和错误信息，以及产生的Dependency(成功了）
    @Autowired
    SemanticAnalyzer semanticAnalyzer;

    // compile 函数 负责装配 和 存储数据


    @Override
    public Result SaveDependency(RobotDependency robotDependency,String code) {
        DependencyMap dependencyMap = new DependencyMap();
        dependencyMap.setName(SystemConstant.DEFAULT_DEPENDENCY_NAME);
        dependencyMap.setPath(UUID.randomUUID().toString());
        dependencyMap.setCode(UUID.randomUUID().toString());
        dependencyMap.setDefaltState(robotDependency.getDefaultState());
        try {
            LOG.INFO("开始序列化存储");
            LOG.INFO("存储路径为" + SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyMap.getPath());
            LOG.INFO("默认状态: " + robotDependency.getDefaultState());
            FileOutputStream fileOut = new FileOutputStream(SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyMap.getPath());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(robotDependency);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            throw new SaveException("序列化存储失败");
        }

        if(code == null){
            code = "This is a test code";
        }
            // 保存代码
        try{
            LOG.INFO("开始代码存储");
                LOG.INFO("存储路径为" + SystemConstant.INPUT_PATH + dependencyMap.getCode());
                FileOutputStream fileOut = new FileOutputStream(SystemConstant.INPUT_PATH + dependencyMap.getCode());
                fileOut.write(code.getBytes());
                fileOut.close();
            } catch (Exception e) {
                throw new SaveException("代码存储失败");
            }
        // 存储依赖映射
        Integer id = dependencyMapMapper.insert(dependencyMap);
        return new Result(id,null,null) ;
    }

    @Override
    public Result compileByCode(String code) {
        Tokens tokens = lexicalAnalyzer.analyze(code);
        if(tokens == null){
            return Result.error("词法分析失败");
        }
        LOG.INFO("词法分析完成");
        AbstractSyntaxTree abstractSyntaxTree = syntaxAnalyzer.analyze(tokens.getStream());
        if(abstractSyntaxTree == null){
            return Result.error("语法分析失败");
        }
        LOG.INFO("语法分析完成");
        Map<String, Object> analyseResult = semanticAnalyzer.analyse(abstractSyntaxTree);

        LOG.INFO("语义分析完成");
        RobotDependency robotDependency = (RobotDependency) analyseResult.get("robotDependency");
        List<CompileWarning> compileWarnings = (List<CompileWarning>) analyseResult.get("compileWarnings");

        if(robotDependency != null){
            // 保存依赖
            Result id = SaveDependency(robotDependency, code);

            return new Result(id.getState(),compileWarnings,"编译成功");
        }else{
            return Result.error("语义分析失败");
        }
    }


}
