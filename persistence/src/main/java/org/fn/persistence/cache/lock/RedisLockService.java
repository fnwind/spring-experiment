package org.fn.persistence.cache.lock;

import org.fn.persistence.cache.BaseRedisService;
import org.fn.persistence.cache.RedisDbManager;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * @author chenshoufeng
 * @since 2026/3/10 下午7:12
 **/
@Service
public class RedisLockService extends BaseRedisService {
    public RedisLockService(RedisDbManager redisDbManager) {
        super(redisDbManager);

        unlockScript = new DefaultRedisScript<>();
        unlockScript.setScriptText(UNLOCK_SCRIPT);
        unlockScript.setResultType(Long.class);
    }

    private final DefaultRedisScript<Long> unlockScript;
    private static final String UNLOCK_SCRIPT = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
               return redis.call('del', KEYS[1])
            else
               return 0
            end
            """;

    public boolean tryLock(String key, Duration expire) {
        String value = RedisLockSupport.generateLockValue();
        return tryLock(key, value, expire);
    }

    public boolean tryLock(String key, String value, Duration expire) {
        Boolean success = template().opsForValue().setIfAbsent(
                key,
                value,
                expire
        );

        return Boolean.TRUE.equals(success);
    }

    public void unlock(String key, String value) {
        template().execute(
                unlockScript,
                Collections.singletonList(key),
                value
        );
    }

    public <T> T executeWithLock(String key, Duration expire, Supplier<T> supplier) {
        String value = RedisLockSupport.generateLockValue();
        boolean locked = tryLock(key, value, expire);
        if (!locked) {
            throw new IllegalStateException("Failed to acquire redis lock: " + key);
        }

        try {
            return supplier.get();
        } finally {
            unlock(key, value);
        }
    }
}
