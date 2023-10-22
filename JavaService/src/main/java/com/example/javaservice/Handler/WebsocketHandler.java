package com.example.javaservice.Handler;

import com.example.javaservice.Core.CustomerService;
import com.example.javaservice.Result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@CrossOrigin(origins = "*", maxAge = 3600)
public class WebsocketHandler extends TextWebSocketHandler{


    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        CustomerService.client = session;
        System.out.println("收到消息：" + message.getPayload());
        // TODO 保障这个CS系统的完整性
        Result res = CustomerService.ResponseGenerator(message.getPayload());
        String json = objectMapper.writeValueAsString(res);
        session.sendMessage(new TextMessage(json));
    }

    // 连接成功
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("连接成功");
        CustomerService.client = session;
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("连接关闭");
        CustomerService.client = null;

    }
}
