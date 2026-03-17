package org.fn.web.config.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fn.core.exception.BaseException;
import org.fn.core.exception.ClientException;
import org.fn.core.exception.errorcode.ClientErrorCode;
import org.fn.core.exception.errorcode.ErrorCode;
import org.fn.core.exception.errorcode.ServerErrorCode;
import org.fn.core.model.FieldErrorResp;
import org.fn.persistence.result.R;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * @author chenshoufeng
 * @since 2026/3/17 下午2:53
 **/
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public R<Void> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        if (e instanceof ClientException) {
            log.warn("[ClientException] code={}, msg={}", errorCode.getCode(), e.getMessage(), e);
        } else {
            log.error("[{}] code={}, msg={}", e.getClass().getName(), errorCode.getCode(), e.getMessage(), e);
        }

        return R.<Void>builder()
                .code(errorCode.getCode())
                .i18n(e.getI18n())
                .args(e.getArgs())
                .success(false)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<List<FieldErrorResp>> handleBindErrors(MethodArgumentNotValidException e) {
        log.warn("[MethodArgumentNotValidException] code={} msg={}", ClientErrorCode.A0400.getCode(), e.getMessage(), e);
        return buildErrorResponse(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public R<List<FieldErrorResp>> handleBindErrors(BindException e) {
        log.warn("[BindException] code={} msg={}", ClientErrorCode.A0400.getCode(), e.getMessage(), e);
        return buildErrorResponse(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("[MethodArgumentTypeMismatchException] code={} msg={}", ClientErrorCode.A0400.getCode(), e.getMessage(), e);

        String field = e.getName();
        Object value = e.getValue();
        String targetType = e.getRequiredType() != null
                ? e.getRequiredType().getSimpleName()
                : "unknown";

        return R.<Void>builder()
                .code(ClientErrorCode.A0400.getCode())
                .i18n("common.exception.method-argument-type-mismatch")
                .args(new Object[]{field, targetType, value})
                .success(false)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("[{}] msg={}", e.getClass(), e.getMessage(), e);

        return R.<Void>builder()
                .code(ServerErrorCode.B0001.getCode())
                .i18n(ServerErrorCode.B0001.getI18n())
                .success(false)
                .build();
    }

    private R<List<FieldErrorResp>> buildErrorResponse(BindingResult bindingResult) {
        List<FieldErrorResp> errors = bindingResult.getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResp(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        return R.<List<FieldErrorResp>>builder()
                .code(ClientErrorCode.A0400.getCode())
                .message(ClientErrorCode.A0400.getI18n())
                .data(errors)
                .success(false)
                .build();
    }
}
