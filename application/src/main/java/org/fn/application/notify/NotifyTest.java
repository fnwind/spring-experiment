package org.fn.application.notify;

import lombok.RequiredArgsConstructor;
import org.fn.application.notify.bot.BotService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/11 上午9:33
 **/
@Component
@RequiredArgsConstructor
public class NotifyTest {
    private final BotService botService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        // System.out.println("NotifyTest onReady");
        // botService.sendText("aaa", "bbb");
    }
}
