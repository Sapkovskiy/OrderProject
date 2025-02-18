package org.numbergenerate.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class NumberService {
    private final StringRedisTemplate redisTemplate;
    private static final int ORDER_NUMBER_LENGTH = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Random RANDOM = new Random();

    public NumberService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateUniqueOrderNumber() {
        String uniqueNumber;
        do {
            uniqueNumber = generateRandomNumber();
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(uniqueNumber)));

        String orderId = uniqueNumber + LocalDateTime.now().format(FORMATTER);
        redisTemplate.opsForValue().set(orderId, "1");
        return orderId;
    }

    private String generateRandomNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NumberService.ORDER_NUMBER_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
