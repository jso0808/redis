package com.example.redis.chat.application;

import com.example.redis.chat.domain.ChatMessage;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegionalChatService {

    private final ChatPublisher chatPublisher;
    private final ChatHistoryPort chatHistoryPort;

    public RegionalChatService(ChatPublisher chatPublisher, ChatHistoryPort chatHistoryPort) {
        this.chatPublisher = chatPublisher;
        this.chatHistoryPort = chatHistoryPort;
    }

    public ChatMessage send(String regionCode, String senderId, String content, Instant sentAt) {
        ChatMessage message = new ChatMessage(regionCode, senderId, content, sentAt);
        chatHistoryPort.append(message);
        chatPublisher.publish(message);
        return message;
    }

    public List<ChatMessage> recent(String regionCode, int limit) {
        return chatHistoryPort.recent(regionCode, limit);
    }
}
