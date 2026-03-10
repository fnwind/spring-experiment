package org.fn.persistence.cache;

import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisValueService extends BaseRedisService {

    public RedisValueService(RedisDbManager redisDbManager) {
        super(redisDbManager);
    }

    private ValueOperations<String, Object> ops() {
        return template().opsForValue();
    }

    public void set(String key, Object value) {
        ops().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        ops().set(key, value, timeout, unit);
    }

    /**
     * @deprecated use {@link #get(String, Class)} instead.
     * This method relies on unchecked cast.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) ops().get(key);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = ops().get(key);
        return value == null ? null : type.cast(value);
    }

    public Boolean setIfAbsent(String key, Object value) {
        return ops().setIfAbsent(key, value);
    }

    public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit) {
        return ops().setIfAbsent(key, value, timeout, timeUnit);
    }

    public Long increment(String key) {
        return ops().increment(key);
    }

    public Long increment(String key, long delta) {
        return ops().increment(key, delta);
    }

    public Long decrement(String key) {
        return ops().decrement(key);
    }

    public Long decrement(String key, long delta) {
        return ops().decrement(key, delta);
    }
}