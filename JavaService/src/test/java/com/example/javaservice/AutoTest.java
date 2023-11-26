package com.example.javaservice;

import com.example.javaservice.Config.AutoTestScriptConfig;
import com.example.javaservice.Constant.ConditionConstant;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Constant.TestUnitConstant;
import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Core.DSLCompiler;
import com.example.javaservice.Exception.CompileErrorException;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Pojo.Dto.NewDependencyDto;
import com.example.javaservice.Pojo.Entity.*;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Service.DependencyManager;
import com.example.javaservice.Utils.LOG;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class AutoTest {

    // 随机生成
    Boolean RandomGenerate;

    @Autowired
    AutoTestScriptConfig autoTestScriptConfig;

    @Autowired
    DSLCompiler dslCompiler;

    @Autowired
    DependencyManager dependencyManager;

    @Autowired
    DependencyMapMapper dependencyMapMapper;

    @Test
    void TestAutoScriptConfig(){
        System.out.println(autoTestScriptConfig.getTotalScriptsPath());
        System.out.println(autoTestScriptConfig.getErrorScriptsPath());
        System.out.println(autoTestScriptConfig.getSuccessScriptsPath());
        System.out.println(autoTestScriptConfig.getWarningScriptsPath());
        System.out.println(autoTestScriptConfig.getResponseScriptPath());
        System.out.println(autoTestScriptConfig.getTestBias());
        System.out.println(autoTestScriptConfig.getTestRound());
        System.out.println(autoTestScriptConfig.getTestNumber());
    }

    // 测试桩 创建脚本
    @Test
    void CreateScripts(){
        String ErrorScriptsPath = autoTestScriptConfig.getErrorScriptsPath();
        String SuccessScriptsPath = autoTestScriptConfig.getSuccessScriptsPath();
        String WarningScriptsPath = autoTestScriptConfig.getWarningScriptsPath();

        // 读取代码原本 src/main/resources/static/DemoCode.txt
        String code = "";
        FileInputStream fileInputStream ;
        // 读取文件 到 code
        try {
            fileInputStream = new FileInputStream("src/main/resources/static/DemoCode.txt");
            // 读取所有内容到code
            byte[] bytes = fileInputStream.readAllBytes();
            code = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 每个文件夹中添加10个脚本
        for(int i = 0; i < 10; i++){
            // 创建错误脚本 命名为 ErrorScript + i + .txt 存放到目录下
            String ErrorScriptPath = ErrorScriptsPath + "ErrorScript" + i + ".txt";
            File ErrorScriptFile = new File(ErrorScriptPath);
            try {
                ErrorScriptFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 写入错误脚本
            try {
                java.io.FileWriter fileWriter = new java.io.FileWriter(ErrorScriptFile);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 创建成功脚本 命名为 SuccessScript + i + .txt 存放到目录下
            String SuccessScriptPath = SuccessScriptsPath + "SuccessScript" + i + ".txt";
            File SuccessScriptFile = new File(SuccessScriptPath);
            try {
                SuccessScriptFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 写入成功脚本
            try {
                java.io.FileWriter fileWriter = new java.io.FileWriter(SuccessScriptFile);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 创建警告脚本 命名为 WarningScript + i + .txt 存放到目录下
            String WarningScriptPath = WarningScriptsPath + "WarningScript" + i + ".txt";
            File WarningScriptFile = new File(WarningScriptPath);

            try {
                WarningScriptFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 写入警告脚本
            try {
                java.io.FileWriter fileWriter = new java.io.FileWriter(WarningScriptFile);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LOG.INFO("创建脚本完成");
    }
    // 测试桩 更新脚本
    @Test
    void updateScript(){
        // "购买".st.东西 替换成 "购买".st."东西"
        String ErrorScriptsPath = autoTestScriptConfig.getErrorScriptsPath();
        String SuccessScriptsPath = autoTestScriptConfig.getSuccessScriptsPath();
        String WarningScriptsPath = autoTestScriptConfig.getWarningScriptsPath();

        // 读取文件
        File ErrorScriptsDir = new File(ErrorScriptsPath);
        File SuccessScriptsDir = new File(SuccessScriptsPath);
        File WarningScriptsDir = new File(WarningScriptsPath);

        // 获取所有文件
        File[] ErrorScripts = ErrorScriptsDir.listFiles();
        File[] SuccessScripts = SuccessScriptsDir.listFiles();
        File[] WarningScripts = WarningScriptsDir.listFiles();

        // 读取文件内容
        for(File ErrorScript : ErrorScripts){
            try {
                FileInputStream fileInputStream = new FileInputStream(ErrorScript);
                byte[] bytes = fileInputStream.readAllBytes();
                String code = new String(bytes);
                code = code.replace("\"购买\".st.东西", "\"购买\".st.\"东西\"");
                java.io.FileWriter fileWriter = new java.io.FileWriter(ErrorScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(File SuccessScript : SuccessScripts){
            try {
                FileInputStream fileInputStream = new FileInputStream(SuccessScript);
                byte[] bytes = fileInputStream.readAllBytes();
                String code = new String(bytes);
                code = code.replace("\"购买\".st.东西", "\"购买\".st.\"东西\"");
                java.io.FileWriter fileWriter = new java.io.FileWriter(SuccessScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(File WarningScript : WarningScripts){
            try {
                FileInputStream fileInputStream = new FileInputStream(WarningScript);
                byte[] bytes = fileInputStream.readAllBytes();
                String code = new String(bytes);
                code = code.replace("\"购买\".st.东西", "\"购买\".st.\"东西\"");
                java.io.FileWriter fileWriter = new java.io.FileWriter(WarningScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 测试桩 对所有脚本进行编译
    @Test
    void PreTestCompile(){
        // 对所有脚本进行编译 把结果进行整理 写回脚本中
        String ErrorScriptsPath = autoTestScriptConfig.getErrorScriptsPath();
        String SuccessScriptsPath = autoTestScriptConfig.getSuccessScriptsPath();
        String WarningScriptsPath = autoTestScriptConfig.getWarningScriptsPath();

        // 读取文件
        File ErrorScriptsDir = new File(ErrorScriptsPath);
        File SuccessScriptsDir = new File(SuccessScriptsPath);
        File WarningScriptsDir = new File(WarningScriptsPath);

        // 获取所有文件
        File[] ErrorScripts = ErrorScriptsDir.listFiles();
        File[] SuccessScripts = SuccessScriptsDir.listFiles();
        File[] WarningScripts = WarningScriptsDir.listFiles();

        // 读取文件内容
        for(File ErrorScript : ErrorScripts){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(ErrorScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(ErrorScript.getName());
                newDependencyDto.setDescription("测试错误脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
                System.out.println(annotion);


            } catch (Exception e) {
                annotion = ParseResultFromException(e);
                System.out.println(annotion);
            }
            // 把编译结果替换文件首部相同的行数
            String[] lines = code.split("\n");
            String[] annotions = annotion.split("\n");
            for(int i = 0; i < annotions.length; i++){
                lines[i] = annotions[i];
            }
            code = String.join("\n", lines);
            // 以注释的形式把结果 写回文件
            try {
                java.io.FileWriter fileWriter = new java.io.FileWriter(ErrorScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(File successScript : SuccessScripts){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(successScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(successScript.getName());
                newDependencyDto.setDescription("测试正确脚本");
                newDependencyDto.setCode(code);

                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
                System.out.println(annotion);

            } catch (Exception e) {
                annotion = ParseResultFromException(e);
                System.out.println(annotion);
            }

            try {
                // 把编译结果替换文件首部相同的行数
                String[] lines = code.split("\n");
                String[] annotions = annotion.split("\n");
                for(int i = 0; i < annotions.length; i++){
                    lines[i] = annotions[i];
                }
                code = String.join("\n", lines);

                // 以注释的形式把结果 写回文件
                java.io.FileWriter fileWriter = null;
                fileWriter = new java.io.FileWriter(successScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for(File warningScript : WarningScripts){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(warningScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(warningScript.getName());
                newDependencyDto.setDescription("测试警告脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
                System.out.println(annotion);


            } catch (Exception e) {
                annotion = ParseResultFromException(e);
                System.out.println(annotion);
            }
            try {
                // 把编译结果替换文件首部相同的行数
                String[] lines = code.split("\n");
                String[] annotions = annotion.split("\n");
                for(int i = 0; i < annotions.length; i++){
                    lines[i] = annotions[i];
                }
                code = String.join("\n", lines);
                // 以注释的形式把结果 写回文件
                java.io.FileWriter fileWriter = null;
                fileWriter = new java.io.FileWriter(warningScript);
                fileWriter.write(code);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 自动测试脚本
     * 检测编译是否成功
     */
    @Test
    void TestCompile(){
        LOG.INFO("开始测试");
        // 输出结果 用日期开头
        String outputString = "";

        // 获取所有的脚本路径
        String ErrorScriptsPath = autoTestScriptConfig.getErrorScriptsPath();
        String SuccessScriptsPath = autoTestScriptConfig.getSuccessScriptsPath();
        String WarningScriptsPath = autoTestScriptConfig.getWarningScriptsPath();

        // 编译随机部分脚本 查看是否和预期一致
        // 读取文件
        File ErrorScriptsDir = new File(ErrorScriptsPath);
        File SuccessScriptsDir = new File(SuccessScriptsPath);
        File WarningScriptsDir = new File(WarningScriptsPath);

        // 获取所有文件
        File[] ErrorScriptsFiles = ErrorScriptsDir.listFiles();
        File[] SuccessScriptsFiles = SuccessScriptsDir.listFiles();
        File[] WarningScriptsFiles = WarningScriptsDir.listFiles();

        // 统计测试文件数量 以及 本次测试文件

        outputString += "本次测试文件数量：\n";
        outputString += "错误脚本：" + ErrorScriptsFiles.length + "\n";
        outputString += "正确脚本：" + SuccessScriptsFiles.length + "\n";
        outputString += "警告脚本：" + WarningScriptsFiles.length + "\n";

        outputString += "本次测试文件：\n";

        for(File ErrorScript : ErrorScriptsFiles){
            outputString += ErrorScript.getName() + "\n";
        }
        for(File SuccessScript : SuccessScriptsFiles){
            outputString += SuccessScript.getName() + "\n";
        }
        for(File WarningScript : WarningScriptsFiles){
            outputString += WarningScript.getName() + "\n";
        }


        Integer ScriptCount = ErrorScriptsFiles.length + SuccessScriptsFiles.length + WarningScriptsFiles.length;
        Integer ErrorOkTest = 0;
        Integer SuccessOkTest = 0;
        Integer WarningOkTest = 0;

        LOG.INFO("共有" + ScriptCount + "个脚本需要测试");
        Integer ErrorScriptCount = ErrorScriptsFiles.length;
        Integer SuccessScriptCount = SuccessScriptsFiles.length;
        Integer WarningScriptCount = WarningScriptsFiles.length;

        // 读取文件内容
        for(File ErrorScript : ErrorScriptsFiles){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(ErrorScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(ErrorScript.getName());
                newDependencyDto.setDescription("测试错误脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
            } catch (Exception e) {
                annotion = ParseResultFromException(e);
            }

            // 在代码中查找注释
            if(code.startsWith(annotion)){
                LOG.INFO(ErrorScript.getName() + "测试错误脚本成功");
                outputString += ErrorScript.getName() + "测试错误脚本成功\n";
                ErrorOkTest++;
            }else{
                LOG.ERROR(ErrorScript.getName() + "测试错误脚本失败");
                outputString += ErrorScript.getName() + "测试错误脚本失败\n";
            }

            // 去除annotion中 每一行开头的注释
            String[] annotions = annotion.split("\n");
            for(int i = 0; i < annotions.length; i++){
                annotions[i] = annotions[i].substring(2);
            }
            annotion = String.join("\n", annotions);
            outputString += annotion + "\n";

        }


        for(File SuccessScript : SuccessScriptsFiles){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(SuccessScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(SuccessScript.getName());
                newDependencyDto.setDescription("测试正确脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
            } catch (Exception e) {
                annotion = ParseResultFromException(e);
            }
            // 在代码中查找注释
            if(code.startsWith(annotion)){
                LOG.INFO(SuccessScript.getName() + "测试正确脚本成功");
                outputString += SuccessScript.getName() + "测试正确脚本成功\n";
                SuccessOkTest++;
            }else{
                LOG.ERROR(SuccessScript.getName() + "测试正确脚本失败");
                outputString += SuccessScript.getName() + "测试正确脚本失败\n";
            }

            // 去除annotion中 每一行开头的注释
            String[] annotions = annotion.split("\n");
            for(int i = 0; i < annotions.length; i++){
                annotions[i] = annotions[i].substring(2);
            }
            annotion = String.join("\n", annotions);
            outputString += annotion + "\n";
        }


        for(File WarningScript : WarningScriptsFiles){
            String code = "";
            String annotion = "";
            try {
                FileInputStream fileInputStream = new FileInputStream(WarningScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(WarningScript.getName());
                newDependencyDto.setDescription("测试警告脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                annotion = ParseResultFromResult(result);
            } catch (Exception e) {
                annotion = ParseResultFromException(e);
            }
            // 在代码中查找注释
            if(code.startsWith(annotion)){
                LOG.INFO(WarningScript.getName() + "测试警告脚本成功");
                outputString += WarningScript.getName() + "测试警告脚本成功\n";
                WarningOkTest++;
            }else{
                LOG.ERROR(WarningScript.getName() + "测试警告脚本失败");
                outputString += WarningScript.getName() + "测试警告脚本失败\n";
            }

            // 去除annotion中 每一行开头的注释
            String[] annotions = annotion.split("\n");
            for(int i = 0; i < annotions.length; i++){
                annotions[i] = annotions[i].substring(2);
            }
            annotion = String.join("\n", annotions);
            outputString += annotion + "\n";
        }

        // 打印总结
        LOG.INFO("测试完成");
        LOG.INFO("错误脚本测试完成" + ErrorOkTest + "/" + ErrorScriptCount);
        LOG.INFO("正确脚本测试完成" + SuccessOkTest + "/" + SuccessScriptCount);
        LOG.INFO("警告脚本测试完成" + WarningOkTest + "/" + WarningScriptCount);
        outputString += "错误脚本共" + ErrorScriptCount + "个，测试通过" + ErrorOkTest + "个\n";
        outputString += "正确脚本共" + SuccessScriptCount + "个，测试通过" + SuccessOkTest + "个\n";
        outputString += "警告脚本共" + WarningScriptCount + "个，测试通过" + WarningOkTest + "个\n";

        if(ErrorOkTest == ErrorScriptCount && SuccessOkTest == SuccessScriptCount && WarningOkTest == WarningScriptCount) {
            LOG.INFO("测试通过");
            outputString += "测试通过\n";
        }else{
            LOG.ERROR("测试失败");
            outputString += "测试失败\n";
        }
        LOG.INFO("测试结束");
        outputString += "测试结束\n";

        // 写入文件
        try {
            // 输出结果到文件
            File OutputDir = new File(autoTestScriptConfig.getCompileOutputPath());
            // 创建测试结果文件 + 日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String date = simpleDateFormat.format(new Date());
            File OutputFile = new File(OutputDir, "TestCompile " + date + ".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(OutputFile);
            fileOutputStream.write(outputString.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            LOG.ERROR("写入测试结果失败");
        }
        // 是否清除脚本
        if(autoTestScriptConfig.getClearScriptAfterCompile()){
            clearDataSource();
        }
    }

    /**
     * 自动测试脚本
     * 测试编译器的响应
     */
    @Test
    void TestResponse(){
        Integer testPoint = 0;
        String totalOutputString = "";
        totalOutputString += TestINFO("开始测试响应\n");

        // 打印测试参数
        totalOutputString += TestINFO("测试参数:\n");
        totalOutputString += TestINFO("测试脚本路径:" + autoTestScriptConfig.getResponseScriptPath() + "\n");
        RandomGenerate = autoTestScriptConfig.getRandomGenerate();
        totalOutputString += TestINFO("是否随机生成测试:" + RandomGenerate + "\n");
        // 读取相应测试脚本
        String ResponseScriptsPath = autoTestScriptConfig.getResponseScriptPath();
        File ResponseScriptsDir = new File(ResponseScriptsPath);
        // 得到所有脚本文件夹
        File[] ResponseScriptsFiles = ResponseScriptsDir.listFiles();
        totalOutputString += TestINFO("测试脚本数量:" + ResponseScriptsFiles.length + "\n");
        totalOutputString += TestINFO("是否随机抽取测试:" + autoTestScriptConfig.getRandomTest() + "\n");
        // 是否随机抽取测试 考虑到测试时间过长，可以选择随机抽取测试
        Boolean isRandom = autoTestScriptConfig.getRandomTest();
        Boolean isTestBias = autoTestScriptConfig.getTestBias();
        Integer randomTestSkip = autoTestScriptConfig.getRandomTestSkip();
        Integer testBiasFor = autoTestScriptConfig.getTestBiasFor();
        // 处理随机测试
        if(isRandom){
            // 随机初始化 0 - randomTestSkip
            Random random = new Random();
            testPoint = random.nextInt(randomTestSkip);
        }
        Integer testRound = autoTestScriptConfig.getTestRound();
        String outputString = "";
        for(File ResponseScriptDir : ResponseScriptsFiles){
            outputString = "";
            File ResponseScript = new File(ResponseScriptDir,"script.txt");
            // 是否随机抽取测试
            outputString += TestINFO("开始测试" + ResponseScriptDir.getName());
            // 测试参数
            Integer totalTestPoint = 0;
            Integer passTestPoint = 0;
            Integer failTestPoint = 0;
            Integer unknownTestPoint = 0;

            String code = "";
            Integer robotId = -1;
            try {
                FileInputStream fileInputStream = new FileInputStream(ResponseScript);
                byte[] bytes = fileInputStream.readAllBytes();
                code = new String(bytes);
                NewDependencyDto newDependencyDto = new NewDependencyDto();
                newDependencyDto.setId(-1);
                newDependencyDto.setName(ResponseScript.getName());
                newDependencyDto.setDescription("测试响应脚本");
                newDependencyDto.setCode(code);
                // 编译
                Result result = dslCompiler.compileByCode(newDependencyDto);
                // 得到ID
                robotId = (Integer) result.getState();

            } catch (Exception e) {
                LOG.ERROR(ResponseScript.getName() + "编译响应脚本失败");
            }
            // 装配相应脚本依赖
            dependencyManager.checkOutDependency(robotId,0);
            // 响应映射 状态 - 输入映射
            Map<Integer, Map<String,TestUnitEntity>> testUnitEntityMap = new HashMap<>();

            // 序列化映射测试单元
            File testUnitEntityMapFile = new File(ResponseScriptDir,"UnitEntityMap");
            if(testUnitEntityMapFile.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(testUnitEntityMapFile);
                    // 反序列化
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    testUnitEntityMap = (Map<Integer, Map<String, TestUnitEntity>>) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();

                } catch (Exception e) {
                    LOG.ERROR(ResponseScript.getName() + "序列化映射失败");
                }
            }

            Map<Integer, Map<String,TestUnitEntity>> tempTestUnitEntityMap = new HashMap<>();
            // 得到 Customer中Robot 所有状态
            RobotDependency robotDependency = CustomerService.getRobotDependency();
            Map<String, Integer> stateMap = robotDependency.getStateMap();
            // 根据ID映射成状态名
            Map<Integer, String> stateIdMap = new HashMap<>();
            for(String stateName : stateMap.keySet()){
                Integer stateId = stateMap.get(stateName);
                stateIdMap.put(stateId,stateName);
            }

            Map<Integer, TransferList> transMap = robotDependency.getTransMap();

            // 得到所有状态的测试单元
            for(int i = 0;i < testRound; i++){
                // 测试轮数 每一轮应该相同
                for(String stateName : stateMap.keySet()){
                    Integer stateId = stateMap.get(stateName);
                    // 得到该状态对应的TransferMap
                    TransferList transferList = transMap.get(stateId);
                    List<TransferNode> transferNodes = transferList.getTransferList();

                    Integer tempState = stateId; // 防止状态被修改

                    // 遍历所有的转移表
                    for(TransferNode transferNode : transferNodes){
                        // 得到测试状态 给Customer装配状态
                        CustomerService.setStateInTest(stateId);
                        // 更新测试点
                        if(isRandom){
                            if(testPoint % randomTestSkip == 0){
                                testPoint++;
                                continue; // 越过测试点
                            }
                        }
                        // 得到Condition
                        Condition Condition = transferNode.getCondition();
                        if(isTestBias){
                            if(Condition.getType() != testBiasFor){
                                testPoint++;
                                continue; // 越过测试点
                            }
                        }

                        // 开始测试
                        totalTestPoint++;
                        testPoint++;
                        String response;
                        Integer resState;
                        Result result;
                        TestUnitEntity testUnitEntity = new TestUnitEntity();

                        // 判断类型 得到输出
                        String inputStr = getInputFromCondition(Condition);
                        if(inputStr == ""){
                            TestERROR("测试单元" + stateId + "输入为空");
                            return;
                        }
                        // 得到响应结果
                        result = CustomerService.ResponseGenerator(inputStr);
                        response = (String) result.getData();
                        resState = (Integer) result.getState();
                        // 填充测试单元
                        testUnitEntity.setState(tempState);
                        testUnitEntity.setName(stateIdMap.get(tempState));
                        testUnitEntity.setInput(inputStr);
                        testUnitEntity.setResponse(response);
                        testUnitEntity.setTargetId(resState);
                        testUnitEntity.setTargetName(stateIdMap.get(resState));
                        testUnitEntity.setCheckState(TestUnitConstant.TEST_UNKNOWN);

                        // 查看模板 Map映射中是否存在该单元
                        if(testUnitEntityMap.containsKey(tempState)){ // 存在
                            Map<String,TestUnitEntity> TestUnitMapFromState = testUnitEntityMap.get(tempState);
                            // 查看是否存在相同的输入
                            if(TestUnitMapFromState.containsKey(inputStr)){
                                // 存在
                                TestUnitEntity testUnitEntityFromInput = TestUnitMapFromState.get(inputStr);
                                // 比较响应
                                if(testUnitEntityFromInput.getResponse().equals(response)){
                                    // 相同
                                    testUnitEntity.setCheckState(TestUnitConstant.TEST_RIGHT);
                                }else{
                                    // 不同
                                    testUnitEntity.setCheckState(TestUnitConstant.TEST_WRONG);
                                }
                            }
                        }

                        // 查看临时Map中是否存在该单元 检查多次相应是否相同
                        if(tempTestUnitEntityMap.containsKey(tempState)){
                            // 存在
                            Map<String,TestUnitEntity> TestUnitMapFromState = tempTestUnitEntityMap.get(tempState);
                            // 查看是否存在相同的输入
                            if(TestUnitMapFromState.containsKey(inputStr)){
                                // 存在
                                TestUnitEntity testUnitEntityFromInput = TestUnitMapFromState.get(inputStr);
                                // 比较响应
                                if(!testUnitEntityFromInput.getResponse().equals(response)){
                                    // 不同
                                    testUnitEntity.setCheckState(TestUnitConstant.TEST_WRONG);
                                    TestERROR("测试单元" + stateId + "输入:" + inputStr + "多次响应不匹配");
                                }
                            }
                        }

                        // 更新到Map中
                        tempTestUnitEntityMap = updateTestUnitMap(stateId,inputStr,testUnitEntity,tempTestUnitEntityMap);

                        // 检查testUnitEntity状态 更新统计数据
                        if(testUnitEntity.getCheckState() == TestUnitConstant.TEST_RIGHT){
                            passTestPoint++;
                            TestINFO("测试状态 " + stateIdMap.get(stateId) + " 输入: " + inputStr + " 返回结果：" + testUnitEntity.getResponse() +"->响应正确");
                        }else if(testUnitEntity.getCheckState() == TestUnitConstant.TEST_WRONG){
                            failTestPoint++;
                            TestERROR("测试状态 " + stateIdMap.get(stateId) + " 输入: " + inputStr + " 返回结果：" + testUnitEntity.getResponse() + "->响应错误");
                        }else{
                            unknownTestPoint++;
                            TestINFO("测试状态 " + stateIdMap.get(stateId) + " 输入: " + inputStr + " 返回结果：" + testUnitEntity.getResponse() + "->响应未知");
                        }
                    }
                    outputString += TestINFO("测试状态 " + stateIdMap.get(stateId) + " 测试完毕");
                }
                outputString += TestINFO("测试轮数 " + i + " 测试完毕");
            }

            // 测试default

            // 测试begin

            // 测试wait

            // 测试一个脚本结束 统计测试数据
            outputString += TestINFO("测试脚本 " + ResponseScriptsDir.getName() + " 测试完毕");
            outputString += TestINFO("测试点数 " + totalTestPoint);
            outputString += TestINFO("测试通过点数 " + passTestPoint );
            outputString += TestINFO("测试失败点数 " + failTestPoint );
            outputString += TestINFO("测试未知点数 " + unknownTestPoint );

            // 保存测试结果
            SaveTestResponseData(ResponseScriptsDir.getName(),outputString,tempTestUnitEntityMap);
            totalOutputString += outputString;
        }
        // 保存 totalOutputString 测试结果 到文件中
        SaveTestResponseData("total",totalOutputString,null);
    }

    private String ParseResultFromResult (Result result) {
        // 以注释的形式把结果 写回到文件首部
        String msg = result.getMsg();

        List<String> warningMsgs = new ArrayList<>();
        List<Integer> warningLines = new ArrayList<>();

        String Result = "编译成功";

        List<CompileWarning> compileWarnings = (List<CompileWarning>) result.getData();
        if(compileWarnings != null && compileWarnings.size() > 0){
            for(CompileWarning compileWarning : compileWarnings){
                warningMsgs.add(compileWarning.getMsg());
                warningLines.add(compileWarning.getWarningLine());
            }
        }

        // 构建注释 用单行注释的形式
        String annotation = "";
        annotation += "// " + Result + "\n";
        // 统计错误和警告个数
        int errorCount = 0;
        int warningCount = 0;

        if(warningMsgs.size() > 0){
            warningCount += warningMsgs.size();
        }

        annotation += "// " + "错误" + errorCount + "个" + "警告" + warningCount + "个" + "\n";

        if(warningMsgs.size() > 0){
            for(int i = 0;i < warningMsgs.size();i++){
                annotation += "// " + warningMsgs.get(i) + " 位于第" + warningLines.get(i) + "行\n";
            }
        }
        return annotation;
    }

    private String ParseResultFromException(Exception e){

        if(e == null){
            return "";
        }

        CompileErrorException compileErrorException = (CompileErrorException) e;

        int errorCount = 1;
        int warningCount = 0;

        Integer errorLine = compileErrorException.line;
        String errorMsg = compileErrorException.errorMsg;
        List<CompileWarning> warnings = compileErrorException.warnings;

        String Result = "编译失败";
        String annotation = "";
        annotation += "// " + Result + "\n";
        if(warnings != null && warnings.size() > 0){
            warningCount = warnings.size();
        }
        annotation += "// " + "错误" + errorCount + "个" + "警告" + warningCount + "个" + "\n";

        annotation += "// " + errorMsg + " 位于第" + errorLine + "行\n";

        if(warnings != null && warnings.size() > 0){
            for(CompileWarning warning : warnings){
                annotation += "// " + warning.getMsg() + " 位于第" + warning.getWarningLine() + "行\n";
            }
        }

        return  annotation;
    }


    /**
     * 清除数据库中的数据
     */
    private void clearDataSource(){
        List<DependencyMap> dependencyMaps = dependencyMapMapper.selectList(null);
        for(DependencyMap dependencyMap : dependencyMaps){
            // 清除在数据库中的数据
            dependencyMapMapper.deleteById(dependencyMap.getId());
            // 清除在本地的数据
            String path = SystemConstant.ROBOT_DEPENDENCY_PATH + dependencyMap.getPath();
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }

            path = SystemConstant.INPUT_PATH + dependencyMap.getCode();
            file = new File(path);
            if(file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 清除测试结果
     * @return
     */
    @Test
    void clearTestResult(){
        clearTestCompileResult();
        clearTestResponseResult();
    }

    private void clearTestCompileResult(){
        // 删除目录下面的所有文件
        String path = autoTestScriptConfig.getCompileOutputPath();
        File file = new File(path);
        if(file.exists()){
            File[] files = file.listFiles();
            for(File file1 : files){
                file1.delete();
            }
        }
    }

    private void clearTestResponseResult(){
        // 删除目录下面的所有目录
        String path = autoTestScriptConfig.getResponseOutputPath();
        File file = new File(path);

        // 先删除目录下面的所有文件
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null) return;
            for(File file1 : files){
                file1.delete();
            }
            for(File file1 : files){
                // 删除目录下面的所有文件 以及 目录
                File[] files1 = file1.listFiles();
                if(files1 == null) continue;
                for(File file2 : files1){
                    file2.delete();
                }
                file1.delete();
            }
        }
    }

    // 引用传递 outputStr
    private String TestINFO(String input) {
        LOG.INFO(input);
        return input + "\n";
    }

    private String TestERROR(String input) {
        LOG.ERROR(input);
        return input + "\n";
    }

    // 修改测试单元Map
    private Map<Integer, Map<String,TestUnitEntity>> updateTestUnitMap(Integer state,String input,TestUnitEntity entity,Map<Integer, Map<String,TestUnitEntity>> map){
        if(map.containsKey(state)){
            Map<String,TestUnitEntity> testUnitEntityMap = map.get(state);
            testUnitEntityMap.put(input,entity);
        }else{
            Map<String,TestUnitEntity> testUnitEntityMap = new HashMap<>();
            testUnitEntityMap.put(input,entity);
            map.put(state,testUnitEntityMap);
        }
        return map;
    }

    // 根据条件模拟输入
    private String getInputFromCondition(Condition condition){
        String input = "";
        switch (condition.getType()){
            case ConditionConstant.INPUT:
                List<String> inputs = condition.getREGEX();
                for(int i = 0;i < inputs.size();i++){
                    if(inputs.get(i).startsWith("\"") && inputs.get(i).endsWith("\"")){
                        input += inputs.get(i).substring(1,inputs.get(i).length() - 1);
                    }else{
                        input += getRandomInputID();
                    }
                }
                return input;
            case ConditionConstant.JUDGE_EXACT:
                input = condition.getREGEX().get(0);
                return input;
            case ConditionConstant.JUDGE_REGEX:
                // 根据正则表达式生成一个字符串
                String regex = condition.getREGEX().get(0);
                regex.replace(".*?",getRandomInputID());
                regex.replace(".*",getRandomInputID());
                regex.replace(".+",getRandomInputID());
                // 其他正则标识为空
                regex.replace(".","");
                regex.replace("*","");
                regex.replace("+","");
                regex.replace("?","");
                regex.replace("|","");
                regex.replace("^","");
                regex.replace("$","");
                return regex;
            case ConditionConstant.JUDGE_CONTAIN:
                // 包含匹配 也是只有一个关键词
                input = condition.getREGEX().get(0);
                // 去除两旁的 .* 如果存在的话
                if(input.startsWith(".*")){
                    input = input.substring(2);
                    input = getRandomInputHead() + input;
                }
                if(input.endsWith(".*")){
                    input = input.substring(0,input.length() - 2);
                    input = input + getRandomInputTail();
                }
                return input;
            default:
                return "";
        }
    }

    private String getRandomInputID(){
        String[] inputs = {"范小勤","北邮","清华大学","北京大学","淘宝","比亚迪","未知"};
        Random random = new Random();
        if(!RandomGenerate) return inputs[0];
        int index = random.nextInt(inputs.length);
        return inputs[index];
    }

    private String getRandomInputHead(){
        String[] heads = {"我想","我要","我想要","你","你能"};
        Random random = new Random();
        if(!RandomGenerate) return heads[0];
        int index = random.nextInt(heads.length);
        return heads[index];
    }

    private String getRandomInputTail(){
        String[] tails = {"吗","么","呢","啊","呀","咧","呵"};
        Random random = new Random();
        if(!RandomGenerate) return tails[0];
        int index = random.nextInt(tails.length);
        return tails[index];
    }

    private void SaveTestResponseData(String name,String outputString,Map<Integer,Map<String,TestUnitEntity>> map){
        // 获得相应测试输出目录
        String path = autoTestScriptConfig.getResponseOutputPath();

        // 创建新的文件夹 和 日期组合
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = sdf.format(new Date());

        // 保存总的测试结果
        if(map == null){
            // 保存outputString到文件
            String Name = name + " In " + date + ".txt";
            File file = new File(path + Name);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(outputString.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        String DirName = name + " In " + date;

        File file = new File(path + DirName);

        if(!file.exists()){
            file.mkdirs();
        }

        // 1. 输出信息 到 测试结果文件
        File resultFile = new File(file ,"测试结果.txt");
        try {
            if(!resultFile.exists()){
                resultFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(resultFile);
            fos.write(outputString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. map 可视化到 测试点集文件
        File testPointFile = new File(file , "测试点集.txt");
        try {
            if(!testPointFile.exists()){
                testPointFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(testPointFile);
            String testPointString = "";
            for(Integer state : map.keySet()){
                Map<String,TestUnitEntity> testUnitEntityMap = map.get(state);
                for(String input : testUnitEntityMap.keySet()){
                    TestUnitEntity entity = testUnitEntityMap.get(input);
                    testPointString += entity.toString() + '\n';
                }
            }

            fos.write(testPointString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3.修改所有的 结果为RIGHT  map 序列化到 测试点映射依赖 可以后面拖到check中作为依赖
        for(Integer state : map.keySet()) {
            Map<String, TestUnitEntity> testUnitEntityMap = map.get(state);
            for (String input : testUnitEntityMap.keySet()) {
                TestUnitEntity entity = testUnitEntityMap.get(input);
                entity.setCheckState(TestUnitConstant.TEST_RIGHT);
            }
        }

        // 4.序列化到文件 名称为 name
        File testPointMapFile = new File(file,  "UnitEntityMap");
        try {
            if(!testPointMapFile.exists()){
                testPointMapFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(testPointMapFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.INFO("测试结果保存成功");
    }
}
