package org.fn.persistence.entity.basic;

import org.fn.core.common.Const;

public interface ITenant {
    String COLUMN = Const.Database.TENANT_ID;
    String FIELD  = Const.Entity.TENANT_ID;

    String getTenantId();
}