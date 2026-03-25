package org.fn.web.component;

import jakarta.servlet.http.HttpServletRequest;
import org.fn.core.identity.IdentityProvider;
import org.fn.core.identity.IdentitySource;
import org.fn.core.identity.IdentityUser;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/25 上午10:53
 **/
@Component
public class HttpIdentityProvider implements IdentityProvider {
    @Override
    public boolean supports(IdentitySource source) {
        return source instanceof HttpIdentitySource;
    }

    @Override
    public IdentityUser resolve(IdentitySource source) {
        HttpIdentitySource httpSource = (HttpIdentitySource) source;
        HttpServletRequest request = httpSource.getRequest();
        // TODO: 将请求中的 token 解析成用户信息
        IdentityUser identityUser = new IdentityUser();
        identityUser.setId(1L);
        identityUser.setUsername("http");
        return identityUser;
    }
}
