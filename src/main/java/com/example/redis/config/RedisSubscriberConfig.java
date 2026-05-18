package com.example.redis.config;

import com.example.redis.chat.domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class RedisSubscriberConfig {

    @Bean
    RedisMessageListenerContainer redisContainer(
            org.springframework.data.redis.connection.RedisConnectionFactory connectionFactory,
            MessageListener chatRelayListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatRelayListener, new PatternTopic("chat.region.*"));
        return container;
    }

    @Bean
    MessageListener chatRelayListener(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        return new ChatRelayListener(messagingTemplate, objectMapper);
    }

    static class ChatRelayListener implements MessageListener {
        private static final String PREFIX = "chat.region.";
        private final SimpMessagingTemplate messagingTemplate;
        private final ObjectMapper objectMapper;

        ChatRelayListener(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
            this.messagingTemplate = messagingTemplate;
            this.objectMapper = objectMapper;
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {
            String channel = new String(message.getChannel());
            String regionCode = channel.startsWith(PREFIX) ? channel.substring(PREFIX.length()) : "unknown";
            String payload = new String(message.getBody());
            messagingTemplate.convertAndSend("/topic/chat/" + regionCode, fromJson(payload));
        }

        private ChatMessage fromJson(String payload) {
            try {
                return objectMapper.readValue(payload, ChatMessage.class);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to deserialize chat payload", e);
            }
        }
    }
}
