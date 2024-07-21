package com.leitingsd.plugins.nlkwhitelist.manager;

import com.leitingsd.plugins.nlkwhitelist.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class WhitelistManager {
    private final DatabaseManager databaseManager;

    public WhitelistManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public CompletableFuture<Boolean> isPlayerWhitelisted(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String sql = "SELECT COUNT(*) FROM whitelist WHERE player = ? AND deleteAt = 0";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt(1) > 0;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public CompletableFuture<Void> addPlayerToWhitelist(String player, String operator, String guarantor, String train, String description) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM whitelist WHERE player = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                    checkStatement.setString(1, player);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            // 玩家存在，更新记录
                            String updateSql = "UPDATE whitelist SET operator = ?, guarantor = ?, train = ?, description = ?, deleteAt = 0, deleteReason = NULL, deleteOperator = NULL WHERE player = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                                updateStatement.setString(1, operator);
                                updateStatement.setString(2, guarantor);
                                updateStatement.setString(3, train);
                                updateStatement.setString(4, description);
                                updateStatement.setString(5, player);
                                updateStatement.executeUpdate();
                            }
                        } else {
                            // 玩家不存在，插入新记录
                            String insertSql = "INSERT INTO whitelist (player, operator, guarantor, train, description) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                                insertStatement.setString(1, player);
                                insertStatement.setString(2, operator);
                                insertStatement.setString(3, guarantor);
                                insertStatement.setString(4, train);
                                insertStatement.setString(5, description);
                                insertStatement.executeUpdate();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<WhitelistRecord> queryWhitelist(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String sql = "SELECT * FROM whitelist WHERE player = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            return new WhitelistRecord(
                                    resultSet.getLong("id"),
                                    resultSet.getString("player"),
                                    resultSet.getString("operator"),
                                    resultSet.getString("guarantor"),
                                    resultSet.getString("train"),
                                    resultSet.getString("description"),
                                    resultSet.getTimestamp("time"),
                                    resultSet.getLong("deleteAt"),
                                    resultSet.getString("deleteOperator"),
                                    resultSet.getString("deleteReason")
                            );
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> removePlayerFromWhitelist(String player, String reason, String operator) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String sql = "UPDATE whitelist SET deleteAt = ?, deleteReason = ?, deleteOperator = ? WHERE player = ? AND deleteAt = 0";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, System.currentTimeMillis());
                    statement.setString(2, reason);
                    statement.setString(3, operator);
                    statement.setString(4, player);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
