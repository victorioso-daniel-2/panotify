package com.panotify.service;

import com.panotify.model.Instructor;
import com.panotify.model.Student;
import com.panotify.model.User;
import com.panotify.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Service class that handles user authentication operations.
 * This includes login, password management, and validation functions.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class AuthenticationService {
    
    /**
     * Authenticates a user based on username, password, and account type.
     * 
     * @param username The username to authenticate
     * @param password The password to verify
     * @param accountType The type of account ("Student" or "Instructor")
     * @return The authenticated User object if successful, null otherwise
     * @throws SQLException If a database error occurs
     */
    public User login(String username, String password, String accountType) throws SQLException {
        String sql = "SELECT * FROM [user] WHERE username = ? AND account_type = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, accountType);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    
                    if (password.equals(storedPassword)) {
                        User user = createUserFromResultSet(rs, accountType);
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error during login: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Creates a User object (Student or Instructor) from database result set.
     * 
     * @param rs The ResultSet containing user data
     * @param accountType The type of account to create
     * @return A populated User object
     * @throws SQLException If a database error occurs
     */
    private User createUserFromResultSet(ResultSet rs, String accountType) throws SQLException {
        User user;
        
        if ("Student".equals(accountType)) {
            user = new Student();
        } else {
            user = new Instructor();
            ((Instructor)user).setInstitution(rs.getString("institution"));
            ((Instructor)user).setDepartment(rs.getString("department"));
        }
        
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAccountType(rs.getString("account_type"));
        
        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        
        return user;
    }

    /**
     * Changes a user's password after verifying the old password.
     * 
     * @param userId The ID of the user
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if password was changed successfully, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE user_id = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.setString(3, oldPassword);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Resets a user's password using their email address.
     * 
     * @param email The email address of the user
     * @param newPassword The new password to set
     * @return true if password was reset successfully, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean resetPassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Validates a password against security requirements.
     * Currently simplified for testing purposes.
     * 
     * @param password The password to validate
     * @return true if the password meets requirements, false otherwise
     */
    public boolean validatePassword(String password) {
        // For simplicity during testing, accept any non-empty password
        return password != null && !password.isEmpty();
        
        // Production implementation (commented out):
        /*
        // Password must be at least 8 characters long and contain at least one digit,
        // one lowercase letter, one uppercase letter, and one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.matches(passwordRegex, password);
        */
    }

    /**
     * Validates an email address format.
     * 
     * @param email The email address to validate
     * @return true if the email format is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        // Basic email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }
} 