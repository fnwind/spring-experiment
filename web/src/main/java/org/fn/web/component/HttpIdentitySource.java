package org.fn.web.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.fn.core.identity.IdentitySource;

/**
 * @author chenshoufeng
 * @since 2026/3/25 上午10:53
 **/
@Data
public class HttpIdentitySource implements IdentitySource {
    private HttpServletRequest request;

    public static HttpIdentitySource of(HttpServletRequest request) {
        HttpIdentitySource source = new HttpIdentitySource();
        source.setRequest(request);
        return source;
    }
}
