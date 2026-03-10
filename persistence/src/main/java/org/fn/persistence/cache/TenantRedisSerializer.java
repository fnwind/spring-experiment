package org.fn.persistence.cache;

import org.fn.core.common.Global;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;

public class TenantRedisSerializer implements RedisSerializer<String> {
    @NotNull
    @Override
    public byte[] serialize(String key) {
        String tenantKey = Global.Tenant.get() + ":" + key;
        return tenantKey.getBytes(StandardCharsets.UTF_8);
    }

    // 通过 scan 等接口返回 keys 时，排除租户前缀
    @Override
    public String deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        String key = new String(bytes, StandardCharsets.UTF_8);
        int index = key.indexOf(':');
        return index > 0 ? key.substring(index + 1) : key;
    }
}