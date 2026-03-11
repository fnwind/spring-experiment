package org.fn.application.notify.bot;

public interface BotService {
    void sendText(String bot, String content);

    void sendMarkdown(String bot, String markdown);
}