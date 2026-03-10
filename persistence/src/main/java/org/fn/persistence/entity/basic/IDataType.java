package org.fn.persistence.entity.basic;

import org.fn.core.common.Const;
import org.fn.core.common.DataType;
import org.fn.persistence.database.MybatisDataProtectInterceptor;

/**
 * 标识该实体具有数据类型
 * <p>适用于存在无法删除的系统数据表</p>
 *
 * @author chenshoufeng
 * @see MybatisDataProtectInterceptor
 * @since 2026/3/6 上午9:04
 */
public interface IDataType {
    String COLUMN = Const.Database.DATA_TYPE;
    String FIELD = Const.Entity.DATA_TYPE;

    Integer getDataType();

    default DataType getDataTypeEnum() {
        Integer code = getDataType();
        return code == null ? null : DataType.of(code);
    }

    default boolean isSystemData() {
        return getDataTypeEnum() == DataType.SYSTEM;
    }

    default boolean isUserData() {
        return getDataTypeEnum() == DataType.USER;
    }
}