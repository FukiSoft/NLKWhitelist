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

    public CompletableFuture<Void> addPlayerToWhitelist(String player, String operator, String guarantor, String train, String description) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String sql = "INSERT INTO whitelist (player, operator, guarantor, train, description) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    statement.setString(2, operator);
                    statement.setString(3, guarantor);
                    statement.setString(4, train);
                    statement.setString(5, description);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public CompletableFuture<WhitelistRecord> queryWhitelist(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = databaseManager.getConnection()) {
                String sql = "SELECT * FROM whitelist WHERE player = ? ORDER BY time DESC";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            long deleteAt = resultSet.getLong("deleteAt");
                            String deleteAtFormatted = deleteAt == 0 ? "未删除" : String.valueOf(deleteAt);
                            return new WhitelistRecord(
                                    resultSet.getLong("id"),
                                    resultSet.getTimestamp("time").toInstant(),
                                    resultSet.getString("player"),
                                    resultSet.getString("operator"),
                                    resultSet.getString("guarantor"),
                                    resultSet.getString("train"),
                                    resultSet.getString("description"),
                                    deleteAtFormatted,
                                    resultSet.getString("deleteReason"),
                                    resultSet.getString("deleteOperator")
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

    public boolean isPlayerWhitelisted(String player) {
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
    }
}