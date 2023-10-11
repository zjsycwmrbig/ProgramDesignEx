package com.example.javaservice.Controller;

import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Pojo.Dto.ReceiveMessageDTO;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageListener {
    @PostMapping("/submit")
    public Result submit(@RequestBody ReceiveMessageDTO receiveMessageDTO) {
        LOG.INFO("收到消息：" + receiveMessageDTO.getMessage());
        // 这里的message理论上永远不可能是空,后面查询不到时返回建议
        return CustomerService.ResponseGenerator(receiveMessageDTO.getMessage());
    }


}
