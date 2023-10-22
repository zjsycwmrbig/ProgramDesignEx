package com.example.javaservice.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 设置针对websocketHandler的跨域设置
        registry.addHandler(new com.example.javaservice.Handler.WebsocketHandler(), "/cs_robot").setAllowedOrigins("*");

    }

    // 其他配置...
}