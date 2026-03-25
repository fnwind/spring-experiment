package org.fn.persistence.database;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.fn.core.common.Const;
import org.fn.core.common.Global;
import org.fn.core.identity.IdentityUser;
import org.fn.persistence.entity.basic.ICreator;
import org.fn.persistence.entity.basic.IModifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author chenshoufeng
 * @since 2026/2/5 下午2:41
 **/
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        IdentityUser identityUser = Global.User.get();
        if (Objects.nonNull(identityUser)) {
            if (originalObject instanceof ICreator) {
                this.strictInsertFill(metaObject, Const.Entity.CREATOR_ID, Long.class, identityUser.getId());
                this.strictInsertFill(metaObject, Const.Entity.CREATOR_NAME, String.class, identityUser.getUsername());
            }
        }

        this.strictInsertFill(metaObject, Const.Entity.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        IdentityUser identityUser = Global.User.get();
        if (Objects.nonNull(identityUser)) {
            if (originalObject instanceof IModifier) {
                this.strictInsertFill(metaObject, Const.Entity.MODIFIER_ID, Long.class, identityUser.getId());
                this.strictInsertFill(metaObject, Const.Entity.MODIFIER_NAME, String.class, identityUser.getUsername());
            }
        }

        this.strictUpdateFill(metaObject, Const.Entity.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }
}
