package com.example.redis.user.domain;

public record User(
        String userId,
        String nickname,
        String regionCode
) {
}
