package com.panotify.service;

import com.panotify.model.User;
import com.panotify.model.Student;
import com.panotify.model.Instructor;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Service class for handling user authentication and password management.
 * Provides functionality for user login, password changes, and validation.
 * Implements secure password hashing using SHA-256.
 */
public class AuthenticationService {
    
    /**
     * Authenticates a user with the provided credentials.
     * 
     * @param username The user's username
     * @param password The user's password
     * @param accountType The type of account ("Student" or "Instructor")
     * @return User object if authentication successful, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User login(String username, String password, String accountType) throws SQLException {
        String sql = "SELECT * FROM [user] WHERE username = ? AND password = ? AND account_type = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); // Using plain text password
            stmt.setString(3, accountType.toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user;
                    String type = rs.getString("account_type").toLowerCase();
                    
                    if (type.equals("student")) {
                        user = new Student();
                    } else if (type.equals("instructor")) {
                        user = new Instructor();
                    } else {
                        user = new User();
                    }
                    
                    user.setUserId(rs.getInt("user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setAccountType(rs.getString("account_type"));
                    user.setInstitution(rs.getString("institution"));
                    user.setDepartment(rs.getString("department"));
                    return user;
                }
            }
        }
        return null;
    }
    
    /**
     * Changes a user's password after verifying the old password.
     * 
     * @param userId The ID of the user
     * @param oldPassword The current password
     * @param newPassword The new password to set
     * @return true if password change successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE user_id = ? AND password = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashPassword(newPassword));
            stmt.setInt(2, userId);
            stmt.setString(3, hashPassword(oldPassword));
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Resets a user's password using their email address.
     * This method should be used for password recovery.
     * 
     * @param email The user's email address
     * @param newPassword The new password to set
     * @return true if password reset successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean resetPassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashPassword(newPassword));
            stmt.setString(2, email);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Hashes a password using SHA-256 algorithm.
     * 
     * @param password The plain text password to hash
     * @return Base64 encoded string of the password hash
     * @throws RuntimeException if the hashing algorithm is not available
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Validates a password against security requirements.
     * Password must be at least 8 characters long and contain at least one number and one letter.
     * 
     * @param password The password to validate
     * @return true if password meets requirements, false otherwise
     */
    public boolean validatePassword(String password) {
        // Password must be at least 8 characters long and contain at least one number and one letter
        return password != null && 
               password.length() >= 8 && 
               password.matches(".*[0-9].*") && 
               password.matches(".*[a-zA-Z].*");
    }
    
    /**
     * Validates an email address format.
     * Uses a basic regex pattern to check if the email format is valid.
     * 
     * @param email The email address to validate
     * @return true if email format is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        // Basic email validation
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
} 