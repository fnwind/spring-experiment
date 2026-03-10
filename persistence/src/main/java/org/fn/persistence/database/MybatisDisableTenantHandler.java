package org.fn.persistence.database;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import org.fn.core.common.Feature;
import org.fn.core.feature.DisableFeatureHandler;
import org.springframework.stereotype.Component;

@Component
public class MybatisDisableTenantHandler implements DisableFeatureHandler {
    @Override
    public Feature support() {
        return Feature.TENANT;
    }

    @Override
    public void beforeIgnore() {
        InterceptorIgnoreHelper.handle(
                IgnoreStrategy.builder().tenantLine(true).build()
        );
    }

    @Override
    public void afterIgnore() {
        InterceptorIgnoreHelper.clearIgnoreStrategy();
    }
}