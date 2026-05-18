package com.example.redis.alert.infra;

import com.example.redis.alert.application.AlertHistoryPort;
import com.example.redis.alert.domain.WeatherAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisAlertHistoryAdapter implements AlertHistoryPort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisAlertHistoryAdapter(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void append(WeatherAlert alert) {
        redisTemplate.opsForStream().add("stream:weather-alerts", Collections.singletonMap("payload", toJson(alert)));
    }

    @Override
    public List<WeatherAlert> recent(String regionCode, int limit) {
        List<String> rows = redisTemplate.opsForList().range("alerts:region:" + regionCode, -limit, -1);
        if (rows == null) {
            return Collections.emptyList();
        }
        return rows.stream().map(this::fromJson).toList();
    }

    private String toJson(WeatherAlert alert) {
        try {
            return objectMapper.writeValueAsString(alert);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize alert", e);
        }
    }

    private WeatherAlert fromJson(String json) {
        try {
            return objectMapper.readValue(json, WeatherAlert.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize alert", e);
        }
    }
}
