package org.fn.persistence.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.fn.core.common.Global;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedisDbAspect {
    @Around("@annotation(RedisDb) || @within(RedisDb)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        RedisDb redisDb = AnnotationUtils.findAnnotation(method, RedisDb.class);

        if (redisDb == null) {
            redisDb = AnnotationUtils.findAnnotation(
                    pjp.getTarget().getClass(),
                    RedisDb.class
            );
        }

        if (redisDb != null) {
            Global.Redis.set(redisDb.value());
        }

        try {
            return pjp.proceed();
        } finally {
            Global.Redis.clear();
        }
    }
}
