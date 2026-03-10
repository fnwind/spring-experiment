package org.fn.persistence.cache;

import org.springframework.data.redis.core.HashOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenshoufeng
 * @since 2026/3/10 下午4:28
 **/
public class RedisHashService extends BaseRedisService {

    public RedisHashService(RedisDbManager redisDbManager) {
        super(redisDbManager);
    }

    public void put(String key, String hashKey, Object value) {
        ops().put(key, hashKey, value);
    }

    public void putAll(String key, Map<String, ?> map) {
        ops().putAll(key, map);
    }

    public <T> T get(String key, String hashKey, Class<T> type) {
        Object value = ops().get(key, hashKey);
        return value == null ? null : type.cast(value);
    }

    public Boolean hasKey(String key, String hashKey) {
        return ops().hasKey(key, hashKey);
    }

    public Long delete(String key, String... hashKeys) {
        return ops().delete(key, (Object[]) hashKeys);
    }

    public Long size(String key) {
        return ops().size(key);
    }

    public Set<String> keys(String key) {
        return ops().keys(key);
    }

    public List<Object> values(String key) {
        return ops().values(key);
    }

    public Map<String, Object> entries(String key) {
        return ops().entries(key);
    }

    public Long increment(String key, String hashKey, long delta) {
        return ops().increment(key, hashKey, delta);
    }

    public Double increment(String key, String hashKey, double delta) {
        return ops().increment(key, hashKey, delta);
    }

    public <T> Map<String, T> entries(String key, Class<T> type) {
        Map<String, Object> map = ops().entries(key);

        Map<String, T> result = new HashMap<>(map.size());
        map.forEach((k, v) -> result.put(k, type.cast(v)));

        return result;
    }

    private HashOperations<String, String, Object> ops() {
        return template().opsForHash();
    }
}