package org.numbergenerate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.numbergenerate.service.NumberService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NumberServiceTest {
    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private NumberService numberService;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        numberService = new NumberService(redisTemplate);
    }

    @Test
    void testGenerateUniqueOrderNumber() {
        when(redisTemplate.hasKey(anyString())).thenReturn(false);

        String orderNumber = numberService.generateUniqueOrderNumber();

        assertNotNull(orderNumber);
        assertEquals(13, orderNumber.length());
        verify(valueOperations, times(1)).set(anyString(), eq("1"));
    }
}
