package org.fn.core.exception.errorcode;

import lombok.Getter;

/**
 * @author chenshoufeng
 * @since 2026/3/17 上午11:12
 **/
@Getter
public enum ServerErrorCode implements ErrorCode {
    B0001(500, "系统执行出错"),

    B0100(504, "系统执行超时"),
    B0101(504, "系统订单处理超时"),

    B0200(503, "系统容灾功能被触发"),
    B0210(429, "系统限流"),
    B0220(503, "系统功能降级"),

    B0300(500, "系统资源异常"),
    B0310(503, "系统资源耗尽"),
    B0311(507, "系统磁盘空间耗尽"),
    B0312(507, "系统内存耗尽"),
    B0313(500, "文件句柄耗尽"),
    B0314(503, "系统连接池耗尽"),
    B0315(503, "系统线程池耗尽"),

    B0320(500, "系统资源访问异常"),
    B0321(500, "系统读取磁盘文件失败"),
    ;

    private final String desc;
    private final int httpStatusCode;

    ServerErrorCode(int httpStatusCode, String desc) {
        this.desc = desc;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getI18n() {
        return "ERROR_CODE.SERVER." + name();
    }
}
