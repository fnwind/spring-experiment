package org.fn.persistence.database;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MybatisDataPermissionHandler implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        // 本质上就是修改 SQL 语句的 WHERE 条件，添加数据权限相关的条件

        return null;
    }
}