package com.example.redis.user.application;

public interface PresencePort {
    void join(String regionCode, String userId);

    void leave(String regionCode, String userId);

    long onlineCount(String regionCode);
}
