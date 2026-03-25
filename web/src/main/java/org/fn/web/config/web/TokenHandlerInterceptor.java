package org.fn.web.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.fn.core.common.Global;
import org.fn.core.identity.IdentityResolver;
import org.fn.core.identity.IdentityUser;
import org.fn.web.component.HttpIdentitySource;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午10:06
 **/
@RequiredArgsConstructor
public class TokenHandlerInterceptor implements HandlerInterceptor {
    private final IdentityResolver resolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        IdentityUser identityUser = resolver.resolve(HttpIdentitySource.of(request));
        Global.User.set(identityUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Global.User.clear();
    }
}
