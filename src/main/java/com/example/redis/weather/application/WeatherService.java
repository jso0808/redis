package com.example.redis.weather.application;

import com.example.redis.weather.domain.RegionWeatherSnapshot;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final double ALERT_THRESHOLD = 3.0;

    private final WeatherProvider weatherProvider;
    private final WeatherCachePort weatherCachePort;
    private final WeatherAlertPublisher weatherAlertPublisher;

    public WeatherService(
            WeatherProvider weatherProvider,
            WeatherCachePort weatherCachePort,
            WeatherAlertPublisher weatherAlertPublisher
    ) {
        this.weatherProvider = weatherProvider;
        this.weatherCachePort = weatherCachePort;
        this.weatherAlertPublisher = weatherAlertPublisher;
    }

    public RegionWeatherSnapshot getCurrent(String regionCode) {
        return weatherCachePort.get(regionCode)
                .orElseGet(() -> refresh(regionCode));
    }

    public RegionWeatherSnapshot refresh(String regionCode) {
        RegionWeatherSnapshot fetched = weatherProvider.fetchCurrent(regionCode);
        weatherCachePort.get(regionCode).ifPresent(prev -> publishIfChanged(prev, fetched));
        weatherCachePort.save(fetched);
        return fetched;
    }

    private void publishIfChanged(RegionWeatherSnapshot before, RegionWeatherSnapshot after) {
        if (Math.abs(before.temperature() - after.temperature()) >= ALERT_THRESHOLD) {
            weatherAlertPublisher.publishTemperatureChanged(before, after);
        }
    }
}
