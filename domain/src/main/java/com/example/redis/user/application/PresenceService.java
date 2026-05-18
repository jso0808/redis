package com.example.redis.user.application;

import org.springframework.stereotype.Service;

@Service
public class PresenceService {

    private final PresencePort presencePort;

    public PresenceService(PresencePort presencePort) {
        this.presencePort = presencePort;
    }

    public void join(String regionCode, String userId) {
        presencePort.join(regionCode, userId);
    }

    public void leave(String regionCode, String userId) {
        presencePort.leave(regionCode, userId);
    }

    public long onlineCount(String regionCode) {
        return presencePort.onlineCount(regionCode);
    }
}
