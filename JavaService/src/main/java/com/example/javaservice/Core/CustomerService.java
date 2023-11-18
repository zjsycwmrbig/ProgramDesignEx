package com.example.javaservice.Core;

import com.example.javaservice.Constant.ConditionConstant;
import com.example.javaservice.Constant.ResultConstant;
import com.example.javaservice.Constant.SemanticAnalysisConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Exception.AssembleException;
import com.example.javaservice.Exception.ResposeGenerateException;
import com.example.javaservice.Pojo.Entity.*;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Result.WebsocketResult;
import com.example.javaservice.Service.Impl.FunctionCaller;
import com.example.javaservice.Utils.LOG;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;


/**
 * 静态客户系统
 */

public class CustomerService {
    private static int state = 0;
    private static RobotDependency robotDependency;
    private static ResultDictionary resultDictionary;
    private static Map<Integer,TransferNode> defaultResultMap;

    public static WebSocketSession client;

    private static Integer waitTime = 0;
    private static Integer tick = 0;
    private static Integer waitResponse;
    private static Integer waitTarget;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 新建线程
    public static Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                try { // 等待时间 如果waitTime不为0 则证明存在等待响应时间
                    // 定时器
                    for(tick = 0;tick <waitTime;tick++){
                        // 睡眠一秒
                        Thread.sleep(1000);
                    }
                    if(client != null && waitTime != 0) {
                        LOG.DEBUG("超时发送");
                        // 发送超时响应
                        WebsocketResult websocketResult = new WebsocketResult();
                        websocketResult.setType(ResultConstant.WAIT_RESPONSE);
                        websocketResult.setData(ResultGenerator(waitResponse,null));
                        String json = objectMapper.writeValueAsString(websocketResult);
                        client.sendMessage(new TextMessage(json));
                        // 可以设置发送超时响应
                        if(robotDependency.getSuggestion_when_pass()){
                            // 把建议发送给客户端 转换成json序列
                            websocketResult.setType(ResultConstant.SUGGESTION);
                            websocketResult.setData(SuggestionGenerator().toString());
                            json = objectMapper.writeValueAsString(websocketResult);
                            client.sendMessage(new TextMessage(json));
                        }
                        StateChangeProcessor(waitTarget);
                    }
                    Thread.sleep(SystemConstant.WAIT_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    /**
     * 更新响应状态
     */
    public static void resetWaitResponse(){
        if(robotDependency != null && state != -1 && robotDependency.getWaitResultMap().containsKey(state)) {
            // 可能存在超时响应状态
            tick = 0;
            waitTime = robotDependency.getWaitResultMap().get(state).getWaitTime();
            waitResponse = robotDependency.getWaitResultMap().get(state).getResultID();
            waitTarget = robotDependency.getWaitResultMap().get(state).getTargetState();
        }else{
            waitTime = 0; // 保证不可能进入循环
        }
    }

    /**
     * 建议生成器,根据state 和 robotDependency 获得转移列表 生成建议列表
     * @return
     */
    public static List<Suggestion> SuggestionGenerator() {
        // 根据state 和 robotDependency 获得转移列表 生成建议列表
        List<TransferNode>  TransferList =  robotDependency.getTransMap().get(state).getTransferList();
        List<Suggestion> suggestionList = new ArrayList<>();
        if(TransferList == null) {
            return suggestionList;
        };
        // 遍历转移列表，生成建议
        for(int i = 0;i < TransferList.size();i++){
            // 比对Condition条件
            Condition condition = TransferList.get(i).getCondition();
            Suggestion suggestion = new Suggestion();
            switch (condition.getType()){
                case ConditionConstant.JUDGE_EXACT -> {
                    // 精确匹配
                    String keyWord = condition.getREGEX().get(0);
                    suggestion.setSuggestion(keyWord);
                    suggestion.setInputTemplate(keyWord);
                    suggestionList.add(suggestion);
                    break;
                }
                case ConditionConstant.JUDGE_CONTAIN -> {
                    // 包含匹配 也是只有一个关键词
                    String keyWord = condition.getREGEX().get(0);
                    // 去除两旁的 .* 如果存在的话
                    if(keyWord.startsWith(".*")) {
                        keyWord = keyWord.substring(2);
                    }
                    if(keyWord.endsWith(".*")) {
                        keyWord = keyWord.substring(0,keyWord.length() - 2);
                    }
                    suggestion.setSuggestion("猜您想问:" + keyWord);
                    suggestion.setInputTemplate(keyWord);
                    suggestionList.add(suggestion);
                    break;
                }
                case ConditionConstant.JUDGE_REGEX -> {
                    // 正则匹配 需要按照完整的正则表达式进行匹配
                    String keyWord = condition.getREGEX().get(0);
                    suggestion.setSuggestion("你可以输入:" + keyWord + "来获取相关信息");
                    suggestion.setInputTemplate(keyWord + "请修改成您想要的关键词");
                    suggestionList.add(suggestion);
                    break;
                }
                case ConditionConstant.INPUT -> {
                    // 输入模式 关键词不再是一个
                    String suggestionStr = "您可以输入:";
                    for(int j = 0;j < condition.getREGEX().size();j++) {
                        suggestionStr += condition.getREGEX().get(j) ;
                        if(j != condition.getREGEX().size() - 1) {
                            suggestionStr += "xxx";
                        }
                    }
                    suggestionStr += "来获取相关信息";
                    String inputTemplateStr = "";
                    for(int j = 0;j < condition.getREGEX().size();j++) {
                        inputTemplateStr += condition.getREGEX().get(j) ;
                        if(j != condition.getREGEX().size() - 1) {
                            inputTemplateStr += "xxx";
                        }
                    }
                    suggestion.setInputTemplate(inputTemplateStr);
                    suggestion.setSuggestion(suggestionStr);
                    suggestionList.add(suggestion);
                    break;
                }
            }
        }
        return suggestionList;
    }


    public static Result ResponseGenerator(String inputStr) {
        if(robotDependency == null) {
            return new Result(-1,null,"机器人依赖为空");
        } // 剪枝

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
                if(TransferList.get(i).getTargetState() != -1){
                    StateChangeProcessor(TransferList.get(i).getTargetState());
                }
                return new Result(state,ResultGenerator(TransferList.get(i).getResultID(), (Map) res.getData()),"响应成功");
            }
        }

        // 查看默认响应
        if(defaultResultMap.containsKey(state)){
            // 如果存在默认响应
            return new Result(state,ResultGenerator(defaultResultMap.get(state).getResultID(),null),"响应成功");
        }


        // 没有相应成功的 从全局状态中寻找
        List<Integer> globalState = robotDependency.getGlobalState();
        for(int i = 0;i < globalState.size();i++){
            Integer global_tate = globalState.get(i);
            if(global_tate == state) continue;
            // 比对Condition条件
            Result res = null;
            TransferList = robotDependency.getTransMap().get(global_tate).getTransferList();
            if(TransferList == null) {
                throw new ResposeGenerateException("转移列表为空");
            };
            for(int j = 0;j < TransferList.size();j++){
                // 比对Condition条件
                if((res = ConditionProcessor(TransferList.get(j).getCondition(),inputStr)) != null){
                    // 如果匹配成功，那么就返回对应的响应
                    if(TransferList.get(j).getTargetState() != -1){
                        StateChangeProcessor(TransferList.get(j).getTargetState());
                    }
                    return new Result(state,ResultGenerator(TransferList.get(j).getResultID(), (Map) res.getData()),"响应成功");
                }
            }
            // 全局查询默认
            if(defaultResultMap.containsKey(global_tate)){
                // 如果存在默认响应
                return new Result(state,ResultGenerator(defaultResultMap.get(global_tate).getResultID(),null),"响应成功");
            }
        }
        return null;
    }

