package com.example.redis.chat.domain;

import java.time.Instant;

public record ChatMessage(
        String regionCode,
        String senderId,
        String content,
        Instant sentAt
) {
}
