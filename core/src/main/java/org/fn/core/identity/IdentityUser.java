package org.fn.core.identity;

import lombok.Data;
import org.fn.core.common.AccountType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenshoufeng
 * @since 2026/3/6 上午10:51
 **/
@Data
public class IdentityUser {
    private Long id;
    private String code;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private AccountType accountType;

    private Long orgId;
    private Set<Long> subOrgIds = new HashSet<>();

    private Set<Long> roleIds = new HashSet<>();

    public boolean isAdmin() {
        return AccountType.ADMIN.equals(accountType);
    }
}
