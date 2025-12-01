package uni.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uni.example.model.PlayerScore;

public class DatabaseService {

    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());

    private Connection connection;

    public DatabaseService() {
        this("jdbc:sqlite:scoreboard.db");
    }

    public DatabaseService(final String dbUrl) {
        try {
            this.connection = DriverManager.getConnection(dbUrl);
            createTable();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    private void createTable() {
        final String sql = "CREATE TABLE IF NOT EXISTS scores (" +
                           "name TEXT PRIMARY KEY, " +
                           "wins INTEGER NOT NULL)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database table creation failed", e);
        }
    }

    public void addWin(final String playerName) {
        final String sql = "INSERT INTO scores (name, wins) VALUES (?, 1) " +
                           "ON CONFLICT(name) DO UPDATE SET wins = wins + 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update win count", e);
        }
    }

    public List<PlayerScore> getHighScores() {
        final List<PlayerScore> list = new ArrayList<>();
        final String sql = "SELECT name, wins FROM scores ORDER BY wins DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                list.add(new PlayerScore(result.getString("name"), result.getInt("wins")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get high scores", e);
        }
        return list;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to close database connection", e);
        }
    }
}
