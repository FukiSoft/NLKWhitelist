package com.leitingsd.plugins.nlkwhitelist.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseManager {

    private final NLKWhitelist plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(NLKWhitelist plugin) {
        this.plugin = plugin;
    }

    public void init(Map<String, Object> config) {
        Logger logger = plugin.getLogger();
        HikariConfig hikariConfig = new HikariConfig();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC 驱动程序加载失败", e);
        }

        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.get("host") + ":" + config.get("port") + "/" + config.get("database") + "?useSSL=" + config.get("usessl"));
        hikariConfig.setUsername((String) config.get("user"));
        hikariConfig.setPassword((String) config.get("password"));
        config.forEach((key, value) -> hikariConfig.addDataSourceProperty(key, value));
        this.dataSource = new HikariDataSource(hikariConfig);
        logger.info("数据库连接已初始化");

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS whitelist (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "player VARCHAR(255) NOT NULL, " +
                    "operator VARCHAR(255), " +
                    "guarantor VARCHAR(255), " +
                    "train VARCHAR(255), " +
                    "description TEXT, " +
                    "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "deleteAt BIGINT DEFAULT 0, " +
                    "deleteReason TEXT, " +
                    "deleteOperator VARCHAR(255)" +
                    ")";
            statement.execute(sql);
            logger.info("表 'whitelist' 已检查/创建");
        } catch (SQLException e) {
            logger.error("无法创建表 'whitelist'", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
