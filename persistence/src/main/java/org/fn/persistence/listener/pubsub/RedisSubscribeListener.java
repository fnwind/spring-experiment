package org.fn.persistence.listener.pubsub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSubscribeListener {
    String[] channels() default {};

    String[] patterns() default {};
}
