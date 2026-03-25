package org.fn.core.identity;

public interface IdentityProvider {

    /**
     * 是否支持当前场景
     */
    boolean supports(IdentitySource source);

    /**
     * 解析用户
     */
    IdentityUser resolve(IdentitySource source);
}