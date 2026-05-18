package com.example.redis.weather.application;

import com.example.redis.weather.domain.RegionWeatherSnapshot;
import java.util.Optional;

public interface WeatherCachePort {
    Optional<RegionWeatherSnapshot> get(String regionCode);

    void save(RegionWeatherSnapshot snapshot);
}
