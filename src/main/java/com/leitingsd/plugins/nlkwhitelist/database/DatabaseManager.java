package com.leitingsd.plugins.nlkwhitelist.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private final HikariDataSource dataSource;

    public DatabaseManager(FileConfiguration config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("database.address") + ":" + config.getInt("database.port") + "/" + config.getString("database.name"));
        hikariConfig.setUsername(config.getString("database.user"));
        hikariConfig.setPassword(config.getString("database.password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(hikariConfig);
        DataTables.init(dataSource);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
