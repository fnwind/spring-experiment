package org.fn.persistence.cache;

import org.fn.core.common.Global;
import org.fn.core.common.RdbIndex;
import org.fn.persistence.cache.lock.RedisLockService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisDbManager redisDbManager;

    public final RedisValueService value;
    public final RedisListService list;
    public final RedisHashService hash;
    public final RedisSetService set;
    public final RedisZSetService zset;
    public final RedisLockService lock;

    public RedisService(RedisDbManager redisDbManager) {
        this.redisDbManager = redisDbManager;

        this.value = new RedisValueService(redisDbManager);
        this.list = new RedisListService(redisDbManager);
        this.hash = new RedisHashService(redisDbManager);
        this.set = new RedisSetService(redisDbManager);
        this.zset = new RedisZSetService(redisDbManager);
        this.lock = new RedisLockService(redisDbManager);
    }

    public RedisTemplate<String, Object> template() {
        RdbIndex db = Global.Redis.get();
        if (db == null) {
            return redisDbManager.getTemplate();
        }
        return redisDbManager.getTemplate(db);
    }

    public void delete(String key) {
        template().delete(key);
    }

    public Long delete(Collection<String> keys) {
        return template().delete(keys);
    }

    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return template().expire(key, timeout, unit);
    }

    public Set<String> keys(String pattern) {
        return template().keys(pattern);
    }

    public Set<String> scan(String pattern, int count) {
        RedisSerializer<String> serializer = keySerializer();
        byte[] rawPattern = serializer.serialize(pattern);
        ScanOptions options = ScanOptions.scanOptions()
                .match(rawPattern)
                .count(count)
                .build();

        Set<String> keys = new HashSet<>();
        RedisConnectionFactory factory = template().getConnectionFactory();
        if (factory == null) {
            return keys;
        }

        try (RedisConnection connection = factory.getConnection();
             Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {

            while (cursor.hasNext()) {
                keys.add(serializer.deserialize(cursor.next()));
            }
        }

        return keys;
    }

    @SuppressWarnings("unchecked")
    private RedisSerializer<String> keySerializer() {
        return (RedisSerializer<String>) template().getKeySerializer();
    }
}