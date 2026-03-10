package org.fn.web.config.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fn.core.common.Const;
import org.fn.core.common.Global;
import org.fn.core.common.TraceIdGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午9:58
 **/
@Component
public class WebRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = TraceIdGenerator.getOrCreateTraceId(request.getHeader(Const.Header.TRACE_ID));
            Global.TraceId.set(traceId);
            response.setHeader(Const.Header.TRACE_ID, traceId);
            filterChain.doFilter(request, response);
        } finally {
            Global.TraceId.clear();
        }
    }
}
