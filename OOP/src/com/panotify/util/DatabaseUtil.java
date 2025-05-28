package com.panotify.util;

import java.sql.*;

/**
 * Utility class for database operations
 */
public class DatabaseUtil {
    // Database connection parameters
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=panotify_db;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "libraryAdmin";
    private static final String DB_PASSWORD = "Qazplm891251";
    
    // Static block to load the JDBC driver once when the class is loaded
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MSSQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get a connection to the database using try-with-resources
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Close database resources using try-with-resources
     * @param resources AutoCloseable resources to close
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
    
    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException if database operations fail
     */
    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM [user] WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        }
    }
    
    /**
     * Check if an email already exists in the database
     * @param email Email to check
     * @return true if the email exists, false otherwise
     * @throws SQLException if database operations fail
     */
    public static boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM [user] WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        }
    }
    
    /**
     * Check if a course code exists in the database
     * @param courseCode Course code to check
     * @return true if the course code exists, false otherwise
     * @throws SQLException if database operations fail
     */
    public static boolean courseCodeExists(String courseCode) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM course WHERE course_code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        }
    }
    
    /**
     * Get the next available ID for a table
     * @param tableName Name of the table
     * @param idColumnName Name of the ID column
     * @return Next available ID
     * @throws SQLException if database operations fail
     */
    public static int getNextId(String tableName, String idColumnName) throws SQLException {
        String sql = "SELECT IDENT_CURRENT(?) + IDENT_INCR(?) AS next_id";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tableName);
            pstmt.setString(2, tableName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("next_id") : 1;
            }
        }
    }
    
    /**
     * Begin a database transaction
     * @param conn Connection to set auto-commit false on
     * @throws SQLException if operation fails
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }
    
    /**
     * Commit a database transaction
     * @param conn Connection to commit
     * @throws SQLException if operation fails
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }
    
    /**
     * Rollback a database transaction
     * @param conn Connection to rollback
     */
    public static void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            }
        }
    }
}