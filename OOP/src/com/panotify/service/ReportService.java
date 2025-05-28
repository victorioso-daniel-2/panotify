package com.panotify.service;

import com.panotify.model.ExamResult;
import com.panotify.model.StudentReport;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for generating and managing academic reports.
 * Provides functionality for retrieving student performance reports
 * at both individual and course levels.
 * This class interacts with the database to aggregate exam results and calculate statistics.
 */
public class ReportService {
    
    /**
     * Retrieves a comprehensive report for a specific student in a course.
     * Includes student details, course information, exam statistics, and individual exam results.
     * 
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @return StudentReport object containing the comprehensive report, null if not found
     * @throws SQLException if a database error occurs
     */
    public StudentReport getStudentReport(int studentId, int courseId) throws SQLException {
        String sql = "SELECT u.first_name || ' ' || u.last_name as student_name, " +
                    "u.email as student_email, " +
                    "c.course_name, " +
                    "COUNT(DISTINCT e.exam_id) as exams_taken, " +
                    "SUM(CASE WHEN sa.is_correct = 1 THEN q.points ELSE 0 END) as total_score, " +
                    "SUM(q.points) as max_score " +
                    "FROM [user] u " +
                    "JOIN user_course uc ON u.user_id = uc.user_id " +
                    "JOIN course c ON uc.course_id = c.course_id " +
                    "LEFT JOIN exam e ON c.course_id = e.course_id " +
                    "LEFT JOIN question q ON e.exam_id = q.exam_id " +
                    "LEFT JOIN student_answer sa ON q.question_id = sa.question_id AND sa.user_id = u.user_id " +
                    "WHERE u.user_id = ? AND c.course_id = ? " +
                    "GROUP BY u.user_id, u.first_name, u.last_name, u.email, c.course_id, c.course_name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StudentReport report = new StudentReport();
                    report.setStudentId(studentId);
                    report.setStudentName(rs.getString("student_name"));
                    report.setStudentEmail(rs.getString("student_email"));
                    report.setCourseId(courseId);
                    report.setCourseName(rs.getString("course_name"));
                    report.setExamsTaken(rs.getInt("exams_taken"));
                    report.setTotalScore(rs.getInt("total_score"));
                    report.setMaxScore(rs.getInt("max_score"));
                    
                    if (report.getMaxScore() > 0) {
                        report.setAveragePercentage((double) report.getTotalScore() / report.getMaxScore() * 100);
                    }
                    
                    // Get exam results for this student in this course
                    report.setExamResults(getExamResults(studentId, courseId));
                    
                    return report;
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves reports for all students enrolled in a course.
     * Includes overall performance statistics for each student.
     * 
     * @param courseId The ID of the course
     * @return List of StudentReport objects for all enrolled students
     * @throws SQLException if a database error occurs
     */
    public List<StudentReport> getCourseReport(int courseId) throws SQLException {
        String sql = "SELECT u.user_id, u.first_name || ' ' || u.last_name as student_name, " +
                    "u.email as student_email, " +
                    "COUNT(DISTINCT e.exam_id) as exams_taken, " +
                    "SUM(CASE WHEN sa.is_correct = 1 THEN q.points ELSE 0 END) as total_score, " +
                    "SUM(q.points) as max_score " +
                    "FROM [user] u " +
                    "JOIN user_course uc ON u.user_id = uc.user_id " +
                    "LEFT JOIN exam e ON uc.course_id = e.course_id " +
                    "LEFT JOIN question q ON e.exam_id = q.exam_id " +
                    "LEFT JOIN student_answer sa ON q.question_id = sa.question_id AND sa.user_id = u.user_id " +
                    "WHERE uc.course_id = ? AND u.account_type = 'student' " +
                    "GROUP BY u.user_id, u.first_name, u.last_name, u.email";
        
        List<StudentReport> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentReport report = new StudentReport();
                    report.setStudentId(rs.getInt("user_id"));
                    report.setStudentName(rs.getString("student_name"));
                    report.setStudentEmail(rs.getString("student_email"));
                    report.setCourseId(courseId);
                    report.setExamsTaken(rs.getInt("exams_taken"));
                    report.setTotalScore(rs.getInt("total_score"));
                    report.setMaxScore(rs.getInt("max_score"));
                    
                    if (report.getMaxScore() > 0) {
                        report.setAveragePercentage((double) report.getTotalScore() / report.getMaxScore() * 100);
                    }
                    
                    reports.add(report);
                }
            }
        }
        return reports;
    }
    
    /**
     * Retrieves detailed exam results for a specific student in a course.
     * Calculates scores and percentages for each exam attempted by the student.
     * 
     * @param studentId The ID of the student
     * @param courseId The ID of the course
     * @return List of ExamResult objects containing individual exam performance
     * @throws SQLException if a database error occurs
     */
    private List<ExamResult> getExamResults(int studentId, int courseId) throws SQLException {
        String sql = "SELECT e.exam_id, e.exam_title, " +
                    "SUM(CASE WHEN sa.is_correct = 1 THEN q.points ELSE 0 END) as score, " +
                    "SUM(q.points) as max_score " +
                    "FROM exam e " +
                    "JOIN question q ON e.exam_id = q.exam_id " +
                    "LEFT JOIN student_answer sa ON q.question_id = sa.question_id AND sa.user_id = ? " +
                    "WHERE e.course_id = ? " +
                    "GROUP BY e.exam_id, e.exam_title";
        
        List<ExamResult> results = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExamResult result = new ExamResult(rs.getInt("exam_id"), studentId);
                    result.setExamTitle(rs.getString("exam_title"));
                    result.setTotalScore(rs.getInt("score"));
                    result.setMaxScore(rs.getInt("max_score"));
                    
                    if (result.getMaxScore() > 0) {
                        result.setPercentage((double) result.getTotalScore() / result.getMaxScore() * 100);
                    }
                    
                    results.add(result);
                }
            }
        }
        return results;
    }
} 