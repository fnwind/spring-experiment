package org.fn.core.identity;

import org.fn.core.common.AccountType;
import org.springframework.stereotype.Component;

/**
 * 提供匿名用户身份
 * <p>当无法获取用户，且必须要用户才能正常流转时，提供默认用户实现</p>
 */
@Component
public class AnonymousIdentityProvider implements IdentityProvider {

    @Override
    public boolean supports(IdentitySource source) {
        return true;
    }

    @Override
    public IdentityUser resolve(IdentitySource source) {
        IdentityUser user = new IdentityUser();
        // TODO: 完善用户信息
        user.setId(0L);
        user.setUsername("anonymous");
        user.setAccountType(AccountType.USER);
        return user;
    }
}