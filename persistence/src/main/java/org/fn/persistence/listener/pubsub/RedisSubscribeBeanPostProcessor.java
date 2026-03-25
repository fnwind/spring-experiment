package org.fn.persistence.listener.pubsub;

import lombok.RequiredArgsConstructor;
import org.fn.persistence.listener.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RedisSubscribeBeanPostProcessor implements BeanPostProcessor {
    private final RedisMessageListenerContainer container;
    private final ObjectMapper objectMapper;

    @Override
    public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) {
        Method[] methods = bean.getClass().getMethods();

        for (Method method : methods) {
            RedisSubscribeListener annotation = method.getAnnotation(RedisSubscribeListener.class);
            if (annotation == null) {
                continue;
            }

            checkMethodSignature(method);
            registerListener(bean, method, annotation);
        }

        return bean;
    }

    private void checkMethodSignature(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Assert.isTrue(paramTypes.length <= 2, "方法 " + method.getName() + " 的参数数量超过限制（<=2）");
        if (paramTypes.length == 2) {
            Assert.isTrue(paramTypes[0] == String.class, "方法 " + method.getName() + " 的第一个参数必须是 String 类型");
        }
    }

    private void registerListener(Object bean, Method method, RedisSubscribeListener annotation) {
        MessageListener listener = (message, pattern) -> {
            try {
                String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
                String body = new String(message.getBody(), StandardCharsets.UTF_8);
                method.invoke(bean, convertMessage(method, channel, body));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        for (String channel : annotation.channels()) {
            container.addMessageListener(listener, new ChannelTopic(channel));
        }
    }

    private Object[] convertMessage(Method method, String channel, String body) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            // Message<Model> / Model / Integer / ...
            Class<?> type = parameterTypes[0];
            if (!Message.class.isAssignableFrom(type)) {
                return new Object[]{convertValue(body, type)};
            }
            return new Object[]{convertValue(body, Message.class)};
        } else {
            // String, Model
            return new Object[]{channel, convertValue(body, parameterTypes[1])};
        }
    }

    private Object convertValue(String body, Class<?> type) {
        if (type == String.class) {
            return body;
        }

        if (type == byte[].class) {
            return body.getBytes(StandardCharsets.UTF_8);
        }

        return objectMapper.readValue(body, type);
    }
}