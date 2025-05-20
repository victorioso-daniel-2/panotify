package com.panotify.util;

import java.sql.*;

/**
 * Utility class for database operations
 */
public class DatabaseUtil {

    // Your actual DB connection URL — update host and options as needed
    private static final String DB_URL = "jdbc:sqlserver://localhost;databaseName=panotify_db;encrypt=false;trustServerCertificate=true";

    private static final String DB_USER = "new_user"; // your new username
    private static final String DB_PASSWORD = "StrongP@ssword123";

    private static final boolean DEBUG = true; // Set to false in production

    // Static block to load the JDBC driver once when the class is loaded
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            if (DEBUG) System.out.println("JDBC Driver loaded successfully");
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
        try {
            if (DEBUG) System.out.println("Attempting to connect to: " + DB_URL);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (DEBUG) System.out.println("Database connection established successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            System.err.println("Connection URL: " + DB_URL);
            System.err.println("Username: " + DB_USER);
            throw e; // Re-throw the exception after logging
        }
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
            String sql = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if username exists: " + e.getMessage());
            throw e;
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
            String sql = "SELECT COUNT(*) AS count FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if email exists: " + e.getMessage());
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return exists;
    }
}
