package org.fn.persistence.cache;

import org.springframework.data.redis.core.SetOperations;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RedisSetService extends BaseRedisService {
    public RedisSetService(RedisDbManager redisDbManager) {
        super(redisDbManager);
    }

    private SetOperations<String, Object> ops() {
        return template().opsForSet();
    }

    public Long add(String key, Object... values) {
        return ops().add(key, values);
    }

    public Long remove(String key, Object... values) {
        return ops().remove(key, values);
    }

    public Boolean isMember(String key, Object value) {
        return ops().isMember(key, value);
    }

    public Set<Object> members(String key) {
        return ops().members(key);
    }

    public Long size(String key) {
        return ops().size(key);
    }

    public <T> T pop(String key, Class<T> type) {
        Object value = ops().pop(key);
        return value == null ? null : type.cast(value);
    }

    public List<Object> pop(String key, long count) {
        return ops().pop(key, count);
    }

    public Set<Object> intersect(String key, String otherKey) {
        return ops().intersect(key, otherKey);
    }

    public Set<Object> union(String key, String otherKey) {
        return ops().union(key, otherKey);
    }

    public Set<Object> difference(String key, String otherKey) {
        return ops().difference(key, otherKey);
    }

    public Set<Object> intersect(String key, Collection<String> otherKeys) {
        return ops().intersect(key, otherKeys);
    }

    public Set<Object> union(String key, Collection<String> otherKeys) {
        return ops().union(key, otherKeys);
    }

    public Set<Object> difference(String key, Collection<String> otherKeys) {
        return ops().difference(key, otherKeys);
    }
}