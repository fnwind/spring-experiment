package org.fn.core.exception.errorcode;

/**
 * @author chenshoufeng
 * @since 2026/3/17 上午11:08
 **/
public interface ErrorCode {
    String getI18n();
    String getCode();
    int getHttpStatusCode();
}
