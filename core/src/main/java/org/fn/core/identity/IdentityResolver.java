package org.fn.core.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IdentityResolver {

    private final List<IdentityProvider> providers;
    private final AnonymousIdentityProvider anonymousProvider;

    public IdentityUser resolve(IdentitySource source) {
        for (IdentityProvider provider : providers) {
            if (provider.supports(source)) {
                IdentityUser user = provider.resolve(source);
                if (user != null) {
                    return user;
                }
            }
        }
        return anonymousProvider.resolve(source);
    }
}