package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.LexicalAnalyzer;
import com.example.javaservice.Core.SemanticAnalyzer;
import com.example.javaservice.Core.SyntaxAnalyzer;
import com.example.javaservice.Exception.SaveException;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Dto.NewDependencyDto;
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
    public Result compileByCode(NewDependencyDto newDependencyDto) {
        String code = newDependencyDto.getCode();
        String name = newDependencyDto.getName();
        String description = newDependencyDto.getDescription();
        Boolean suggestion_when_check = newDependencyDto.getSuggestion_when_check();
        Boolean suggestion_when_pass = newDependencyDto.getSuggestion_when_pass();

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerIpml();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzerImpl();

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
            Result res = SaveDependency(robotDependency, code, name, description,suggestion_when_check,suggestion_when_pass);
            if(res.getState() == ResultConstant.SUCCESS){
                return new Result((Integer) res.getData(),compileWarnings,"编译成功");
            }else{
                return Result.error("依赖存储失败");
            }
        }else{
            return Result.error("语义分析失败");
        }
    }

    private Result SaveDependency(RobotDependency robotDependency, String code, String name, String description,Boolean suggestion_when_check,Boolean suggestion_when_pass) {
        DependencyMap dependencyMap = new DependencyMap();
        dependencyMap.setName(name);
        dependencyMap.setPath(UUID.randomUUID().toString());
        dependencyMap.setCode(UUID.randomUUID().toString());
        dependencyMap.setDefaltState(robotDependency.getDefaultState());
        dependencyMap.setDescription(description);

        robotDependency.setSuggestion_when_check(suggestion_when_check);
        robotDependency.setSuggestion_when_pass(suggestion_when_pass);
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
        // 存储依赖映射 返回id
        Integer id = null;
        if(dependencyMapMapper.insert(dependencyMap) > 0){
            id = dependencyMap.getId();
            return Result.success(id) ;
        }else{
            return Result.error("存储依赖映射失败");
        }
    }

}
