package com.example.redis.weather.infra;

import com.example.redis.weather.application.WeatherProvider;
import com.example.redis.weather.domain.RegionWeatherSnapshot;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class StubKmaWeatherProvider implements WeatherProvider {

    @Override
    public RegionWeatherSnapshot fetchCurrent(String regionCode) {
        double base = ThreadLocalRandom.current().nextDouble(15.0, 31.0);
        int humidity = ThreadLocalRandom.current().nextInt(30, 91);
        boolean raining = ThreadLocalRandom.current().nextBoolean();
        return new RegionWeatherSnapshot(regionCode, base, humidity, raining, Instant.now());
    }
}
