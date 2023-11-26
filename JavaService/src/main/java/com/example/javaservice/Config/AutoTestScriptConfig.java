package com.example.javaservice.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auto-test-script") // 读取配置文件中的 auto-test-script 配置项
@Data
public class AutoTestScriptConfig {
    String totalScriptsPath;
    String errorScriptsPath;
    String successScriptsPath;
    String warningScriptsPath;
    String compileOutputPath;
    String responseOutputPath;
    String responseScriptPath;
    Boolean testBias;
    Integer testBiasFor;
    Integer testRound;
    Integer testNumber;
    Boolean randomTest;
    Integer randomTestSkip;
    Boolean clearScriptAfterCompile;
    Boolean randomGenerate;
}
