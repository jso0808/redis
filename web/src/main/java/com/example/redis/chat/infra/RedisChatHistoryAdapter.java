package com.example.redis.chat.infra;

import com.example.redis.chat.application.ChatHistoryPort;
import com.example.redis.chat.domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisChatHistoryAdapter implements ChatHistoryPort {

    private static final int MAX_MESSAGES = 200;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatHistoryAdapter(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void append(ChatMessage message) {
        String key = key(message.regionCode());
        redisTemplate.opsForList().rightPush(key, toJson(message));
        redisTemplate.opsForList().trim(key, -MAX_MESSAGES, -1);
    }

    @Override
    public List<ChatMessage> recent(String regionCode, int limit) {
        List<String> raw = redisTemplate.opsForList().range(key(regionCode), -limit, -1);
        if (raw == null) {
            return Collections.emptyList();
        }
        return raw.stream().map(this::fromJson).toList();
    }

    private String key(String regionCode) {
        return "chat:region:" + regionCode + ":messages";
    }

    private String toJson(ChatMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize chat message", e);
        }
    }

    private ChatMessage fromJson(String json) {
        try {
            return objectMapper.readValue(json, ChatMessage.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize chat message", e);
        }
    }
}
