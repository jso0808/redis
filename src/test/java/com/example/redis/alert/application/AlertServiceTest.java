package com.example.redis.alert.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.redis.alert.domain.WeatherAlert;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlertServiceTest {

    private FakeAlertHistoryPort historyPort;
    private FakeAlertNotifier notifier;
    private AlertService service;

    @BeforeEach
    void setUp() {
        historyPort = new FakeAlertHistoryPort();
        notifier = new FakeAlertNotifier();
        service = new AlertService(historyPort, notifier);
    }

    @Test
    void publishStoresAndNotifies() {
        WeatherAlert alert = new WeatherAlert("11B00000", "TEMP_SPIKE", "기온 급상승", Instant.now());

        service.publish(alert);

        assertThat(historyPort.alerts).hasSize(1);
        assertThat(notifier.notified).hasSize(1);
    }

    static class FakeAlertHistoryPort implements AlertHistoryPort {
        private final List<WeatherAlert> alerts = new ArrayList<>();

        @Override
        public void append(WeatherAlert alert) {
            alerts.add(alert);
        }

        @Override
        public List<WeatherAlert> recent(String regionCode, int limit) {
            return alerts.stream().filter(a -> a.regionCode().equals(regionCode)).toList();
        }
    }

    static class FakeAlertNotifier implements AlertNotifier {
        private final List<WeatherAlert> notified = new ArrayList<>();

        @Override
        public void notify(WeatherAlert alert) {
            notified.add(alert);
        }
    }
}
