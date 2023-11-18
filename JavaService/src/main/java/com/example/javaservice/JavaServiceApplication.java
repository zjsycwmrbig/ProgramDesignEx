package com.example.javaservice;

import com.example.javaservice.Core.CustomerService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@MapperScan("com.example.javaservice.Mapper")
public class JavaServiceApplication {

    public static void main(String[] args) {
        CustomerService.thread.start();  // 单独超时 响应线程
        SpringApplication.run(JavaServiceApplication.class, args);
    }

}
