package org.fn.web.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fn.core.common.Const;
import org.fn.core.common.Global;
import org.fn.core.identity.IdentityUser;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

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

        IdentityUser identityUser = Global.User.get();
        if (Objects.nonNull(identityUser)) {
            MDC.put(Const.MDC.USERNAME, identityUser.getUsername());
        }

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
