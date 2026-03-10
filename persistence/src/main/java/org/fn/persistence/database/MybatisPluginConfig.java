package org.fn.persistence.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author chenshoufeng
 * @since 2026/2/9 上午11:17
 **/
@MapperScan("org.fn.persistence.database.mapper")
@Configuration
@RequiredArgsConstructor
public class MybatisPluginConfig {
    private final MybatisExtensionProperties properties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 租户
        // TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        // tenantInterceptor.setTenantLineHandler(customTenantHandler);
        // interceptor.addInnerInterceptor(tenantInterceptor);

        // 租户及其他实现 InnerInterceptor 的插件
        innerInterceptors.forEach(interceptor::addInnerInterceptor);

        // 分页插件
        // 特性：支持通过配置强制指定分页数据库类型，特别对于在 sharding-jdbc 下达梦数据库使用场景。
        // 首先我们知道 sharding-jdbc 官方是不支持 dm 数据库的，
        // 但是实际上绝大部分语句都能正常执行，目前已知分页会报错，
        // 这是因为 mybatis 使用 oracle 方言生成分页语句，
        // 但是 sharding-jdbc 由于读取不到支持的数据库，使用了默认的 mysql 方言
        DbType customDbType = properties.getPageInterceptorDbType();
        interceptor.addInnerInterceptor(null == customDbType
                ? new PaginationInnerInterceptor()
                : new PaginationInnerInterceptor(customDbType));

        return interceptor;
    }

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(MybatisTenantLineHandler customTenantLineHandler) {
        return new TenantLineInnerInterceptor(customTenantLineHandler);
    }

    @Bean
    public DataPermissionInterceptor dataPermissionInterceptor(MybatisDataPermissionHandler customDataPermissionHandler) {
        return new DataPermissionInterceptor(customDataPermissionHandler);
    }
}
