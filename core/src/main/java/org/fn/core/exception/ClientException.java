package org.fn.core.exception;

import org.fn.core.exception.errorcode.ClientErrorCode;

/**
 * @author chenshoufeng
 * @since 2026/3/17 上午10:49
 **/
public class ClientException extends BaseException {

    public ClientException(ClientErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public ClientException(ClientErrorCode errorCode, String messageKey, Object... args) {
        super(errorCode, messageKey, args);
    }

    public ClientException(Throwable cause, ClientErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ClientException(Throwable cause, ClientErrorCode errorCode, String messageKey, Object... args) {
        super(cause, errorCode, messageKey, args);
    }
}
