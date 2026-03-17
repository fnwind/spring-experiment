package org.fn.core.exception.errorcode;

import lombok.Getter;

/**
 * @author chenshoufeng
 * @since 2026/3/17 上午11:12
 **/
@Getter
public enum ThirdErrorCode implements ErrorCode {
    C0001(502, "调用第三方服务出错"),

    C0100(502, "中间件服务出错"),
    C0110(502, "RPC服务出错"),
    C0111(404, "RPC服务未找到"),
    C0112(503, "RPC服务未注册"),
    C0113(404, "接口不存在"),

    C0120(502, "消息服务出错"),
    C0121(502, "消息投递出错"),
    C0122(502, "消息消费出错"),
    C0123(502, "消息订阅出错"),
    C0124(404, "消息分组未查到"),

    C0130(502, "缓存服务出错"),
    C0131(400, "key长度超过限制"),
    C0132(400, "value长度超过限制"),
    C0133(507, "存储容量已满"),
    C0134(400, "不支持的数据格式"),

    C0140(502, "配置服务出错"),

    C0150(502, "网络资源服务出错"),
    C0151(502, "VPN服务出错"),
    C0152(502, "CDN服务出错"),
    C0153(502, "域名解析服务出错"),
    C0154(502, "网关服务出错"),

    C0200(504, "第三方系统执行超时"),
    C0210(504, "RPC执行超时"),
    C0220(504, "消息投递超时"),
    C0230(504, "缓存服务超时"),
    C0240(504, "配置服务超时"),
    C0250(504, "数据库服务超时"),

    C0300(502, "数据库服务出错"),
    C0311(500, "表不存在"),
    C0312(500, "列不存在"),
    C0321(500, "多表关联中存在多个相同名称的列"),
    C0331(500, "数据库死锁"),
    C0341(409, "主键冲突"),

    C0400(503, "第三方容灾系统被触发"),
    C0401(429, "第三方系统限流"),
    C0402(503, "第三方功能降级"),

    C0500(502, "通知服务出错"),
    C0501(502, "短信提醒服务失败"),
    C0502(502, "语音提醒服务失败"),
    C0503(502, "邮件提醒服务失败"),
    ;

    private final String desc;
    private final int httpStatusCode;

    ThirdErrorCode(int httpStatusCode, String desc) {
        this.desc = desc;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getI18n() {
        return "ERROR_CODE.THIRD." + name();
    }
}
