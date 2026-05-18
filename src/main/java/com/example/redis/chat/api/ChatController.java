package com.example.redis.chat.api;

import com.example.redis.chat.application.RegionalChatService;
import com.example.redis.chat.domain.ChatMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions/{regionCode}/chat")
public class ChatController {

    private final RegionalChatService regionalChatService;

    public ChatController(RegionalChatService regionalChatService) {
        this.regionalChatService = regionalChatService;
    }

    @GetMapping("/recent")
    public List<ChatMessage> recent(
            @PathVariable String regionCode,
            @RequestParam(defaultValue = "50") @Min(1) @Max(200) int limit
    ) {
        return regionalChatService.recent(regionCode, limit);
    }
}
