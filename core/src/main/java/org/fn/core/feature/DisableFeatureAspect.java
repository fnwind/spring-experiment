package org.fn.core.feature;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.fn.core.common.Feature;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenshoufeng
 * @see Feature
 * @since 2026/3/6 下午2:39
 **/
@Aspect
@Component
@RequiredArgsConstructor
public class DisableFeatureAspect {
    private final List<DisableFeatureHandler> handlers;

    private static final ThreadLocal<EnumSet<Feature>> DISABLED =
            new TransmittableThreadLocal<>();

    @Around("@within(disable) || @annotation(disable)")
    public Object around(ProceedingJoinPoint joinPoint, DisableFeature disable) throws Throwable {
        EnumSet<Feature> features = EnumSet.noneOf(Feature.class);
        Collections.addAll(features, disable.value());

        // 注解继承
        EnumSet<Feature> previous = DISABLED.get();
        EnumSet<Feature> merged = previous == null
                ? EnumSet.copyOf(features)
                : EnumSet.copyOf(previous);
        merged.addAll(features);

        DISABLED.set(merged);

        try {
            handlers.stream()
                    .filter(h -> features.contains(h.support()))
                    .forEach(DisableFeatureHandler::beforeIgnore);

            return joinPoint.proceed();
        } finally {
            handlers.stream()
                    .filter(h -> features.contains(h.support()))
                    .forEach(DisableFeatureHandler::afterIgnore);
            // 注解还原
            DISABLED.set(previous);
        }
    }

    public static boolean isDisabled(Feature feature) {
        EnumSet<Feature> set = DISABLED.get();
        return set != null && set.contains(feature);
    }
}
