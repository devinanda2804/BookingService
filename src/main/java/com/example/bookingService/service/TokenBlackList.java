package com.example.bookingService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackList {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist_token:";  // Key prefix for blacklisted tokens

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted");  // Store token in Redis
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);  // Check if token is in Redis
    }

    public void removeTokenFromBlacklist(String token) {
        redisTemplate.delete(BLACKLIST_PREFIX + token);  // Remove token from Redis
    }
}
