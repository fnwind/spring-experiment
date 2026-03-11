package org.fn.application.notify.bot.wechat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Markdown 消息
 * <p>可用于 <code>markdown</code> 和 <code>markdown_v2</code> 类型</p>
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Data
@Accessors(chain = true)
public class MarkdownMessage {
    private String content;
}
