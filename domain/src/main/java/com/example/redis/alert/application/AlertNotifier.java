package com.example.redis.alert.application;

import com.example.redis.alert.domain.WeatherAlert;

public interface AlertNotifier {
    void notify(WeatherAlert alert);
}
