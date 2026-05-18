package com.example.redis.weather.infra;

import com.example.redis.weather.application.WeatherCachePort;
import com.example.redis.weather.domain.RegionWeatherSnapshot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisWeatherCacheAdapter implements WeatherCachePort {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisWeatherCacheAdapter(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<RegionWeatherSnapshot> get(String regionCode) {
        String json = redisTemplate.opsForValue().get(key(regionCode));
        if (json == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, RegionWeatherSnapshot.class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse cached weather", e);
        }
    }

    @Override
    public void save(RegionWeatherSnapshot snapshot) {
        try {
            redisTemplate.opsForValue().set(key(snapshot.regionCode()), objectMapper.writeValueAsString(snapshot), TTL);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize weather", e);
        }
    }

    private String key(String regionCode) {
        return "weather:region:" + regionCode + ":current";
    }
}
