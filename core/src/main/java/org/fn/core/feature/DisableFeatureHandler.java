package org.fn.core.feature;

import org.fn.core.common.Feature;

/**
 * 实现此接口以支持功能禁用的处理器
 *
 * @see DisableFeatureAspect
 */
public interface DisableFeatureHandler {
    /**
     * 当前实现哪种功能的禁用处理器
     */
    Feature support();

    void beforeIgnore();

    void afterIgnore();
}