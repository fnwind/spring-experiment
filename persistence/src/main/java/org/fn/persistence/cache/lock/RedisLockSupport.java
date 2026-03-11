package org.fn.persistence.cache.lock;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class RedisLockSupport {

    private static final String PREFIX = "lock";

    public static String generateLockValue() {
        return UUID.randomUUID()
                + ":" + Thread.currentThread().threadId()
                + ":" + System.currentTimeMillis();
    }
}