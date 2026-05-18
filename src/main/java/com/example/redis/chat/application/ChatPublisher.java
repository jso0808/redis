package com.example.redis.chat.application;

import com.example.redis.chat.domain.ChatMessage;

public interface ChatPublisher {
    void publish(ChatMessage message);
}
