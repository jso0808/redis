package com.example.redis.alert.application;

import com.example.redis.alert.domain.WeatherAlert;
import java.util.List;

public interface AlertHistoryPort {
    void append(WeatherAlert alert);

    List<WeatherAlert> recent(String regionCode, int limit);
}
