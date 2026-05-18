package com.example.redis.user.infra;

import com.example.redis.user.application.PresencePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisPresenceAdapter implements PresencePort {

    private final StringRedisTemplate redisTemplate;

    public RedisPresenceAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void join(String regionCode, String userId) {
        redisTemplate.opsForSet().add(regionKey(regionCode), userId);
    }

    @Override
    public void leave(String regionCode, String userId) {
        redisTemplate.opsForSet().remove(regionKey(regionCode), userId);
    }

    @Override
    public long onlineCount(String regionCode) {
        Long size = redisTemplate.opsForSet().size(regionKey(regionCode));
        return size == null ? 0L : size;
    }

    private String regionKey(String regionCode) {
        return "presence:region:" + regionCode;
    }
}