    /**
     * 根据响应ID生成响应
     * @return
     */
    private static Result StateChangeProcessor(Integer targetState){
        if(targetState != -1){
            state = targetState;
            resetWaitResponse(); // 状态更新也要更新等待响应模块
            if(robotDependency.getSuggestion_when_check()){
                try {
                    WebsocketResult websocketResult = new WebsocketResult();
                    websocketResult.setType(ResultConstant.SUGGESTION);
                    websocketResult.setData(SuggestionGenerator());
                    String json = objectMapper.writeValueAsString(websocketResult);
                    client.sendMessage(new TextMessage(json));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Result.success();
        }else{
            return Result.error();
        }
    }

    public static int AssembleDependency(String dependencyPath,Integer assembleState) {
        LOG.INFO("开始装配依赖" + dependencyPath);
        Object obj = null;
        try {
            FileInputStream fileIn = new FileInputStream(SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyPath);
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
            defaultResultMap = robotDependency.getDefaultResultMap();
            if(assembleState == null){
                state = robotDependency.getDefaultState();
            }else{
                state = assembleState; // 装配的状态
            }

            LOG.INFO("默认状态: " + state);
            return ResultConstant.SUCCESS;
        } else{
            throw new AssembleException("反序列化失败");
        }
    }

    public static Result ConditionProcessor(Condition condition, String inputStr) {
        if(condition.getPattern() == null){
            LOG.WARNING("正则表达式为空");
            return null;
        }

        if(condition.getType() == ConditionConstant.JUDGE_REGEX || condition.getType() == ConditionConstant.JUDGE_CONTAIN || condition.getType() == ConditionConstant.JUDGE_EXACT){
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

    public static String ResultGenerator(Integer resultID, Map inputs) {
        // 打印inputs
        CompileResult result = resultDictionary.getCompileResult(resultID);
        if(Objects.equals(result.getType(), SemanticAnalysisConstant.CONSTANT_RESULT)){
            return result.getValue();
        }else if(Objects.equals(result.getType(), SemanticAnalysisConstant.COMPLEX_RESULT)) { // 复合型结果 需要遍历增加
            String resultStr = "";
            List<Integer> resultIDList = (List<Integer>) result.getData();
            if(resultIDList == null){
                LOG.WARNING("复合型结果为空");
                return "";
            }

            for (int i = 0; i < resultIDList.size(); i++) {
                resultStr += " " + ResultGenerator(resultIDList.get(i), inputs);
            }
            return  resultStr;
        }else if(Objects.equals(result.getType(), SemanticAnalysisConstant.FUNCTION_RESULT)){
            // 需要导入函数参数进行赋值 并且调用 data指的都是参数和结果指向列表

            Map<String,Integer> resultParams = (Map<String, Integer>) result.getData();

            if(resultParams == null){
                LOG.WARNING("输入型结果参数为空");
                return "";
            }
            Map<String,String> params = new HashMap<>();

            for(Map.Entry<String,Integer> entry : resultParams.entrySet()){
                params.put(entry.getKey(),ResultGenerator(entry.getValue(),inputs));
            }

            return FunctionCaller.Caller(result.getValue(),params);
        }else if(Objects.equals(result.getType(), SemanticAnalysisConstant.INPUT_VARIABLE_RESULT)){
            // 需要导入的参数进行赋值处理，参数在params中，这个节点的vaule是变量名称
            String variableName = result.getValue();

            if(inputs.containsKey(variableName)){
                return (String) inputs.get(variableName);
            }else{
                LOG.WARNING("变量名不存在");
                return "";
            }
        }
        else{
            LOG.WARNING("结果解析出现未知类型");
        }

        return "";
    }

    public static void testAssembly(RobotDependency robot){
        // 测试性装填
        robotDependency = robot;
        resultDictionary = robot.getResultDictionary();
    }

    public static void resetState() {
        state = robotDependency.getDefaultState();
    }
}
