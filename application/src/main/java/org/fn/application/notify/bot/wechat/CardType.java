package org.fn.application.notify.bot.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模板卡片类型
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Getter
@AllArgsConstructor
public enum CardType {
    /**
     * 文本通知模板卡片
     */
    TEXT_NOTICE("text_notice"),
    /**
     * 图文展示模板卡片
     */
    NEWS_NOTICE("news_notice");

    private final String type;
}
