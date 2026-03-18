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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * @author chenshoufeng
 * @since 2026/3/17 下午2:53
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<R<Void>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        if (e instanceof ClientException) {
            log.warn("[ClientException] code={}, msg={}", errorCode.getCode(), e.getMessage(), e);
        } else {
            log.error("[{}] code={}, msg={}", e.getClass().getName(), errorCode.getCode(), e.getMessage(), e);
        }

        R<Void> result = R.<Void>builder()
                .code(errorCode.getCode())
                .i18n(e.getI18n())
                .args(e.getArgs())
                .success(false)
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<List<FieldErrorResp>>> handleBindErrors(MethodArgumentNotValidException e) {
        ClientErrorCode errorCode = ClientErrorCode.A0400;
        log.warn("[MethodArgumentNotValidException] code={} msg={}", errorCode.getCode(), e.getMessage(), e);

        R<List<FieldErrorResp>> result = buildErrorResponse(e.getBindingResult(), errorCode);
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<R<List<FieldErrorResp>>> handleBindErrors(BindException e) {
        ClientErrorCode errorCode = ClientErrorCode.A0400;
        log.warn("[BindException] code={} msg={}", errorCode.getCode(), e.getMessage(), e);

        R<List<FieldErrorResp>> result = buildErrorResponse(e.getBindingResult(), errorCode);
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<R<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        ClientErrorCode errorCode = ClientErrorCode.A0400;
        log.warn("[MethodArgumentTypeMismatchException] code={} msg={}", errorCode.getCode(), e.getMessage(), e);

        String field = e.getName();
        Object value = e.getValue();
        String targetType = e.getRequiredType() != null
                ? e.getRequiredType().getSimpleName()
                : "unknown";

        R<Void> result = R.<Void>builder()
                .code(errorCode.getCode())
                .i18n("common.exception.method-argument-type-mismatch")
                .args(new Object[]{field, targetType, value})
                .success(false)
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    // 不处理 NoResourceFoundException 异常
    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResource(NoResourceFoundException e) throws NoResourceFoundException {
        throw e;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleException(Exception e) {
        ServerErrorCode errorCode = ServerErrorCode.B0001;
        log.error("[{}] code={} msg={}", e.getClass(), errorCode.getCode(), e.getMessage(), e);

        R<Void> result = R.<Void>builder()
                .code(errorCode.getCode())
                .i18n(errorCode.getI18n())
                .success(false)
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(result);
    }

    private R<List<FieldErrorResp>> buildErrorResponse(BindingResult bindingResult, ErrorCode errorCode) {
        List<FieldErrorResp> errors = bindingResult.getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResp(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        return R.<List<FieldErrorResp>>builder()
                .code(errorCode.getCode())
                .i18n(errorCode.getI18n())
                .data(errors)
                .success(false)
                .build();
    }
}
