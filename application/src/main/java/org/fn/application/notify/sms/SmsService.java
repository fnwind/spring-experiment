package org.fn.application.notify.sms;

/**
 * 发送消息到短信
 *
 * @author chenshoufeng
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param mobile  手机号
     * @param content 短信内容
     */
    void send(String mobile, String content);
}
