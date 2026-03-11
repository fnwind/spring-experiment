package org.fn.application.notify.bot.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    /**
     * 文本类型
     */
    Text("text"),
    /**
     * Markdown
     */
    Markdown("markdown"),
    /**
     * Markdown V2
     * <ul>
     *     <li>不支持字体颜色</li>
     *     <li>不支持 @群成员</li>
     *     <li>支持表格</li>
     * </ul>
     */
    MarkdownV2("markdown_v2"),
    /**
     * 图片类型
     */
    Image("image"),
    /**
     * 图文类型
     */
    News("news"),
    /**
     * 文件类型
     */
    File("file"),
    /**
     * 语音类型
     */
    Voice("voice"),
    /**
     * 模板卡片类型
     */
    TemplateCard("template_card");

    private final String type;
}