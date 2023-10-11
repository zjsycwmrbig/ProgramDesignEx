package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.ConditionConstant;
import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SemanticAnalysisConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Exception.AssembleException;
import com.example.javaservice.Exception.ResposeGenerateException;
import com.example.javaservice.Exception.SaveException;
import com.example.javaservice.Pojo.Entity.*;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.FunctionCaller;
import com.example.javaservice.Utils.LOG;

import java.io.FileInputStream;

import java.io.ObjectInputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * 静态客户系统
 */

public class CustomerService {
    private static int state = 0;
    private static RobotDependency robotDependency;
    private static ResultDictionary resultDictionary;


    public static List<Suggestion> SuggestionGenerator() {
        return null;
    }


    public static String ResponseGenerator(String inputStr) {
        // 根据state 和 robotDependency 获得转移列表
        List<TransferNode>  TransferList =  robotDependency.getTransMap().get(state).getTransferList();
        if(TransferList == null) {
            throw new ResposeGenerateException("转移列表为空");
        };
        // 遍历转移列表，找到符合匹配的转移节点
        for(int i = 0;i < TransferList.size();i++){
            // 比对Condition条件
            Result res = null;
            if((res = ConditionProcessor(TransferList.get(i).getCondition(),inputStr)) != null){
                // 如果匹配成功，那么就返回对应的响应
                return ResultGenerator(TransferList.get(i).getResultID(), (Map) res.getData());
            }
        }
        // 没有相应成功的
        return null;
    }


    public static Integer AssembleDependency(String dependencyID)  {
        Object obj = null;
        try {
            FileInputStream fileIn = new FileInputStream(SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyID);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            obj = in.readObject();
            in.close();
            fileIn.close();
        }catch (Exception e){
            throw new AssembleException("反序列化读取失败");
        }

        if(obj instanceof RobotDependency){
            robotDependency = (RobotDependency) obj;
            resultDictionary = robotDependency.getResultDictionary();
            state = robotDependency.getDefaultState(); // 设置默认状态
            return ResultConstant.SUCCESS;
        } else{
            throw new AssembleException("反序列化失败");
        }
    }

    public static Result InputParser(String inputStr) {
        return null;
    }

    public static Result ConditionProcessor(Condition condition, String inputStr) {
        if(condition.getPattern() == null){
            LOG.WARNING("正则表达式为空");
            return null;
        }
        if(condition.getType() == ConditionConstant.JUDGE){
            if(inputStr.matches(condition.getPattern().pattern())){
                return Result.success();
            }else{
                return null;
            }
        }else if(condition.getType() == ConditionConstant.INPUT){
            Matcher matcher = condition.getPattern().matcher(inputStr);
            if(matcher.find()){
                Map<String,String> params = new HashMap<>();
                for(int i = 0;i < condition.getParams().size();i++){
                    params.put(condition.getParams().get(i),matcher.group(i+1));
                }
                return Result.success(params);
            }else{
                return null;
            }
        }else{
            LOG.WARNING("条件类型错误");
        }
        return null;
    }

    public static String ResultGenerator(Integer resultID, Map params) {
        CompileResult result = resultDictionary.getCompileResult(resultID);

        if(result.getType() == SemanticAnalysisConstant.CONSTANT_RESULT){
            return result.getValue();
        }else if(result.getType() == SemanticAnalysisConstant.COMPLEX_RESULT) {
            // 复合型结果 需要遍历增加
            String resultStr = "";
            List<Integer> resultIDList = (List<Integer>) result.getData();
            if(resultIDList == null){
                LOG.WARNING("复合型结果为空");
                return "";
            }

            for (int i = 0; i < resultIDList.size(); i++) {
                resultStr += " " + ResultGenerator(resultIDList.get(i), params);
            }
            return  resultStr;
        }else if(result.getType() == SemanticAnalysisConstant.INPUT_RESULT){
            // 需要导入函数参数进行赋值 并且调用
            return FunctionCaller.Caller(result.getValue(),params);
        }
        return null;
    }


    public static void testAssembly(RobotDependency robotDependency){
        // 测试性装填
        robotDependency = robotDependency;
        resultDictionary = robotDependency.getResultDictionary();
    }


}
