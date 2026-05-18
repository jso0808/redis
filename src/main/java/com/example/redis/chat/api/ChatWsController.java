package com.example.redis.chat.api;

import com.example.redis.chat.application.RegionalChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWsController {

    private final RegionalChatService regionalChatService;

    public ChatWsController(RegionalChatService regionalChatService) {
        this.regionalChatService = regionalChatService;
    }

    @MessageMapping("/chat/{regionCode}")
    public void send(@DestinationVariable String regionCode, @Valid @Payload ChatSendRequest request) {
        regionalChatService.send(regionCode, request.senderId(), request.content(), Instant.now());
    }

    public record ChatSendRequest(
            @NotBlank String senderId,
            @NotBlank String content
    ) {
    }
}
