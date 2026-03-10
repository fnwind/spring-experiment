package org.fn.persistence.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.fn.core.common.RdbIndex;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RedisDbManager {
    private final RedisTemplate<String, Object> baseTemplate;
    private final LettuceConnectionFactory baseFactory;
    private RdbIndex defaultRdbIndex; //配置文件为默认库

    // 缓存已创建的 RedisTemplate
    private final Map<RdbIndex, RedisTemplate<String, Object>> templateCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        RedisStandaloneConfiguration config = baseFactory.getStandaloneConfiguration();
        defaultRdbIndex = RdbIndex.of(config.getDatabase());
        templateCache.put(defaultRdbIndex, baseTemplate);
    }

    public RedisTemplate<String, Object> getTemplate() {
        return templateCache.computeIfAbsent(defaultRdbIndex, this::createTemplate);
    }

    public RedisTemplate<String, Object> getTemplate(RdbIndex dbIndex) {
        return templateCache.computeIfAbsent(dbIndex, this::createTemplate);
    }

    private RedisTemplate<String, Object> createTemplate(RdbIndex dbIndex) {
        // 1. 克隆 Redis 配置
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        RedisStandaloneConfiguration baseConfig = baseFactory.getStandaloneConfiguration();
        config.setHostName(baseConfig.getHostName());
        config.setPort(baseConfig.getPort());
        config.setPassword(baseConfig.getPassword());
        config.setDatabase(dbIndex.getIndex());

        // 2. 新建 LettuceFactory，复用客户端配置，避免重复 Netty 线程池
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, baseFactory.getClientConfiguration());
        factory.afterPropertiesSet();

        // 3. 新建 RedisTemplate 并复用序列化器
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(baseTemplate.getKeySerializer());
        template.setValueSerializer(baseTemplate.getValueSerializer());
        template.setHashKeySerializer(baseTemplate.getHashKeySerializer());
        template.setHashValueSerializer(baseTemplate.getHashValueSerializer());
        template.afterPropertiesSet();

        return template;
    }
}