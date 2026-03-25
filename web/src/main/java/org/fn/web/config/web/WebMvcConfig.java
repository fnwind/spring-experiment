package org.fn.web.config.web;

import lombok.RequiredArgsConstructor;
import org.fn.core.identity.IdentityResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午10:24
 **/
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final IdentityResolver resolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // .order(Ordered.HIGHEST_PRECEDENCE)
        registry.addInterceptor(new TenantHandlerInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(new TokenHandlerInterceptor(resolver)).addPathPatterns("/api/**");
        registry.addInterceptor(new MDCHandlerInterceptor()).addPathPatterns("/api/**");
    }
}
