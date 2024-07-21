package com.leitingsd.plugins.nlkwhitelist.manager;

import com.leitingsd.plugins.nlkwhitelist.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WhitelistManager {
    private static DatabaseManager databaseManager;

    public WhitelistManager(DatabaseManager databaseManager) {
        WhitelistManager.databaseManager = databaseManager;
    }

    public static void addWhitelist(String player, String guarantor, String train, String description) {
        try (Connection connection = databaseManager.getConnection()) {
            String sql = "INSERT INTO whitelist (player, operator, guarantor, train, description) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, player);
                statement.setString(2, "console"); // 操作员设置为 "console"
                statement.setString(3, guarantor);
                statement.setString(4, train);
                statement.setString(5, description);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean removeWhitelist(String player, String reason) {
        try (Connection connection = databaseManager.getConnection()) {
            String sql = "UPDATE whitelist SET deleteAt = ?, deleteReason = ? WHERE player = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, System.currentTimeMillis());
                statement.setString(2, reason);
                statement.setString(3, player);
                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WhitelistRecord queryWhitelist(String player) {
        try (Connection connection = databaseManager.getConnection()) {
            String sql = "SELECT * FROM whitelist WHERE player = ? AND deleteAt = 0";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, player);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new WhitelistRecord(
                                resultSet.getString("player"),
                                resultSet.getString("guarantor"),
                                resultSet.getString("train"),
                                resultSet.getString("description")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isPlayerWhitelisted(String player) {
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
