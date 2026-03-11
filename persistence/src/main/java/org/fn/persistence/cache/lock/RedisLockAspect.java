package org.fn.persistence.cache.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
public class RedisLockAspect {
    private final RedisLockService lockService;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer =
            new DefaultParameterNameDiscoverer();

    public RedisLockAspect(RedisLockService lockService) {
        this.lockService = lockService;
    }

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String key = parseKey(redisLock.key(), method, pjp.getArgs());
        Duration expire = Duration.ofSeconds(redisLock.expire());

        return lockService.executeWithLock(
                key,
                expire,
                () -> {
                    try {
                        return pjp.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private String parseKey(String key, Method method, Object[] args) {
        if (!key.contains("#")) {
            return key;
        }

        EvaluationContext context = new MethodBasedEvaluationContext(
                null,
                method,
                args,
                nameDiscoverer
        );

        Expression expression = parser.parseExpression(key);
        Object value = expression.getValue(context);

        return value == null ? null : value.toString();
    }
}