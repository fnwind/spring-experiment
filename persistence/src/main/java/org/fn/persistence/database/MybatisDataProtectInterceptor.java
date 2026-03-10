package org.fn.persistence.database;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.fn.core.common.Feature;
import org.fn.core.feature.DisableFeatureAspect;
import org.fn.persistence.entity.basic.IDataType;
import org.fn.persistence.entity.basic.IEntity;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 保护系统数据不被修改或删除
 *
 * @author chenshoufeng
 * @see IDataType
 * @since 2026/3/6 上午9:04
 **/
@Slf4j
@Component
public class MybatisDataProtectInterceptor implements InnerInterceptor {
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (DisableFeatureAspect.isDisabled(Feature.DATA_PROTECT)) return;

        SqlCommandType type = ms.getSqlCommandType();
        log.debug("Executing SQL command: {}, parameter: {}", type, parameter);

        if (type == SqlCommandType.UPDATE || type == SqlCommandType.DELETE) {
            if (parameter instanceof IDataType entity && entity.isSystemData()) {
                throw new RuntimeException("Cannot modify system data");
            }
        }
    }
}
