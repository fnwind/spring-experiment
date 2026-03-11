package org.fn.application.notify.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 机器人配置
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "notify.bot")
public class BotProperties {
    /**
     * 机器人平台
     */
    private BotType type;
    /**
     * 企业微信机器人
     */
    private WechatBot wechat = new WechatBot();

    @Data
    public static class WechatBot {
        private Map<String, String> bots = new HashMap<>();
    }
}
