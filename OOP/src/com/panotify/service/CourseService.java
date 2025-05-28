package com.panotify.service;

import com.panotify.model.Course;
import com.panotify.model.StudentProgress;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing course-related operations.
 * Provides functionality for creating courses, managing enrollments,
 * and tracking student progress.
 * This class interacts with the database to perform CRUD operations on course data.
 */
public class CourseService {
    
    /**
     * Creates a new course in the system.
     * 
     * @param course The Course object containing course details
     * @return true if course creation successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createCourse(Course course) throws SQLException {
        String sql = "INSERT INTO [course] (course_name, course_code, instructor_id, created_at) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseCode());
            stmt.setInt(3, course.getInstructorId());
            stmt.setTimestamp(4, Timestamp.valueOf(course.getCreatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        course.setCourseId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Retrieves all courses taught by a specific instructor.
     * 
     * @param instructorId The ID of the instructor
     * @return List of Course objects associated with the instructor
     * @throws SQLException if a database error occurs
     */
    public List<Course> getCoursesByInstructor(int instructorId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM [course] WHERE instructor_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, instructorId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setInstructorId(rs.getInt("instructor_id"));
                    course.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    courses.add(course);
                }
            }
        }
        return courses;
    }
    
    /**
     * Retrieves all courses in which a student is enrolled.
     * Also includes the instructor's name for each course.
     * 
     * @param studentId The ID of the student
     * @return List of Course objects the student is enrolled in
     * @throws SQLException if a database error occurs
     */
    public List<Course> getCoursesByStudent(int studentId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, u.first_name + ' ' + u.last_name as instructor_name " +
                    "FROM course c " +
                    "INNER JOIN user_course uc ON c.course_id = uc.course_id " +
                    "INNER JOIN [user] u ON c.instructor_id = u.user_id " +
                    "WHERE uc.user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        rs.getInt("instructor_id")
                    );
                    course.setCourseId(rs.getInt("course_id"));
                    course.setInstructorName(rs.getString("instructor_name"));
                    courses.add(course);
                }
            }
        }
        return courses;
    }
    
    /**
     * Enrolls a student in a course using the course code.
     * 
     * @param userId The ID of the student to enroll
     * @param courseCode The unique code of the course
     * @return true if enrollment successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean enrollStudent(int userId, String courseCode) throws SQLException {
        String sql = "INSERT INTO [user_course] (user_id, course_id) " +
                    "SELECT ?, course_id FROM [course] WHERE course_code = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, courseCode);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Retrieves a course by its unique code.
     * 
     * @param courseCode The unique code of the course
     * @return Course object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Course getCourseByCode(String courseCode) throws SQLException {
        String sql = "SELECT * FROM [course] WHERE course_code = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setInstructorId(rs.getInt("instructor_id"));
                    course.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return course;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves progress information for all students in a course.
     * Includes number of exams taken, total exams, and average scores.
     * 
     * @param courseId The ID of the course
     * @return List of StudentProgress objects containing progress information
     * @throws SQLException if a database error occurs
     */
    public List<StudentProgress> getStudentProgress(int courseId) throws SQLException {
        List<StudentProgress> progressList = new ArrayList<>();
        String sql = "SELECT u.username as student_name, " +
                    "COUNT(DISTINCT r.exam_id) as exams_taken, " +
                    "COUNT(DISTINCT e.exam_id) as total_exams, " +
                    "AVG(CAST(r.total_score AS FLOAT) / r.max_score * 100) as avg_score " +
                    "FROM [user] u " +
                    "INNER JOIN [user_course] uc ON u.user_id = uc.user_id " +
                    "LEFT JOIN exam e ON e.course_id = uc.course_id " +
                    "LEFT JOIN report r ON r.exam_id = e.exam_id AND r.student_id = u.user_id " +
                    "WHERE uc.course_id = ? AND u.account_type = 'Student' " +
                    "GROUP BY u.user_id, u.username";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    int examsTaken = rs.getInt("exams_taken");
                    int totalExams = rs.getInt("total_exams");
                    double avgScore = rs.getDouble("avg_score");
                    
                    StudentProgress progress = new StudentProgress(
                        studentName,
                        examsTaken,
                        totalExams,
                        avgScore
                    );
                    progressList.add(progress);
                }
            }
        }
        return progressList;
    }

    /**
     * Gets the number of students enrolled in a course.
     * 
     * @param courseId The ID of the course
     * @return Number of enrolled students
     * @throws SQLException if a database error occurs
     */
    public int getEnrolledStudentCount(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) as student_count " +
                    "FROM [user_course] uc " +
                    "INNER JOIN [user] u ON u.user_id = uc.user_id " +
                    "WHERE uc.course_id = ? AND u.account_type = 'Student'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("student_count");
                }
            }
        }
        return 0;
    }
} 