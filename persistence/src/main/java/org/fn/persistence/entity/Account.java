package org.fn.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.fn.core.common.AccountType;
import org.fn.persistence.entity.basic.IDataType;
import org.fn.persistence.entity.basic.ITenant;
import org.fn.persistence.entity.basic.OwnerEntity;

/**
 * @author chenshoufeng
 * @since 2026/3/5 上午10:44
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("account")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends OwnerEntity implements ITenant, IDataType {
    private String code;
    private String username;
    private Integer accountType;
    private String password;
    private String email;
    private String nickname;
    private String phone;

    private String tenantId;
    private Integer dataType;

    public boolean isAdmin() {
        if (accountType == null) {
            return false;
        }

        return AccountType.ADMIN.equals(AccountType.of(accountType));
    }
}
