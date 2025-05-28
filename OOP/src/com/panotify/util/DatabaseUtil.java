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
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Close database resources
     * @param conn Connection object
     * @param stmt Statement object
     * @param rs ResultSet object
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing Statement: " + e.getMessage());
        }
        
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
    }
    
    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if the username exists, false otherwise
     * @throws SQLException if database operations fail
     */
    public static boolean usernameExists(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE username = ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return exists;
    }
    
    /**
     * Check if an email already exists in the database
     * @param email Email to check
     * @return true if the email exists, false otherwise
     * @throws SQLException if database operations fail
     */
    public static boolean emailExists(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE email = ?");
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return exists;
    }
}