package org.fn.web.enhance.systemlog;

import org.fn.core.common.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    LogType logType() default LogType.CUSTOM;

    boolean onError() default false;

    String value();
}
