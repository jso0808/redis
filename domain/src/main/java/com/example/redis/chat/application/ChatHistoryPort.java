package com.example.redis.chat.application;

import com.example.redis.chat.domain.ChatMessage;
import java.util.List;

public interface ChatHistoryPort {
    void append(ChatMessage message);

    List<ChatMessage> recent(String regionCode, int limit);
}
