package org.fn.core.feature;

import org.fn.core.common.Feature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用框架提供的扩展能力
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableFeature {
    Feature[] value();
}