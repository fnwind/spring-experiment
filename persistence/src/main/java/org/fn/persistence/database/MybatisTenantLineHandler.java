package org.fn.persistence.database;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.fn.core.common.Const;
import org.fn.core.common.Feature;
import org.fn.core.common.Global;
import org.fn.core.feature.DisableFeatureAspect;
import org.fn.persistence.entity.basic.ITenant;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/5 上午9:22
 **/
@Component
@RequiredArgsConstructor
public class MybatisTenantLineHandler implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        return new StringValue(Global.Tenant.get());
    }

    @Override
    public String getTenantIdColumn() {
        return Const.Database.TENANT_ID;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 主动关闭租户能力
        if (DisableFeatureAspect.isDisabled(Feature.TENANT)) return true;

        // 未匹配表信息
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (tableInfo == null) return true;

        // 未实现租户接口
        return !ITenant.class.isAssignableFrom(tableInfo.getEntityType());
    }
}
