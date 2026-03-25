package org.fn.persistence.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/17 下午8:01
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheTest {
    private final RedisService redisService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        redisService.value.set("testKey", "Hello, Redis!");
        redisService.keys("*").forEach(System.out::println);
    }
}
