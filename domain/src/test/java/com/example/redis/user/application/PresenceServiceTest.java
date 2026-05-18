package com.example.redis.user.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresenceServiceTest {

    private FakePresencePort port;
    private PresenceService service;

    @BeforeEach
    void setUp() {
        port = new FakePresencePort();
        service = new PresenceService(port);
    }

    @Test
    void joinAndLeaveUpdateRegionalOnlineCount() {
        service.join("11B00000", "u1");
        service.join("11B00000", "u2");

        assertThat(service.onlineCount("11B00000")).isEqualTo(2);

        service.leave("11B00000", "u1");

        assertThat(service.onlineCount("11B00000")).isEqualTo(1);
    }

    static class FakePresencePort implements PresencePort {
        private final Map<String, Set<String>> online = new HashMap<>();

        @Override
        public void join(String regionCode, String userId) {
            online.computeIfAbsent(regionCode, k -> new HashSet<>()).add(userId);
        }

        @Override
        public void leave(String regionCode, String userId) {
            online.computeIfAbsent(regionCode, k -> new HashSet<>()).remove(userId);
        }

        @Override
        public long onlineCount(String regionCode) {
            return online.getOrDefault(regionCode, Set.of()).size();
        }
    }
}
