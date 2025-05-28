package com.panotify.service;

import com.panotify.model.*;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class AuthenticationService {
    
    public User login(String username, String password, String accountType) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND account_type = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (verifyPassword(password, storedPassword)) {
                    // Update last login
                    updateLastLogin(rs.getInt("user_id"));
                    
                    // Create and return appropriate user object
                    if (accountType.equals("Student")) {
                        Student student = new Student();
                        populateUserFromResultSet(student, rs);
                        student.setStudentId(rs.getInt("user_id"));
                        return student;
                    } else {
                        return getInstructorWithDetails(rs.getInt("user_id"), rs);
                    }
                }
            }
        }
        return null;
    }
    
    private Instructor getInstructorWithDetails(int instructorId, ResultSet userRs) throws SQLException {
        Instructor instructor = new Instructor();
        populateUserFromResultSet(instructor, userRs);
        instructor.setInstructorId(instructorId);
        
        // Get instructor-specific details
        String sql = "SELECT institution, department FROM instructors WHERE instructor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructorId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                instructor.setInstitution(rs.getString("institution"));
                instructor.setDepartment(rs.getString("department"));
            }
        }
        
        return instructor;
    }
    
    private void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
    
    private void populateUserFromResultSet(User user, ResultSet rs) throws SQLException {
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setPassword(rs.getString("password"));
        user.setAccountType(rs.getString("account_type"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
    }
    
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Simple verification for demo - in production, use BCrypt
        return Integer.toString(plainPassword.hashCode()).equals(hashedPassword);
    }
    
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        // First verify old password
        String sql = "SELECT password FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (verifyPassword(oldPassword, storedPassword)) {
                    // Update with new password
                    String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                        updatePstmt.setString(1, hashPassword(newPassword));
                        updatePstmt.setInt(2, userId);
                        return updatePstmt.executeUpdate() > 0;
                    }
                }
            }
        }
        return false;
    }
    
    private String hashPassword(String password) {
        // Simple hash for demo - in production, use BCrypt or similar
        return Integer.toString(password.hashCode());
    }
}