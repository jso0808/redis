package com.example.redis.weather.infra;

import com.example.redis.alert.application.AlertService;
import com.example.redis.alert.domain.WeatherAlert;
import com.example.redis.weather.application.WeatherAlertPublisher;
import com.example.redis.weather.domain.RegionWeatherSnapshot;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class AlertServiceWeatherAlertPublisher implements WeatherAlertPublisher {

    private final AlertService alertService;

    public AlertServiceWeatherAlertPublisher(AlertService alertService) {
        this.alertService = alertService;
    }

    @Override
    public void publishTemperatureChanged(RegionWeatherSnapshot before, RegionWeatherSnapshot after) {
        String message = String.format("기온 변화 %.1f -> %.1f", before.temperature(), after.temperature());
        alertService.publish(new WeatherAlert(after.regionCode(), "TEMP_SPIKE", message, Instant.now()));
    }
}
