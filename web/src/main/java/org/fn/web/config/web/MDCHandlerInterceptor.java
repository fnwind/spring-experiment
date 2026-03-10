package org.fn.web.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fn.core.common.Const;
import org.fn.core.common.Global;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午10:06
 **/
public class MDCHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(Const.MDC.URL, request.getRequestURI());
        MDC.put(Const.MDC.TENANT_ID, Global.Tenant.get());
        MDC.put(Const.MDC.TRACE_ID, Global.TraceId.get());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        MDC.remove(Const.MDC.URL);
        MDC.remove(Const.MDC.TENANT_ID);
        MDC.remove(Const.MDC.TRACE_ID);
    }
}
