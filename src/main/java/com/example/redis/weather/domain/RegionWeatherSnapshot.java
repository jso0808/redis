package com.example.redis.weather.domain;

import java.time.Instant;

public record RegionWeatherSnapshot(
        String regionCode,
        double temperature,
        int humidity,
        boolean raining,
        Instant observedAt
) {
}
