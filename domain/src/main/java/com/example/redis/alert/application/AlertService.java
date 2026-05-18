package com.example.redis.alert.application;

import com.example.redis.alert.domain.WeatherAlert;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final AlertHistoryPort alertHistoryPort;
    private final AlertNotifier alertNotifier;

    public AlertService(AlertHistoryPort alertHistoryPort, AlertNotifier alertNotifier) {
        this.alertHistoryPort = alertHistoryPort;
        this.alertNotifier = alertNotifier;
    }

    public void publish(WeatherAlert alert) {
        alertHistoryPort.append(alert);
        alertNotifier.notify(alert);
    }

    public List<WeatherAlert> recent(String regionCode, int limit) {
        return alertHistoryPort.recent(regionCode, limit);
    }
}
