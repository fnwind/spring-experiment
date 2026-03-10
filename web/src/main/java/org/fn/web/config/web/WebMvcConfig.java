package org.fn.web.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午10:24
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantHandlerInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(new TokenHandlerInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(new MDCHandlerInterceptor()).addPathPatterns("/api/**");
    }
}
