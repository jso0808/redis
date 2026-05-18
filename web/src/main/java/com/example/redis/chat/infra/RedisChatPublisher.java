package com.example.redis.chat.infra;

import com.example.redis.chat.application.ChatPublisher;
import com.example.redis.chat.domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisChatPublisher implements ChatPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatPublisher(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(ChatMessage message) {
        redisTemplate.convertAndSend(channel(message.regionCode()), toJson(message));
    }

    public static String channel(String regionCode) {
        return "chat.region." + regionCode;
    }

    private String toJson(ChatMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize chat message", e);
        }
    }
}
