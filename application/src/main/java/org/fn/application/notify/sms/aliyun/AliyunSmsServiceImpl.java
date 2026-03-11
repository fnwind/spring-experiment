package org.fn.application.notify.sms.aliyun;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import org.fn.application.notify.sms.SmsProperties;
import org.fn.application.notify.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


/**
 * 阿里云短信服务
 *
 * @author chenshoufeng
 * @version 1.5.0
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notify.sms", name = "type", havingValue = "aliyun")
public class AliyunSmsServiceImpl implements SmsService {
    private static final String SMS_RESPONSE_OK_CODE = "OK";
    private static final int SMS_RESPONSE_OK_STATUS_CODE = 200;

    private Client client;
    private final SmsProperties smsProperties;

    @SneakyThrows
    @PostConstruct
    void init() {
        SmsProperties.AliyunSms aliyun = smsProperties.getAliyun();
        Config config = new Config()
                .setAccessKeyId(aliyun.getAccessKeyId())
                .setAccessKeySecret(aliyun.getAccessKeySecret())
                .setProtocol(aliyun.getProtocol())
                .setHttpProxy(aliyun.getHttpProxy())
                .setHttpsProxy(aliyun.getHttpsProxy())
                .setEndpoint(aliyun.getEndpoint());
        client = new Client(config);
        log.info("[sms][aliyun]已初始化阿里云短信服务");
    }

    @Override
    @SneakyThrows
    public void send(String mobile, String content) {
        SmsProperties.AliyunSms aliyun = smsProperties.getAliyun();
        SendSmsRequest request = new SendSmsRequest()
                .setSignName(aliyun.getSignName())
                .setTemplateCode(aliyun.getTemplateCode())
                .setPhoneNumbers(mobile)
                .setTemplateParam(content);
        log.debug("[sms][aliyun]请求参数：signName={}, templateCode={}, phoneNumber={}, templateParam={}",
                aliyun.getSignName(), aliyun.getTemplateCode(), mobile, content);

        SendSmsResponse smsResponse = client.sendSms(request);
        check(request, smsResponse);
    }

    private void check(SendSmsRequest request, SendSmsResponse smsResponse) {
        SendSmsResponseBody responseBody = smsResponse.getBody();
        Integer statusCode = smsResponse.getStatusCode();
        String bodyMessage = responseBody.getMessage();
        String bodyCode = responseBody.getCode();

        log.debug("[sms][aliyun]响应结果：状态码：{}，响应：[{}]-{}", statusCode, bodyCode, bodyMessage);

        if (ObjectUtil.notEqual(statusCode, SMS_RESPONSE_OK_STATUS_CODE)) {
            log.error("[sms][aliyun]发送短信到手机号 [{}] 出现异常，状态码：{}，响应：[{}]-{}", request.getPhoneNumbers(), statusCode, bodyCode, bodyMessage);
            throw new RuntimeException(StrUtil.format("发送短信验证码到手机号 [{}] 时出现异常，详情请检查日志", request.getPhoneNumbers()));
        }

        if (!SMS_RESPONSE_OK_CODE.equalsIgnoreCase(bodyCode)) {
            log.error("[sms][aliyun]发送短信到手机号 [{}] 时出现异常，响应：[{}]-{}", request.getPhoneNumbers(), bodyCode, bodyMessage);
            throw new RuntimeException(StrUtil.format("发送短信验证码到手机号 [{}] 时出现异常，详情请检查日志", request.getPhoneNumbers()));
        }
    }
}
