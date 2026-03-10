package org.fn.persistence.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // TODO: 可配置的多租户模式 + 开关
        template.setKeySerializer(new TenantRedisSerializer());
        template.setHashKeySerializer(new TenantRedisSerializer());

        template.setValueSerializer(new GenericJacksonJsonRedisSerializer(objectMapper));
        template.setHashValueSerializer(new GenericJacksonJsonRedisSerializer(objectMapper));

        return template;
    }
}