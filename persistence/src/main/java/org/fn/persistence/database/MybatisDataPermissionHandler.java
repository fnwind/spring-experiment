package org.fn.persistence.database;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import org.fn.core.common.Const;
import org.fn.core.common.Feature;
import org.fn.core.common.Global;
import org.fn.core.feature.DisableFeatureAspect;
import org.fn.persistence.entity.basic.IOwner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MybatisDataPermissionHandler implements MultiDataPermissionHandler {
    @Override
    @SneakyThrows
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        if (DisableFeatureAspect.isDisabled(Feature.DATA_PERMISSION)) return null;
        if (Global.User.get() == null) return null;

        TableInfo tableInfo = findTableInfo(table.getName());
        if (tableInfo == null) {
            log.warn("No TableInfo found for table: {}, skipping data permission handling", table.getName());
            return null;
        }

        if (IOwner.class.isAssignableFrom(tableInfo.getEntityType())) {
            log.debug("Applying data permission for table: {}, mappedStatementId: {}", table.getName(), mappedStatementId);
            return buildOwnerExpression(Global.User.get().getId());
        }

        return null;
    }

    // TODO: 扩展到相同的组织？
    // TODO: 没有操作者的数据是否公开？
    private Expression buildOwnerExpression(Long userId) throws JSQLParserException {
        return CCJSqlParserUtil.parseCondExpression(
                Const.Database.CREATOR_ID + " = " + userId
        );
    }

    private TableInfo findTableInfo(String tableName) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (tableInfo != null) {
            return tableInfo;
        }

        // 兼容表名大小写不敏感的情况
        return TableInfoHelper.getTableInfos().stream()
                .filter(t -> t.getTableName().equalsIgnoreCase(tableName))
                .findFirst()
                .orElse(null);
    }
}