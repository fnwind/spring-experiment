package org.fn.web.config.web;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.fn.core.common.Global;
import org.fn.persistence.result.R;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final MessageSource messageSource;

    @Override
    public boolean supports(@NotNull MethodParameter returnType,
                            @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        if (!(body instanceof R<?> r)) return body;
        if (StrUtil.isNotBlank(r.getMessage())) return r;

        // 国际化翻译
        String message = messageSource.getMessage(
                r.getI18n(),
                r.getArgs(),
                r.getI18n(),
                LocaleContextHolder.getLocale()
        );

        return R.builder()
                .code(r.getCode())
                .i18n(r.getI18n())
                .args(r.getArgs())
                .message(message)
                .data(r.getData())
                .success(r.isSuccess())
                .traceId(Global.TraceId.get())
                .build();
    }
}