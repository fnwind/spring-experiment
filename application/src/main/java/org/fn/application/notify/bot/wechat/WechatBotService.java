package org.fn.application.notify.bot.wechat;

import org.fn.application.notify.bot.BotService;

/**
 * @author chenshoufeng
 * @since 2026/3/11 上午9:18
 **/
public interface WechatBotService extends BotService {
     void sendMarkdownV2(String bot, String markdown);
}
