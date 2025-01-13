package Prism.Erp.config.business.Tenant;

import Prism.Erp.entity.business.Tenant.Tenant;
import Prism.Erp.exception.TenantNotFoundException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicTenantConnectionProvider implements MultiTenantConnectionProvider {

    private final DataSource defaultDataSource;
    private final TenantRepository tenantRepository;
    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    public DynamicTenantConnectionProvider(
            @Qualifier("defaultDataSource") DataSource defaultDataSource,
            TenantRepository tenantRepository) {
        this.defaultDataSource = defaultDataSource;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        DataSource dataSource = tenantDataSources.computeIfAbsent(
                tenantId,
                this::createDataSourceForTenant
        );
        return dataSource.getConnection();
    }

    private DataSource createDataSourceForTenant(String tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.getDatabaseUrl());
        config.setUsername(tenant.getDatabaseUsername());
        config.setPassword(tenant.getDatabasePassword());
        config.setPoolName("tenant-" + tenantId + "-pool");

        return new HikariDataSource(config);
    }
}
