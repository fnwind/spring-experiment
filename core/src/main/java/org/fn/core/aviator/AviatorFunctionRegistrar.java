package org.fn.core.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AviatorFunctionRegistrar {

    private final ApplicationContext context;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        registerFunctions();
    }

    private void registerFunctions() {
        Map<String, Object> beans = context.getBeansWithAnnotation(Component.class);

        for (Object bean : beans.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (Method method : targetClass.getMethods()) {
                AviatorMethod ann = method.getAnnotation(AviatorMethod.class);
                if (ann != null) {
                    String functionName = ann.namespace().isEmpty()
                            ? ann.method()
                            : ann.namespace() + "." + ann.method();
                    addFunction(bean, method, functionName);
                    log.info("Registered Aviator function: [{}] for method: [{}.{}]", functionName, targetClass.getSimpleName(), method.getName());
                }
            }
        }
    }

    private void addFunction(Object bean, Method method, String functionName) {
        AbstractFunction fn = new AbstractFunction() {

            @Override
            public String getName() {
                return functionName;
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                return callWithArgs(env, arg1);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                return callWithArgs(env, arg1, arg2);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
                return callWithArgs(env, arg1, arg2, arg3);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3,
                                      AviatorObject arg4) {
                return callWithArgs(env, arg1, arg2, arg3, arg4);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3,
                                      AviatorObject arg4, AviatorObject arg5) {
                return callWithArgs(env, arg1, arg2, arg3, arg4, arg5);
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3,
                                      AviatorObject arg4, AviatorObject arg5, AviatorObject arg6) {
                return callWithArgs(env, arg1, arg2, arg3, arg4, arg5, arg6);
            }

            private AviatorObject callWithArgs(Map<String, Object> env, AviatorObject... args) {
                try {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object[] javaArgs = new Object[args.length];

                    for (int i = 0; i < args.length; i++) {
                        Object value = args[i].getValue(env);
                        javaArgs[i] = convert(value, paramTypes[i]);
                    }

                    log.debug("Invoking Aviator function [{}] with Java args: {}", functionName, javaArgs);
                    Object result = method.invoke(bean, javaArgs);
                    return AviatorRuntimeJavaType.valueOf(result);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke aviator method: " + method.getName(), e);
                }
            }

            private Object convert(Object value, Class<?> targetType) {
                if (value == null) {
                    return null;
                }

                if (targetType.isInstance(value)) {
                    return value;
                }

                if (value instanceof Number num) {
                    if (targetType == Integer.class || targetType == int.class) {
                        return num.intValue();
                    }
                    if (targetType == Long.class || targetType == long.class) {
                        return num.longValue();
                    }
                    if (targetType == Double.class || targetType == double.class) {
                        return num.doubleValue();
                    }
                    if (targetType == Float.class || targetType == float.class) {
                        return num.floatValue();
                    }
                    if (targetType == Short.class || targetType == short.class) {
                        return num.shortValue();
                    }
                    if (targetType == Byte.class || targetType == byte.class) {
                        return num.byteValue();
                    }
                }

                return value;
            }
        };

        AviatorEvaluator.addFunction(fn);
    }
}