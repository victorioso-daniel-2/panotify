package com.panotify.service;

import com.panotify.model.Instructor;
import com.panotify.model.Student;
import com.panotify.model.User;
import com.panotify.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles user-related operations.
 * Provides methods for user authentication, registration, and management.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class UserService {
    /** Database connection for user operations */
    private Connection connection;
    
    /**
     * Default constructor that initializes the database connection.
     */
    public UserService() {
        try {
            this.connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user based on email and password.
     * 
     * @param email The email to authenticate
     * @param password The password to verify
     * @return The authenticated User object if successful, null otherwise
     * @throws SQLException If a database error occurs
     */
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM [user] WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setAccountType(rs.getString("account_type"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Registers a new user in the system.
     * 
     * @param user The user object to register
     * @return true if registration was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean register(User user) throws SQLException {
        // Simple validation
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO [user] (username, first_name, last_name, email, phone_number, password, account_type, institution, department) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getAccountType());
            stmt.setString(8, user.getInstitution());
            stmt.setString(9, user.getDepartment());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("SQL error during registration: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Registers a new student in the system.
     * 
     * @param student The student object to register
     * @return true if registration was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean registerStudent(Student student) throws SQLException {
        student.setAccountType("Student"); // Ensure correct account type
        return register(student);
    }

    /**
     * Registers a new instructor in the system.
     * 
     * @param instructor The instructor object to register
     * @return true if registration was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean registerInstructor(Instructor instructor) throws SQLException {
        instructor.setAccountType("Instructor"); // Ensure correct account type
        return register(instructor);
    }

    /**
     * Updates an existing user's information.
     * 
     * @param user The user object with updated information
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE [user] SET username = ?, first_name = ?, last_name = ?, email = ?, " +
                    "phone_number = ?, institution = ?, department = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getInstitution());
            stmt.setString(7, user.getDepartment());
            stmt.setInt(8, user.getUserId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Changes a user's password.
     * 
     * @param userId The ID of the user
     * @param newPassword The new password to set
     * @return true if the password was changed successfully, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE [user] SET password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Verifies if the provided password matches the one stored in the database.
     * 
     * @param userId The user ID to check
     * @param password The password to verify
     * @return true if the password matches, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean verifyPassword(int userId, String password) throws SQLException {
        String sql = "SELECT password FROM [user] WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                }
            }
        }
        return false;
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param userId The ID of the user to retrieve
     * @return The user if found, null otherwise
     * @throws SQLException If a database error occurs
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
                    user.setUsername(rs.getString("username"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setAccountType(rs.getString("account_type"));
                    user.setInstitution(rs.getString("institution"));
                    user.setDepartment(rs.getString("department"));
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
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
     * @return A list of users (students) enrolled in the course
     * @throws SQLException If a database error occurs
     */
    public List<User> getStudentsByCourseId(int courseId) throws SQLException {
        String sql = "SELECT u.* FROM [user] u " +
                    "JOIN course_enrollment ce ON u.user_id = ce.user_id " +
                    "WHERE ce.course_id = ? AND u.account_type = 'student'";
        List<User> students = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User student = new User();
                    student.setUserId(rs.getInt("user_id"));
                    student.setUsername(rs.getString("username"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    students.add(student);
                }
            }
        }
        return students;
    }

    /**
     * Retrieves an instructor by their ID.
     * 
     * @param instructorId The ID of the instructor
     * @return The instructor if found, null otherwise
     */
    public Instructor getInstructorById(int instructorId) {
        try {
            String query = "SELECT * FROM users WHERE user_id = ? AND account_type = 'instructor'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, instructorId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setUserId(rs.getInt("user_id"));
                instructor.setUsername(rs.getString("username"));
                instructor.setFirstName(rs.getString("first_name"));
                instructor.setLastName(rs.getString("last_name"));
                instructor.setEmail(rs.getString("email"));
                instructor.setPhoneNumber(rs.getString("phone_number"));
                instructor.setInstitution(rs.getString("institution"));
                instructor.setDepartment(rs.getString("department"));
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println("Error getting instructor by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all students enrolled in a specific course as Student objects.
     * 
     * @param courseId The ID of the course
     * @return A list of students enrolled in the course
     */
    public List<Student> getStudentsByCourse(int courseId) {
        List<Student> students = new ArrayList<>();
        try {
            String query = "SELECT u.* FROM [user] u " +
                          "JOIN user_course uc ON u.user_id = uc.user_id " +
                          "WHERE uc.course_id = ? AND u.account_type = 'student'";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setUserId(rs.getInt("user_id"));
                student.setUsername(rs.getString("username"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phone_number"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by course: " + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieves all instructors in the system.
     * 
     * @return A list of all instructors
     */
    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        try {
            String instructorQuery = "SELECT * FROM [user] WHERE account_type = 'instructor'";
            PreparedStatement statement = connection.prepareStatement(instructorQuery);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setUserId(rs.getInt("user_id"));
                instructor.setUsername(rs.getString("username"));
                instructor.setFirstName(rs.getString("first_name"));
                instructor.setLastName(rs.getString("last_name"));
                instructor.setEmail(rs.getString("email"));
                instructor.setPhoneNumber(rs.getString("phone_number"));
                instructor.setInstitution(rs.getString("institution"));
                instructor.setDepartment(rs.getString("department"));
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all instructors: " + e.getMessage());
        }
        return instructors;
    }

    /**
     * Retrieves all students in the system.
     * 
     * @return A list of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            String studentQuery = "SELECT * FROM [user] WHERE account_type = 'student'";
            PreparedStatement statement = connection.prepareStatement(studentQuery);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setUserId(rs.getInt("user_id"));
                student.setUsername(rs.getString("username"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phone_number"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieves the instructor for a specific course.
     * 
     * @param courseId The ID of the course
     * @return The instructor of the course if found, null otherwise
     */
    public Instructor getInstructorByCourse(int courseId) {
        try {
            String courseInstructorQuery = "SELECT u.* FROM [user] u " +
                          "JOIN course c ON u.user_id = c.instructor_id " +
                          "WHERE c.course_id = ? AND u.account_type = 'instructor'";
            
            PreparedStatement statement = connection.prepareStatement(courseInstructorQuery);
            statement.setInt(1, courseId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setUserId(rs.getInt("user_id"));
                instructor.setUsername(rs.getString("username"));
                instructor.setFirstName(rs.getString("first_name"));
                instructor.setLastName(rs.getString("last_name"));
                instructor.setEmail(rs.getString("email"));
                instructor.setPhoneNumber(rs.getString("phone_number"));
                instructor.setInstitution(rs.getString("institution"));
                instructor.setDepartment(rs.getString("department"));
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println("Error getting instructor by course: " + e.getMessage());
        }
        return null;
    }
} 