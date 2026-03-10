package org.fn.persistence.cache;

import org.springframework.data.redis.core.ListOperations;

import java.util.Collection;

public class RedisListService extends BaseRedisService {

    public RedisListService(RedisDbManager redisDbManager) {
        super(redisDbManager);
    }

    private ListOperations<String, Object> ops() {
        return template().opsForList();
    }

    public Long rightPush(String key, Object value) {
        return ops().rightPush(key, value);
    }

    public Long rightPushAll(String key, Collection<?> values) {
        return ops().rightPushAll(key, values);
    }

    public Long leftPush(String key, Object value) {
        return ops().leftPush(key, value);
    }

    public <T> T leftPop(String key, Class<T> type) {
        Object value = ops().leftPop(key);
        return value == null ? null : type.cast(value);
    }

    public <T> T rightPop(String key, Class<T> type) {
        Object value = ops().rightPop(key);
        return value == null ? null : type.cast(value);
    }

    public Long listSize(String key) {
        return ops().size(key);
    }
}