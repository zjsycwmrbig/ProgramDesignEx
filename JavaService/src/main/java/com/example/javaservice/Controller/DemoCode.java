package com.example.javaservice.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

@RestController
public class DemoCode {
    @GetMapping("/DemoCode")
    public String DemoCode() {
        // 读取文件 DemoCode.txt 里面的内容 返回
        // src/main/resources/static/DemoCode.txt
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src/main/resources/static/DemoCode.txt");
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "读取文件失败";
        }
    }
}
