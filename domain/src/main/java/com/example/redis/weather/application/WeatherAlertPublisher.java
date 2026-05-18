package com.example.redis.weather.application;

import com.example.redis.weather.domain.RegionWeatherSnapshot;

public interface WeatherAlertPublisher {
    void publishTemperatureChanged(RegionWeatherSnapshot before, RegionWeatherSnapshot after);
}
