package com.panotify.service;

import com.panotify.model.*;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class UserService {
    
    public boolean registerStudent(Student student) throws SQLException {
        String sql = "INSERT INTO users (full_name, username, email, phone_number, password, account_type, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getUsername());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhoneNumber());
            pstmt.setString(5, hashPassword(student.getPassword()));
            pstmt.setString(6, "Student");
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setUserId(generatedKeys.getInt(1));
                        student.setStudentId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean registerInstructor(Instructor instructor) throws SQLException {
        String sql = "INSERT INTO users (full_name, username, email, phone_number, password, account_type, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, instructor.getFullName());
            pstmt.setString(2, instructor.getUsername());
            pstmt.setString(3, instructor.getEmail());
            pstmt.setString(4, instructor.getPhoneNumber());
            pstmt.setString(5, hashPassword(instructor.getPassword()));
            pstmt.setString(6, "Instructor");
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        instructor.setUserId(generatedKeys.getInt(1));
                        instructor.setInstructorId(generatedKeys.getInt(1));
                        
                        // Insert instructor-specific data
                        insertInstructorDetails(instructor);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void insertInstructorDetails(Instructor instructor) throws SQLException {
        String sql = "INSERT INTO instructors (instructor_id, institution, department) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructor.getInstructorId());
            pstmt.setString(2, instructor.getInstitution());
            pstmt.setString(3, instructor.getDepartment());
            pstmt.executeUpdate();
        }
    }
    
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ? AND account_type = 'Student'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student();
                populateUserFromResultSet(student, rs);
                student.setStudentId(rs.getInt("user_id"));
                return student;
            }
        }
        return null;
    }
    
    public Instructor getInstructorById(int instructorId) throws SQLException {
        String sql = "SELECT u.*, i.institution, i.department FROM users u " +
                    "LEFT JOIN instructors i ON u.user_id = i.instructor_id " +
                    "WHERE u.user_id = ? AND u.account_type = 'Instructor'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructorId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Instructor instructor = new Instructor();
                populateUserFromResultSet(instructor, rs);
                instructor.setInstructorId(rs.getInt("user_id"));
                instructor.setInstitution(rs.getString("institution"));
                instructor.setDepartment(rs.getString("department"));
                return instructor;
            }
        }
        return null;
    }
    
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone_number = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setInt(4, user.getUserId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
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
    
    private String hashPassword(String password) {
        // Simple hash for demo - in production, use BCrypt or similar
        return Integer.toString(password.hashCode());
    }
}