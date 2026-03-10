package org.fn.core.common;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@UtilityClass
public class TraceIdGenerator {

    private static final Random RANDOM = new Random();

    /**
     * 生成 traceId
     * 格式：yyyyMMddHHmmssSSS + 4 位随机数
     * 例子：20260304101530123-8371
     */
    public static String generateTraceId() {
        // 当前时间，精确到毫秒
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                .withZone(Instant.now().atZone(java.time.ZoneId.systemDefault()).getZone())
                .format(Instant.now());

        // 4 位随机数
        int random = RANDOM.nextInt(9000) + 1000; // 1000 ~ 9999

        return timestamp + "-" + random;
    }

    /**
     * 从请求 header 获取 traceId，如果没有则生成新的
     */
    public static String getOrCreateTraceId(String headerTraceId) {
        if (StringUtils.hasText(headerTraceId)) {
            return headerTraceId;
        }
        return generateTraceId();
    }
}