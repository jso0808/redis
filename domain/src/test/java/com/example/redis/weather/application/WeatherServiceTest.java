package com.example.redis.weather.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.redis.weather.domain.RegionWeatherSnapshot;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {

    private FakeWeatherProvider provider;
    private FakeWeatherCache cache;
    private FakeWeatherAlertPublisher publisher;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        provider = new FakeWeatherProvider();
        cache = new FakeWeatherCache();
        publisher = new FakeWeatherAlertPublisher();
        weatherService = new WeatherService(provider, cache, publisher);
    }

    @Test
    void getCurrentReturnsCachedValueOnCacheHit() {
        RegionWeatherSnapshot cached = new RegionWeatherSnapshot("11B00000", 20.0, 40, false, Instant.now());
        cache.put(cached);

        RegionWeatherSnapshot result = weatherService.getCurrent("11B00000");

        assertThat(result.temperature()).isEqualTo(20.0);
        assertThat(provider.called).isFalse();
    }

    @Test
    void getCurrentFetchesAndCachesOnCacheMiss() {
        provider.next = new RegionWeatherSnapshot("11B00000", 23.0, 35, false, Instant.now());

        RegionWeatherSnapshot result = weatherService.getCurrent("11B00000");

        assertThat(result.temperature()).isEqualTo(23.0);
        assertThat(provider.called).isTrue();
        assertThat(cache.get("11B00000")).isPresent();
    }

    @Test
    void refreshPublishesAlertOnLargeTemperatureChange() {
        cache.put(new RegionWeatherSnapshot("11B00000", 20.0, 35, false, Instant.now()));
        provider.next = new RegionWeatherSnapshot("11B00000", 23.5, 30, true, Instant.now());

        weatherService.refresh("11B00000");

        assertThat(publisher.published).isTrue();
    }

    static class FakeWeatherProvider implements WeatherProvider {
        private boolean called;
        private RegionWeatherSnapshot next;

        @Override
        public RegionWeatherSnapshot fetchCurrent(String regionCode) {
            called = true;
            return next;
        }
    }

    static class FakeWeatherCache implements WeatherCachePort {
        private final Map<String, RegionWeatherSnapshot> storage = new HashMap<>();

        void put(RegionWeatherSnapshot snapshot) {
            storage.put(snapshot.regionCode(), snapshot);
        }

        @Override
        public Optional<RegionWeatherSnapshot> get(String regionCode) {
            return Optional.ofNullable(storage.get(regionCode));
        }

        @Override
        public void save(RegionWeatherSnapshot snapshot) {
            storage.put(snapshot.regionCode(), snapshot);
        }
    }

    static class FakeWeatherAlertPublisher implements WeatherAlertPublisher {
        private boolean published;

        @Override
        public void publishTemperatureChanged(RegionWeatherSnapshot before, RegionWeatherSnapshot after) {
            published = true;
        }
    }
}
