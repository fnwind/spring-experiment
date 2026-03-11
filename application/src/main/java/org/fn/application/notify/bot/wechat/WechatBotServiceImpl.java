package org.fn.application.notify.bot.wechat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fn.application.notify.bot.BotProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业微信群机器人
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notify.bot", name = "type", havingValue = "wechat")
public class WechatBotServiceImpl implements WechatBotService {
    private final BotProperties botProperties;

    @Override
    public void sendText(String bot, String content) {
        if (StrUtil.hasBlank(bot, content)) {
            return;
        }

        TextMessage textMessage = new TextMessage()
                .setContent(content);

        sendMessage(getBotUrl(bot), MessageType.Text, textMessage);
    }

    @Override
    public void sendMarkdown(String bot, String markdown) {
        if (StrUtil.hasBlank(bot, markdown)) {
            return;
        }

        MarkdownMessage markdownMessage = new MarkdownMessage()
                .setContent(markdown);

        sendMessage(getBotUrl(bot), MessageType.Markdown, markdownMessage);
    }

    @Override
    public void sendMarkdownV2(String bot, String markdown) {
        if (StrUtil.hasBlank(bot, markdown)) {
            return;
        }

        MarkdownMessage markdownMessage = new MarkdownMessage()
                .setContent(markdown);

        sendMessage(getBotUrl(bot), MessageType.MarkdownV2, markdownMessage);
    }

    private void sendMessage(String botUrl, MessageType messageType, Object message) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("msgtype", messageType.getType());
        requestBody.put(messageType.getType(), message);

        @Cleanup
        HttpResponse httpResponse = HttpUtil.createPost(botUrl)
                .header("Content-Type", "application/json")
                .body(JSONUtil.parse(requestBody).toStringPretty())
                .execute();

        if (!httpResponse.isOk()) {
            log.error("企业微信消息推送失败: {}", requestBody);
            throw new RuntimeException("企业微信消息推送失败");
        }
    }

    private String getBotUrl(String bot) {
        BotProperties.WechatBot wechatBot = botProperties.getWechat();

        String botUrl = wechatBot.getBots().get(bot);
        Assert.notNull(botUrl, "未配置 [" + bot + "] 机器人");

        return botUrl;
    }
}
