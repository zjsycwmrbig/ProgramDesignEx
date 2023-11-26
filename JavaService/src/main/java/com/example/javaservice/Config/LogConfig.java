package com.example.javaservice.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "log") // 读取配置文件中的 auto-test-script 配置项
@Data
public class LogConfig {
    String level;
    String path;
    Boolean showStackFrame;
    Boolean ShowTime;
}
