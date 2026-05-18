package com.example.redis.alert.api;

import com.example.redis.alert.application.AlertService;
import com.example.redis.alert.domain.WeatherAlert;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions/{regionCode}/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/recent")
    public List<WeatherAlert> recent(
            @PathVariable String regionCode,
            @RequestParam(defaultValue = "50") @Min(1) @Max(200) int limit
    ) {
        return alertService.recent(regionCode, limit);
    }
}
