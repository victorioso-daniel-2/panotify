package com.panotify.dao;

import com.panotify.model.User;
import com.panotify.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    
    /**
     * Register a new user in the database
     * 
     * @param user The user object to register
     * @return The generated user ID if successful, -1 otherwise
     * @throws SQLException If a database error occurs
     */
    public int registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, username, password_hash, phone_number, account_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        // Hash the password before storing it
        String hashedPassword = hashPassword(user.getPassword());
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, hashedPassword); // Use the hashed password
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAccountType());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        
                        // If this is an instructor, add institution and department info to instructor table
                        if ("Instructor".equals(user.getAccountType()) && 
                            user.getInstitution() != null && 
                            user.getDepartment() != null) {
                            
                            addInstructorInfo(userId, user.getInstitution(), user.getDepartment());
                        }
                        
                        return userId;
                    }
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Add instructor-specific information to the instructor table
     */
    private void addInstructorInfo(int userId, String institution, String department) throws SQLException {
        String sql = "INSERT INTO instructor (user_id, institution, department) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, institution);
            pstmt.setString(3, department);
            
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Authenticate a user by username and password
     * 
     * @param username The username to check
     * @param password The password to verify
     * @return User object if authentication is successful, null otherwise
     * @throws SQLException If a database error occurs
     */
    public User authenticateUser(String username, String password) throws SQLException {
        // First, retrieve the user by username only
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Get the stored password hash
                    String storedHash = rs.getString("password_hash");
                    
                    // Verify the provided password against the stored hash
                    if (verifyPassword(password, storedHash)) {
                        User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("phone_number"),
                            rs.getString("account_type"),
                            null, // institution - will be populated for instructors
                            null  // department - will be populated for instructors
                        );
                        
                        // If this is an instructor, get their institution and department
                        if ("Instructor".equals(user.getAccountType())) {
                            loadInstructorInfo(user);
                        }
                        
                        return user;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Verify a password against a stored hash
     * 
     * @param password The plain text password to verify
     * @param storedHash The stored password hash from the database
     * @return true if the password matches the hash, false otherwise
     */
    private boolean verifyPassword(String password, String storedHash) {
        // This implementation will depend on how your passwords are hashed
        // You will need to replace this with the actual hash verification logic used in your application
        
        try {
            // Example using a library like BCrypt
            // return BCrypt.checkpw(password, storedHash);
            
            // Temporary implementation - replace this with your actual verification logic
            // The actual implementation depends on the hashing algorithm your application uses
            return hashPassword(password).equals(storedHash);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Hash a password using the same algorithm used during registration
     * 
     * @param password The plain text password to hash
     * @return The hashed password
     */
    private String hashPassword(String password) {
        // This implementation will depend on how your passwords are hashed
        // You will need to replace this with the actual hashing logic used in your application
        
        try {
            // Example using a library like BCrypt
            // return BCrypt.hashpw(password, BCrypt.gensalt());
            
            // Temporary implementation - replace this with your actual hashing logic
            // The actual implementation depends on the hashing algorithm your application uses
            
            // This is just a placeholder and NOT secure - replace with your actual hashing algorithm
            return password; // NOT SECURE - REPLACE THIS
        } catch (Exception e) {
            // Log the error
            System.err.println("Error hashing password: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Load instructor-specific information from the instructor table
     */
    private void loadInstructorInfo(User user) throws SQLException {
        String sql = "SELECT institution, department FROM instructor WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, user.getId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user.setInstitution(rs.getString("institution"));
                    user.setDepartment(rs.getString("department"));
                }
            }
        }
    }
    
    /**
     * Get a user by ID
     * 
     * @param userId The user ID to look up
     * @return User object if found, null otherwise
     * @throws SQLException If a database error occurs
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("phone_number"),
                        rs.getString("account_type"),
                        null, // institution - will be populated for instructors
                        null  // department - will be populated for instructors
                    );
                    
                    // If this is an instructor, get their institution and department
                    if ("Instructor".equals(user.getAccountType())) {
                        loadInstructorInfo(user);
                    }
                    
                    return user;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Update a user's profile information
     * 
     * @param user The updated user object
     * @return true if update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone_number = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setInt(4, user.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            // If this is an instructor, update institution and department info
            if ("Instructor".equals(user.getAccountType()) && 
                user.getInstitution() != null && 
                user.getDepartment() != null) {
                
                updateInstructorInfo(user);
            }
            
            return affectedRows > 0;
        }
    }
    
    /**
     * Update instructor-specific information
     */
    private void updateInstructorInfo(User user) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM instructor WHERE user_id = ?";
        String insertSql = "INSERT INTO instructor (user_id, institution, department) VALUES (?, ?, ?)";
        String updateSql = "UPDATE instructor SET institution = ?, department = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, user.getId());
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Update existing record
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, user.getInstitution());
                        updateStmt.setString(2, user.getDepartment());
                        updateStmt.setInt(3, user.getId());
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insert new record
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, user.getId());
                        insertStmt.setString(2, user.getInstitution());
                        insertStmt.setString(3, user.getDepartment());
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }
    
    /**
     * Update a user's password
     * 
     * @param userId The ID of the user
     * @param newPassword The new password
     * @return true if update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        
        // Hash the new password before storing it
        String hashedPassword = hashPassword(newPassword);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashedPassword); // Use the hashed password
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}