package org.fn.persistence.database;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/6 下午3:57
 **/
@Data
@Component
@ConfigurationProperties("mybatis-plus.extension")
public class MybatisExtensionProperties {
    /**
     * 强制指定分页插件的数据库类型
     */
    private DbType pageInterceptorDbType;
}
