package com.example.redis.alert.domain;

import java.time.Instant;

public record WeatherAlert(
        String regionCode,
        String type,
        String message,
        Instant occurredAt
) {
}
