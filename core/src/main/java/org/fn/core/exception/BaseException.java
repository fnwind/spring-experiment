package org.fn.core.exception;

import lombok.Getter;
import org.fn.core.exception.errorcode.ErrorCode;

import java.util.Arrays;

/**
 * @author chenshoufeng
 * @since 2026/3/17 上午10:49
 **/
@Getter
public abstract class BaseException extends RuntimeException {
    /**
     * 覆盖默认 errorCode 的国际化文案，允许在抛出异常时动态传入参数
     */
    private final String i18n;
    private final Object[] args;
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode, Object... args) {
        this(null, errorCode, args);
    }

    public BaseException(ErrorCode errorCode, String i18n, Object... args) {
        this(null, errorCode, i18n, args);
    }

    public BaseException(Throwable cause, ErrorCode errorCode, Object... args) {
        this(cause, errorCode, errorCode.getI18n(), args);
    }

    public BaseException(Throwable cause, ErrorCode errorCode, String i18n, Object... args) {
        super(buildMessage(errorCode, i18n, args), cause);
        this.errorCode = errorCode;
        this.i18n = i18n;
        this.args = args;
    }

    private static String buildMessage(ErrorCode ec, String i18n, Object[] args) {
        return ec.getCode() + " | " + i18n + " | " + Arrays.toString(args);
    }
}
