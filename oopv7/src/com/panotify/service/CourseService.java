package com.panotify.service;

import com.panotify.model.Course;
import com.panotify.model.StudentProgress;
import com.panotify.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles course-related operations.
 * Provides methods for course management, student enrollment, and progress tracking.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class CourseService {
    /** Database connection for course operations */
    private Connection connection;
    
    /**
     * Default constructor that initializes the database connection.
     */
    public CourseService() {
        try {
            this.connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Creates a new course in the database.
     * 
     * @param course The course object to create
     * @return true if the course was created successfully, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean createCourse(Course course) throws SQLException {
        String sql = "INSERT INTO course (course_name, course_code, instructor_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseCode());
            stmt.setInt(3, course.getInstructorId());
            stmt.setTimestamp(4, Timestamp.valueOf(course.getCreatedAt()));
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all courses taught by a specific instructor.
     * 
     * @param instructorId The ID of the instructor
     * @return A list of courses taught by the instructor
     * @throws SQLException If a database error occurs
     */
    public List<Course> getCoursesByInstructor(int instructorId) throws SQLException {
        String sql = "SELECT * FROM course WHERE instructor_id = ?";
        List<Course> courses = new ArrayList<>();
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
     * Retrieves all courses a student is enrolled in.
     * 
     * @param studentId The ID of the student
     * @return A list of courses the student is enrolled in
     * @throws SQLException If a database error occurs
     */
    public List<Course> getCoursesByStudent(int studentId) throws SQLException {
        String sql = "SELECT c.* FROM course c " +
                    "JOIN user_course uc ON c.course_id = uc.course_id " +
                    "WHERE uc.user_id = ?";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
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
     * Enrolls a student in a course using the course code.
     * 
     * @param userId The ID of the student
     * @param courseCode The code of the course to enroll in
     * @return true if enrollment was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean enrollStudent(int userId, String courseCode) throws SQLException {
        Course course = getCourseByCode(courseCode);
        if (course != null) {
            String sql = "INSERT INTO user_course (user_id, course_id) VALUES (?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, course.getCourseId());
                return stmt.executeUpdate() > 0;
            }
        }
        return false;
    }

    /**
     * Retrieves a course by its code.
     * 
     * @param courseCode The code of the course to retrieve
     * @return The course if found, null otherwise
     * @throws SQLException If a database error occurs
     */
    public Course getCourseByCode(String courseCode) throws SQLException {
        String sql = "SELECT * FROM course WHERE course_code = ?";
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
     * 
     * @param courseId The ID of the course
     * @return A list of student progress objects
     * @throws SQLException If a database error occurs
     */
    public List<StudentProgress> getStudentProgress(int courseId) throws SQLException {
        String sql = "SELECT u.user_id, u.first_name, u.last_name, " +
                    "COUNT(DISTINCT er.exam_id) as exams_taken, " +
                    "COUNT(DISTINCT e.exam_id) as total_exams, " +
                    "AVG(er.total_score * 100.0 / er.max_score) as average_score " +
                    "FROM [user] u " +
                    "JOIN user_course uc ON u.user_id = uc.user_id " +
                    "LEFT JOIN report er ON u.user_id = er.student_id " +
                    "LEFT JOIN exam e ON e.course_id = uc.course_id " +
                    "WHERE uc.course_id = ? AND u.account_type = 'student' " +
                    "GROUP BY u.user_id, u.first_name, u.last_name";
        List<StudentProgress> progressList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                    int examsTaken = rs.getInt("exams_taken");
                    int totalExams = rs.getInt("total_exams");
                    double averageScore = rs.getDouble("average_score");
                    progressList.add(new StudentProgress(studentName, examsTaken, totalExams, averageScore));
                }
            }
        }
        return progressList;
    }

    /**
     * Gets the number of students enrolled in a course.
     * 
     * @param courseId The ID of the course
     * @return The number of enrolled students
     * @throws SQLException If a database error occurs
     */
    public int getEnrolledStudentCount(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_course WHERE course_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Retrieves a course by its ID.
     * 
     * @param courseId The ID of the course to retrieve
     * @return The course if found, null otherwise
     */
    public Course getCourseById(int courseId) {
        try {
            String query = "SELECT * FROM course WHERE course_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseCode(rs.getString("course_code"));
                course.setInstructorId(rs.getInt("instructor_id"));
                
                // Set default description or null since description column might not exist
                course.setDescription("");
                
                // Add created_at if it exists
                if (rs.getTimestamp("created_at") != null) {
                    course.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                
                return course;
            }
        } catch (SQLException e) {
            System.err.println("Error getting course by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets all courses that a student is enrolled in, including instructor information.
     * 
     * @param studentId The ID of the student
     * @return A list of courses the student is enrolled in
     */
    public List<Course> getEnrolledCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT c.*, u.first_name, u.last_name FROM course c " +
                          "JOIN user_course uc ON c.course_id = uc.course_id " +
                          "JOIN [user] u ON c.instructor_id = u.user_id " +
                          "WHERE uc.user_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseCode(rs.getString("course_code"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setInstructorName(rs.getString("first_name") + " " + rs.getString("last_name"));
                
                // Add created_at if it exists
                if (rs.getTimestamp("created_at") != null) {
                    course.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrolled courses: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * Enrolls a student in a course using the course code.
     * Alternative implementation that handles exceptions internally.
     * 
     * @param studentId The ID of the student
     * @param courseCode The code of the course to enroll in
     * @return true if enrollment was successful, false otherwise
     */
    public boolean enrollStudentInCourse(int studentId, String courseCode) {
        try {
            // First check if the course exists
            String courseQuery = "SELECT course_id FROM course WHERE course_code = ?";
            PreparedStatement courseStmt = connection.prepareStatement(courseQuery);
            courseStmt.setString(1, courseCode);
            
            ResultSet courseRs = courseStmt.executeQuery();
            if (!courseRs.next()) {
                System.err.println("Course not found: " + courseCode);
                return false;
            }
            
            int courseId = courseRs.getInt("course_id");
            
            // Check if student is already enrolled
            String checkQuery = "SELECT * FROM user_course WHERE user_id = ? AND course_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, studentId);
            checkStmt.setInt(2, courseId);
            
            ResultSet checkRs = checkStmt.executeQuery();
            if (checkRs.next()) {
                System.out.println("Student already enrolled in this course");
                return true; // Already enrolled is considered success
            }
            
            // Enroll the student
            String enrollQuery = "INSERT INTO user_course (user_id, course_id, enrollment_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement enrollStmt = connection.prepareStatement(enrollQuery);
            enrollStmt.setInt(1, studentId);
            enrollStmt.setInt(2, courseId);
            
            int result = enrollStmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error enrolling student in course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the enrollment date for a student in a specific course.
     * 
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @return The enrollment date, or null if not enrolled
     */
    public java.time.LocalDateTime getEnrollmentDate(int studentId, int courseId) {
        try {
            String query = "SELECT enrollment_date FROM user_course WHERE user_id = ? AND course_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getTimestamp("enrollment_date") != null) {
                return rs.getTimestamp("enrollment_date").toLocalDateTime();
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollment date: " + e.getMessage());
        }
        return null;
    }
} 