package org.fn.persistence;

import lombok.RequiredArgsConstructor;
import org.fn.core.common.Feature;
import org.fn.core.common.Global;
import org.fn.core.common.RdbIndex;
import org.fn.core.feature.DisableFeature;
import org.fn.persistence.cache.RedisDb;
import org.fn.persistence.cache.RedisDbManager;
import org.fn.persistence.cache.RedisService;
import org.fn.persistence.database.mapper.AccountMapper;
import org.fn.persistence.entity.Account;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author chenshoufeng
 * @since 2026/3/5 上午10:55
 **/
@Component
@RequiredArgsConstructor
public class Test {
    private final AccountMapper accountMapper;
    private final RedisDbManager rdm;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final JsonMapper jsonMapper;
    // private final RedisService redisService;
    private final RedisService redisService;

    @RedisDb(RdbIndex.DB2)
    @DisableFeature({Feature.DATA_PROTECT})
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        try {
            Global.Tenant.set("0");
            // ValueOperations<String, Object> stringObjectValueOperations = rdm.selectDb(2).opsForValue();

            Account account = new Account();
            account.setUsername("acc");
            account.setCreateTime(LocalDateTime.now());

            // redisTemplate.opsForValue().set("db-default", "hello");
            // // redisService.select(1);
            // rdm.getTemplate(RdbIndex.DB1).opsForValue().set("db1", LocalDateTime.now());
            // // redisService.select(2);
            // rdm.getTemplate(RdbIndex.DB2).opsForValue().set("db2", LocalDateTime.now());
            // System.out.println("objectMapper: " + objectMapper.writeValueAsString(LocalDateTime.now()));
            // System.out.println("jsonMapper: " + jsonMapper.writeValueAsString(LocalDateTime.now()));

            // Set<String> keys = rdm.getTemplate(RdbIndex.DB2).keys("*");
            // keys.forEach(System.out::println);

            redisService.value.set("abc",123);

            System.out.println("keys >>>");
            redisService.keys("*").forEach(System.out::println);

            System.out.println("scan >>>");
            redisService.scan("*", 1024).forEach(System.out::println);

            // redisTemplate.opsForValue().set("object", account);

        } finally {
            Global.Tenant.clear();
        }
    }
}

