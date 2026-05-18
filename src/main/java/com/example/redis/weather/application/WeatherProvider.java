package com.example.redis.weather.application;

import com.example.redis.weather.domain.RegionWeatherSnapshot;

public interface WeatherProvider {
    RegionWeatherSnapshot fetchCurrent(String regionCode);
}
