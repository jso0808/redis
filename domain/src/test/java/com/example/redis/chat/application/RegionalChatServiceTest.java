package com.example.redis.chat.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.redis.chat.domain.ChatMessage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegionalChatServiceTest {

    private FakeChatPublisher publisher;
    private FakeChatHistoryPort history;
    private RegionalChatService service;

    @BeforeEach
    void setUp() {
        publisher = new FakeChatPublisher();
        history = new FakeChatHistoryPort();
        service = new RegionalChatService(publisher, history);
    }

    @Test
    void sendStoresAndPublishesMessage() {
        ChatMessage message = service.send("11B00000", "u1", "hello", Instant.now());

        assertThat(history.messages).hasSize(1);
        assertThat(publisher.published).hasSize(1);
        assertThat(message.regionCode()).isEqualTo("11B00000");
    }

    @Test
    void recentReturnsOnlyRequestedRegionMessages() {
        service.send("11B00000", "u1", "a", Instant.now());
        service.send("26B00000", "u2", "b", Instant.now());

        List<ChatMessage> result = service.recent("11B00000", 50);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).content()).isEqualTo("a");
    }

    static class FakeChatPublisher implements ChatPublisher {
        private final List<ChatMessage> published = new ArrayList<>();

        @Override
        public void publish(ChatMessage message) {
            published.add(message);
        }
    }

    static class FakeChatHistoryPort implements ChatHistoryPort {
        private final List<ChatMessage> messages = new ArrayList<>();

        @Override
        public void append(ChatMessage message) {
            messages.add(message);
        }

        @Override
        public List<ChatMessage> recent(String regionCode, int limit) {
            return messages.stream().filter(m -> m.regionCode().equals(regionCode)).toList();
        }
    }
}
