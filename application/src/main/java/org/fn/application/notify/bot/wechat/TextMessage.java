package org.fn.application.notify.bot.wechat;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本消息
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Data
@Accessors(chain = true)
public class TextMessage {
    /**
     * 纯文本内容
     */
    private String content;
    /**
     * 微信账号的 userId 列表，会@列表中的所有成员
     */
    @Alias("mentioned_ist")
    private List<String> mentionedList = new ArrayList<>();
    /**
     * 手机号列表，会@列表中的所有成员
     */
    @Alias("mentioned_mobile_list")
    private List<String> mentionedMobileList = new ArrayList<>();
}
