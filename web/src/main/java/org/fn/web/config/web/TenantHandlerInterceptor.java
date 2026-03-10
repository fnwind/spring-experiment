package org.fn.web.config.web;

import cn.hutool.core.util.StrUtil;
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
public class TenantHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(Const.Header.TENANT_ID);
        if (StrUtil.isNotBlank(tenantId)) {
            Global.Tenant.set(tenantId);
            MDC.put(Const.MDC.TENANT_ID, tenantId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        MDC.remove(Const.MDC.TENANT_ID);
        Global.Tenant.clear();
    }
}
