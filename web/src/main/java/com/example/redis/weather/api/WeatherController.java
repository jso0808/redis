package com.example.redis.weather.api;

import com.example.redis.weather.application.WeatherService;
import com.example.redis.weather.domain.RegionWeatherSnapshot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions/{regionCode}/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public RegionWeatherSnapshot current(@PathVariable String regionCode) {
        return weatherService.getCurrent(regionCode);
    }

    @PostMapping("/refresh")
    public RegionWeatherSnapshot refresh(@PathVariable String regionCode) {
        return weatherService.refresh(regionCode);
    }
}
