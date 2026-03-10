package org.fn.persistence.cache;

import org.fn.core.common.Global;
import org.fn.core.common.RdbIndex;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class BaseRedisService {
    protected final RedisDbManager redisDbManager;

    protected BaseRedisService(RedisDbManager redisDbManager) {
        this.redisDbManager = redisDbManager;
    }

    protected RedisTemplate<String, Object> template() {
        RdbIndex db = Global.Redis.get();
        
        return db == null
                ? redisDbManager.getTemplate()
                : redisDbManager.getTemplate(db);
    }
}