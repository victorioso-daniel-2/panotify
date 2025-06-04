package com.panotify.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for database operations in the PaNotify application.
 * Provides methods for:
 * <ul>
 *   <li>Database connection management</li>
 *   <li>Resource cleanup</li>
 *   <li>Transaction handling</li>
 *   <li>Common database queries</li>
 * </ul>
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class DatabaseUtil {
    /** Database connection URL with server details and security settings */
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=panotify_db;encrypt=true;trustServerCertificate=true";
    
    /** Database username for authentication */
    private static final String DB_USER = "libraryAdmin";
    
    /** Database password for authentication */
    private static final String DB_PASSWORD = "Qazplm891251";
    
    /**
     * Static initialization block to load the SQL Server JDBC driver.
     * This ensures the driver is available before any connection attempts.
     */
    static {
        try {
            // Explicitly load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found. Make sure you have added the mssql-jdbc.jar to your project.");
            e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a connection to the database.
     * 
     * @return A Connection object representing the database connection
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Safely closes one or more database resources.
     * This method handles exceptions internally to ensure all resources are closed.
     * 
     * @param resources One or more AutoCloseable resources to close (Connection, Statement, ResultSet, etc.)
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error closing resource: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Checks if a username already exists in the database.
     * 
     * @param username The username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks if an email address already exists in the database.
     * 
     * @param email The email address to check
     * @return true if the email exists, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public static boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a course code already exists in the database.
     * 
     * @param courseCode The course code to check
     * @return true if the course code exists, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public static boolean courseCodeExists(String courseCode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Courses WHERE course_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Generates the next available ID for a specified table.
     * Useful for auto-incrementing IDs when adding new records.
     * 
     * @param tableName The name of the table
     * @param idColumnName The name of the ID column
     * @return The next available ID (current maximum + 1, or 1 if table is empty)
     * @throws SQLException If a database access error occurs
     */
    public static int getNextId(String tableName, String idColumnName) throws SQLException {
        String sql = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    /**
     * Begins a database transaction by disabling auto-commit.
     * 
     * @param conn The database connection
     * @throws SQLException If a database access error occurs
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }

    /**
     * Commits a database transaction and re-enables auto-commit.
     * 
     * @param conn The database connection
     * @throws SQLException If a database access error occurs
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }

    /**
     * Rolls back a database transaction in case of errors and re-enables auto-commit.
     * 
     * @param conn The database connection
     */
    public static void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 