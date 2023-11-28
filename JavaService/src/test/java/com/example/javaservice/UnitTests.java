package com.example.javaservice;
import com.example.javaservice.Constant.SystemConstant;
import com.example.javaservice.Core.*;
import com.example.javaservice.Core.impl.DSLCompilerImpl;
import com.example.javaservice.Core.impl.LexicalAnalyzerImpl;
import com.example.javaservice.Core.impl.SemanticAnalyzerImpl;
import com.example.javaservice.Core.impl.SyntaxAnalyzerIpml;
import com.example.javaservice.Mapper.DependencyMapMapper;
import com.example.javaservice.Mapper.TelephoneMapMapper;
import com.example.javaservice.Pojo.Entity.AbstractSyntaxTree;
import com.example.javaservice.Pojo.Entity.DependencyMap;
import com.example.javaservice.Pojo.Entity.RobotDependency;
import com.example.javaservice.Pojo.Entity.Tokens;
import com.example.javaservice.Pojo.SQLDATA.TelephoneMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SpringBootTest
class UnitTests {

    @Autowired
    DependencyMapMapper dependencyMapMapper;

    @Autowired
    TelephoneMapMapper telephoneMapMapper;

    @Test
    void contextLoads() {
    }

    /**
     * 自动测试脚本
     */
    @Test
    void TestAutoScript(){
        // 读取文件
        File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file, StandardCharsets.UTF_8.name());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String code = "";
        while (scanner.hasNextLine()) {
            code += scanner.nextLine();
        }
        // 词法分析
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
        Tokens tokens = lexicalAnalyzer.analyze(code);
        // 语法分析
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerIpml();
        AbstractSyntaxTree abstractSyntaxTree = syntaxAnalyzer.analyze(tokens.getStream());
        // 语义分析
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzerImpl();
//        semanticAnalyzer.analyze(abstractSyntaxTree);
        // 生成DSL
//        DSLCompiler dslCompiler = new DSLCompilerImpl();
//        String dsl = dslCompiler.compile(abstractSyntaxTree);
//        System.out.println(dsl);
    }


    /**
     * 正则匹配测试
     */
    @Test
    void TestStrMatch () {
        String str = "sb游戏dsb愿神";
        String regex = "^[0-9]*$";
        regex = "sb(.*)dsb(.*)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(str);

        if(matcher.find()){
            //
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }

    /**
     * 测试数据库存储
     */
    @Test
    void TestDBTransfer(){

    }

    /**
     * 测试词法分析器
     */
    @Test
    void TestLexicalAnalyzer(){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
        // 把文件转换成字符串
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream( SystemConstant.INPUT_PATH + "test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes = null;
        // 字符流转换
        try {
            bytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bytes = new String(bytes).getBytes(StandardCharsets.UTF_8);
        String code = new String(bytes);

        Tokens tokens = lexicalAnalyzer.analyze(code);
        lexicalAnalyzer.logPrint(tokens);
    }

    /**
     * 测试语法分析器
     */
    @Test
    void TestSyntaxAnalyzer(){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
        // 把文件转换成字符串
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream( SystemConstant.INPUT_PATH + "test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes = null;
        // 字符流转换
        try {
            bytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bytes = new String(bytes).getBytes(StandardCharsets.UTF_8);
        String code = new String(bytes);


        Tokens tokens = lexicalAnalyzer.analyze(code);
        lexicalAnalyzer.logPrint(tokens);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerIpml();
        AbstractSyntaxTree ast = syntaxAnalyzer.analyze(tokens.getStream());
        ast.logPrint();
    }


    /**
     * 测试语义分析器
     */
    @Test
    RobotDependency TestSemanticAnalyzer(){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl();
        // 把文件转换成字符串
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream( SystemConstant.INPUT_PATH + "test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes = null;
        // 字符流转换
        try {
            bytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bytes = new String(bytes).getBytes(StandardCharsets.UTF_8);
        String code = new String(bytes);

        Tokens tokens = lexicalAnalyzer.analyze(code);
        lexicalAnalyzer.logPrint(tokens);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerIpml();
        AbstractSyntaxTree ast = syntaxAnalyzer.analyze(tokens.getStream());
        ast.logPrint();

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzerImpl();
        Map<String, Object> analyse = semanticAnalyzer.analyse(ast);

        ((SemanticAnalyzerImpl) semanticAnalyzer).logPrint();

        return (RobotDependency) analyse.get("robotDependency");
    }

    /**
     * 测试CS系统
     */
    @Test
    public void testCS(){
        RobotDependency robotDependency = TestSemanticAnalyzer();
        robotDependency.setDependencyId("TestDependency");
        DSLCompiler dslCompiler = new DSLCompilerImpl();
        dslCompiler.SaveDependency(robotDependency,null);
        // 保存
        CustomerService.testAssembly(robotDependency);

        // 从文件读取输入
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream( SystemConstant.INPUT_PATH + "input1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 按照行读取
        Scanner scanner = new Scanner(fileInputStream);
        while (scanner.hasNextLine()){
            String input = scanner.nextLine();
        }
    }

    /**
     * 测试依赖存储
     */
    @Test
    void testDB(){
        DependencyMap dependencyMap = new DependencyMap();
        dependencyMap.setCode("test");
        dependencyMap.setPath("test");
        dependencyMap.setName("test");

        dependencyMapMapper.insert(dependencyMap);

        DependencyMap dependencyMap1 = new DependencyMap();
        dependencyMap1.setCode("test1");
        dependencyMap1.setPath("test1");
        dependencyMap1.setName("test1");

        dependencyMapMapper.insert(dependencyMap1);
    }

    /**
     * 清除数据库中的数据
     */
    @Test
    void clearDataSource(){
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
     * 进行电话号码存储
     */
    @Test
    void addTelePhone(){
        telephoneMapMapper.insert(new TelephoneMap("淘宝",123435445));
        telephoneMapMapper.insert(new TelephoneMap("比亚迪",123456789));
        telephoneMapMapper.insert(new TelephoneMap("京东",1234562332));
        telephoneMapMapper.insert(new TelephoneMap("苏宁",1234567233));
        telephoneMapMapper.insert(new TelephoneMap("美团",1234567234));
        telephoneMapMapper.insert(new TelephoneMap("饿了么",1234567235));
        telephoneMapMapper.insert(new TelephoneMap("滴滴",1234567236));
        telephoneMapMapper.insert(new TelephoneMap("美团外卖",1234567237));
        telephoneMapMapper.insert(new TelephoneMap("北邮",1234567238));
        telephoneMapMapper.insert(new TelephoneMap("山东大学",1234567239));
        telephoneMapMapper.insert(new TelephoneMap("北京大学",1234567240));
        telephoneMapMapper.insert(new TelephoneMap("清华大学",1234567241));
        telephoneMapMapper.insert(new TelephoneMap("浙江大学",1234567242));
        telephoneMapMapper.insert(new TelephoneMap("复旦大学",1234567243));
        telephoneMapMapper.insert(new TelephoneMap("南京大学",1234567244));
        telephoneMapMapper.insert(new TelephoneMap("武汉大学",1234567245));
        telephoneMapMapper.insert(new TelephoneMap("中山大学",1234567246));
        telephoneMapMapper.insert(new TelephoneMap("范小勤",1234567247));
        telephoneMapMapper.insert(new TelephoneMap("李小勤",1234567248));
        telephoneMapMapper.insert(new TelephoneMap("王小勤",1234567249));
    }

}
