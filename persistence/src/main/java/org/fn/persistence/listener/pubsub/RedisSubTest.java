package org.fn.persistence.listener.pubsub;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/25 下午1:36
 **/
@Component
public class RedisSubTest {

    @RedisSubscribeListener(channels = "test-channel")
    public void onSubMessage(String channel, String message) {
        System.out.println("Received [" + channel + "] message: " + message);

    }

    @Data
    public static class TestMessage {
        private String name;
        private Integer age;
    }
}
