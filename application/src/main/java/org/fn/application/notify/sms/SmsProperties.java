package org.fn.application.notify.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 *
 * @author chenshoufeng
 */
@Data
@Component
@ConfigurationProperties(prefix = "notify.sms")
public class SmsProperties {
    /**
     * 短信平台
     */
    private SmsType type;
    /**
     * 阿里云短信服务
     */
    private AliyunSms aliyun = new AliyunSms();

    @Data
    public static class AliyunSms {
        /**
         * 密钥
         */
        private String accessKeyId;
        /**
         * 密钥
         */
        private String accessKeySecret;
        /**
         * 请求时使用的协议
         */
        private String protocol = "https";
        /**
         * 服务地址
         */
        private String endpoint = "dysmsapi.aliyuncs.com";
        /**
         * 短信签名
         * <p>测试请使用 <code>阿里云短信测试</code></p>
         */
        private String signName;
        /**
         * 短信模板
         */
        private String templateCode;
        /**
         * HTTP 代理
         * <p>核心是基于 OkHttp 实现的代理，此类属于正向代理，不支持设置路由。</p>
         * <p>若需要通过 Nginx 代理，则配置此项。例如 <code>http://127.0.0.1:18020</code>。</p>
         * <pre><code>
         *  server {
         * 	  listen 18020;
         *
         * 	  location / {
         * 		proxy_pass https://dysmsapi.aliyuncs.com/;
         *
         * 		proxy_set_header X-Real-IP $remote_addr;
         * 		proxy_set_header Host $http_host;
         *      }
         *  }
         * </code></pre>
         */
        private String httpProxy;
        /**
         * HTTPS 代理
         */
        private String httpsProxy;
    }
}
