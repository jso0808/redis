package com.example.redis.alert.infra;

import com.example.redis.alert.application.AlertNotifier;
import com.example.redis.alert.domain.WeatherAlert;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAlertNotifier implements AlertNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketAlertNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notify(WeatherAlert alert) {
        messagingTemplate.convertAndSend("/topic/alerts/" + alert.regionCode(), alert);
    }
}
