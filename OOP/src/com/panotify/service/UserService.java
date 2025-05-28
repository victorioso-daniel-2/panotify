package com.panotify.service;

import com.panotify.model.User;
import com.panotify.model.Student;
import com.panotify.model.Instructor;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling user-related operations.
 * Provides functionality for user authentication, registration, and profile management.
 * This class interacts with the database to perform CRUD operations on user data.
 */
public class UserService {
    
    /**
     * Authenticates a user with the provided email and password.
     * 
     * @param email The user's email address
     * @param password The user's password (should be hashed in production)
     * @return User object if authentication successful, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM [user] WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password); // In production, use password hashing
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
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
     * Registers a new user in the system.
     * 
     * @param user The User object containing registration details
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean register(User user) throws SQLException {
        String sql = "INSERT INTO [user] (first_name, last_name, username, email, phone_number, password, account_type, institution, department) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getPassword()); // In production, use password hashing
            stmt.setString(7, user.getAccountType());
            stmt.setString(8, user.getInstitution());
            stmt.setString(9, user.getDepartment());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Registers a new student in the system.
     * 
     * @param student The Student object containing registration details
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerStudent(Student student) throws SQLException {
        return register(student);
    }
    
    /**
     * Registers a new instructor in the system.
     * 
     * @param instructor The Instructor object containing registration details
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerInstructor(Instructor instructor) throws SQLException {
        return register(instructor);
    }
    
    /**
     * Updates an existing user's profile information.
     * 
     * @param user The User object containing updated information
     * @return true if update successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE [user] SET first_name = ?, last_name = ?, email = ?, " +
                    "institution = ?, department = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getInstitution());
            stmt.setString(5, user.getDepartment());
            stmt.setInt(6, user.getUserId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Changes a user's password.
     * 
     * @param userId The ID of the user
     * @param newPassword The new password (should be hashed in production)
     * @return true if password change successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword); // In production, use password hashing
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Retrieves a user by their ID.
     * 
     * @param userId The ID of the user to retrieve
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM [user] WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
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
     * Retrieves all students enrolled in a specific course.
     * 
     * @param courseId The ID of the course
     * @return List of User objects representing enrolled students
     * @throws SQLException if a database error occurs
     */
    public List<User> getStudentsByCourseId(int courseId) throws SQLException {
        String sql = "SELECT u.* FROM [user] u " +
                    "JOIN user_course uc ON u.user_id = uc.user_id " +
                    "WHERE uc.course_id = ? AND u.account_type = 'student'";
        
        List<User> students = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User student = new User();
                    student.setUserId(rs.getInt("user_id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    student.setAccountType("student");
                    students.add(student);
                }
            }
        }
        return students;
    }
} 