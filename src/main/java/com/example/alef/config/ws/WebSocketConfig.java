package com.example.alef.config.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // брокер для подписки
        config.enableSimpleBroker("/topic", "/user");

        // Префикс для отправки сообщений с клиента
        config.setApplicationDestinationPrefixes("/app");

        // Префикс для приватных сообщений
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Точка подключения WebSocket
        registry.addEndpoint("/ws-game")
                .setAllowedOriginPatterns("*");
    }


}
