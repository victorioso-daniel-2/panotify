package com.panotify.service;

import com.panotify.model.*;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Service class for managing exam-related operations.
 * Provides functionality for creating, updating, and managing exams,
 * handling questions, student answers, and grading.
 * This class interacts with the database to perform CRUD operations on exam data.
 */
public class ExamService {
    
    /**
     * Creates a new exam in the system.
     * 
     * @param exam The Exam object containing exam details
     * @return true if exam creation successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createExam(Exam exam) throws SQLException {
        String sql = "INSERT INTO exam (exam_title, course_id, instructor_id, deadline, duration_minutes, published) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exam.getTitle());
            stmt.setInt(2, exam.getCourseId());
            stmt.setInt(3, exam.getInstructorId());
            stmt.setTimestamp(4, exam.getDeadline() != null ? 
                Timestamp.valueOf(exam.getDeadline()) : null);
            stmt.setInt(5, exam.getDurationMinutes());
            stmt.setBoolean(6, exam.isPublished());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        exam.setExamId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Adds a new question to an exam.
     * 
     * @param question The Question object containing question details
     * @return true if question addition successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO question (exam_id, question_text, correct_answer, points) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, question.getExamId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getCorrectAnswer());
            stmt.setInt(4, question.getPoints());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        question.setQuestionId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Retrieves all exams for a specific course.
     * 
     * @param courseId The ID of the course
     * @return List of Exam objects associated with the course
     * @throws SQLException if a database error occurs
     */
    public List<Exam> getExamsByCourse(int courseId) throws SQLException {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam WHERE course_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exam exam = new Exam(
                        rs.getString("exam_title"),
                        rs.getInt("course_id"),
                        rs.getInt("instructor_id")
                    );
                    exam.setExamId(rs.getInt("exam_id"));
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setPublished(rs.getBoolean("published"));
                    
                    Timestamp deadline = rs.getTimestamp("deadline");
                    if (deadline != null) {
                        exam.setDeadline(deadline.toLocalDateTime());
                    }
                    exams.add(exam);
                }
            }
        }
        return exams;
    }
    
    /**
     * Retrieves all questions for a specific exam.
     * 
     * @param examId The ID of the exam
     * @return List of Question objects associated with the exam
     * @throws SQLException if a database error occurs
     */
    public List<Question> getQuestionsByExam(int examId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestionId(rs.getInt("question_id"));
                    question.setExamId(rs.getInt("exam_id"));
                    question.setQuestionText(rs.getString("question_text"));
                    question.setCorrectAnswer(rs.getString("correct_answer"));
                    question.setPoints(rs.getInt("points"));
                    questions.add(question);
                }
            }
        }
        return questions;
    }
    
    /**
     * Submits a student's answer to a question.
     * 
     * @param answer The StudentAnswer object containing the answer details
     * @return true if answer submission successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean submitAnswer(StudentAnswer answer) throws SQLException {
        String sql = "INSERT INTO student_answer (student_id, question_id, answer_text, is_correct, submitted_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, answer.getStudentId());
            stmt.setInt(2, answer.getQuestionId());
            stmt.setString(3, answer.getAnswerText());
            stmt.setBoolean(4, answer.isCorrect());
            stmt.setTimestamp(5, Timestamp.valueOf(answer.getSubmittedAt()));
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Grades an exam for a specific student.
     * Calculates total points earned and updates the report.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return true if grading successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean gradeExam(int studentId, int examId) throws SQLException {
        // Get all questions for the exam
        List<Question> questions = getQuestionsByExam(examId);
        int totalPoints = 0;
        int earnedPoints = 0;
        
        // For each question, check the student's answer
        for (Question question : questions) {
            totalPoints += question.getPoints();
            String sql = "SELECT answer_text FROM student_answer WHERE student_id = ? AND question_id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, question.getQuestionId());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String studentAnswer = rs.getString("answer_text");
                        if (studentAnswer.equals(question.getCorrectAnswer())) {
                            earnedPoints += question.getPoints();
                        }
                    }
                }
            }
        }
        
        // Update report
        String sql = "UPDATE report SET total_score = ?, max_score = ?, submitted_at = ?, status = 'completed' " +
                    "WHERE student_id = ? AND exam_id = ? AND status = 'in_progress'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, earnedPoints);
            stmt.setInt(2, totalPoints);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, studentId);
            stmt.setInt(5, examId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Checks if a student has attempted an exam.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return true if student has attempted the exam, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean hasAttemptedExam(int studentId, int examId) throws SQLException {
        String sql = "SELECT COUNT(*) as attempt_count FROM report WHERE student_id = ? AND exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("attempt_count") > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Retrieves a student's report for a specific exam.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return Report object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Report getStudentReport(int studentId, int examId) throws SQLException {
        String sql = "SELECT * FROM report WHERE student_id = ? AND exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Report report = new Report();
                    report.setReportId(rs.getInt("report_id"));
                    report.setStudentId(rs.getInt("student_id"));
                    report.setExamId(rs.getInt("exam_id"));
                    report.setTotalScore(rs.getInt("total_score"));
                    report.setMaxScore(rs.getInt("max_score"));
                    report.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                    return report;
                }
            }
        }
        return null;
    }
    
    /**
     * Gets the number of students who have attempted an exam.
     * 
     * @param examId The ID of the exam
     * @return Number of students who have attempted the exam
     * @throws SQLException if a database error occurs
     */
    public int getAttemptedCount(int examId) throws SQLException {
        String sql = "SELECT COUNT(*) as attempt_count FROM report WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("attempt_count");
                }
            }
        }
        return 0;
    }
    
    /**
     * Retrieves all answers submitted by a student for an exam.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return List of StudentAnswer objects
     * @throws SQLException if a database error occurs
     */
    public List<StudentAnswer> getStudentAnswers(int studentId, int examId) throws SQLException {
        List<StudentAnswer> answers = new ArrayList<>();
        String sql = "SELECT * FROM student_answer sa " +
                    "INNER JOIN question q ON sa.question_id = q.question_id " +
                    "WHERE sa.student_id = ? AND q.exam_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentAnswer answer = new StudentAnswer(
                        rs.getInt("student_id"),
                        rs.getInt("question_id"),
                        rs.getString("answer_text")
                    );
                    answer.setCorrect(rs.getBoolean("is_correct"));
                    answers.add(answer);
                }
            }
        }
        return answers;
    }
    
    /**
     * Retrieves an exam by its ID.
     * 
     * @param examId The ID of the exam
     * @return Exam object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Exam getExam(int examId) throws SQLException {
        String sql = "SELECT * FROM exam WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Exam exam = new Exam(
                        rs.getString("exam_title"),
                        rs.getInt("course_id"),
                        rs.getInt("instructor_id")
                    );
                    exam.setExamId(rs.getInt("exam_id"));
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setPublished(rs.getBoolean("published"));
                    
                    Timestamp deadline = rs.getTimestamp("deadline");
                    if (deadline != null) {
                        exam.setDeadline(deadline.toLocalDateTime());
                    }
                    return exam;
                }
            }
        }
        return null;
    }
    
    /**
     * Deletes an exam and all associated data.
     * 
     * @param examId The ID of the exam to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteExam(int examId) throws SQLException {
        // First delete related records in other tables
        String[] cleanupQueries = {
            "DELETE FROM student_answer WHERE question_id IN (SELECT question_id FROM question WHERE exam_id = ?)",
            "DELETE FROM report WHERE exam_id = ?",
            "DELETE FROM question WHERE exam_id = ?",
            "DELETE FROM exam WHERE exam_id = ?"
        };
        
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);
            
            for (String sql : cleanupQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, examId);
                    stmt.executeUpdate();
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Updates an existing exam's details.
     * 
     * @param exam The Exam object containing updated details
     * @return true if update successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateExam(Exam exam) throws SQLException {
        String sql = "UPDATE exam SET exam_title = ?, deadline = ?, duration_minutes = ?, published = ? " +
                    "WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, exam.getTitle());
            stmt.setTimestamp(2, exam.getDeadline() != null ? 
                Timestamp.valueOf(exam.getDeadline()) : null);
            stmt.setInt(3, exam.getDurationMinutes());
            stmt.setBoolean(4, exam.isPublished());
            stmt.setInt(5, exam.getExamId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Checks if the time limit for an exam has expired for a student.
     * 
     * @param examId The ID of the exam
     * @param studentId The ID of the student
     * @return true if exam time has expired, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean isExamTimeExpired(int examId, int studentId) throws SQLException {
        String sql = "SELECT e.*, r.started_at FROM exam e " +
                    "LEFT JOIN report r ON e.exam_id = r.exam_id AND r.student_id = ? " +
                    "WHERE e.exam_id = ?";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp startedAt = rs.getTimestamp("started_at");
                    int durationMinutes = rs.getInt("duration_minutes");
                    Timestamp deadline = rs.getTimestamp("deadline");
                    
                    if (startedAt == null) {
                        // Exam hasn't started yet, check only deadline
                        return deadline != null && deadline.before(new Timestamp(System.currentTimeMillis()));
                    }
                    
                    // Check both duration and deadline
                    LocalDateTime expiryTime = startedAt.toLocalDateTime().plusMinutes(durationMinutes);
                    boolean isDurationExpired = LocalDateTime.now().isAfter(expiryTime);
                    boolean isDeadlineExpired = deadline != null && 
                        LocalDateTime.now().isAfter(deadline.toLocalDateTime());
                    
                    return isDurationExpired || isDeadlineExpired;
                }
            }
        }
        return false;
    }
    
    /**
     * Automatically submits all expired exams.
     * This method should be called periodically to handle unsubmitted exams.
     * 
     * @throws SQLException if a database error occurs
     */
    public void autoSubmitExpiredExams() throws SQLException {
        String sql = "SELECT r.student_id, r.exam_id FROM report r " +
                    "INNER JOIN exam e ON r.exam_id = e.exam_id " +
                    "WHERE r.status = 'in_progress' AND " +
                    "((r.started_at IS NOT NULL AND DATEADD(MINUTE, e.duration_minutes, r.started_at) <= GETDATE()) OR " +
                    "(e.deadline IS NOT NULL AND e.deadline <= GETDATE()))";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    int examId = rs.getInt("exam_id");
                    // Grade and submit the exam
                    gradeExam(studentId, examId);
                }
            }
        }
    }
    
    /**
     * Retrieves all published exams for a specific course.
     * 
     * @param courseId The ID of the course
     * @return List of published Exam objects
     * @throws SQLException if a database error occurs
     */
    public List<Exam> getPublishedExamsByCourse(int courseId) throws SQLException {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam WHERE course_id = ? AND published = 1";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exam exam = new Exam(
                        rs.getString("exam_title"),
                        rs.getInt("course_id"),
                        rs.getInt("instructor_id")
                    );
                    exam.setExamId(rs.getInt("exam_id"));
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setPublished(true);
                    
                    Timestamp deadline = rs.getTimestamp("deadline");
                    if (deadline != null) {
                        exam.setDeadline(deadline.toLocalDateTime());
                    }
                    exams.add(exam);
                }
            }
        }
        return exams;
    }
    
    /**
     * Publishes an exam, making it available to students.
     * 
     * @param examId The ID of the exam to publish
     * @return true if publishing successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean publishExam(int examId) throws SQLException {
        return setExamPublishStatus(examId, true);
    }
    
    /**
     * Unpublishes an exam, making it unavailable to students.
     * 
     * @param examId The ID of the exam to unpublish
     * @return true if unpublishing successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean unpublishExam(int examId) throws SQLException {
        return setExamPublishStatus(examId, false);
    }
    
    /**
     * Sets the publish status of an exam.
     * 
     * @param examId The ID of the exam
     * @param published The publish status to set
     * @return true if status update successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    private boolean setExamPublishStatus(int examId, boolean published) throws SQLException {
        String sql = "UPDATE exam SET published = ? WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, published);
            stmt.setInt(2, examId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Starts an exam for a student.
     * Creates a new report entry with 'in_progress' status.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return true if exam start successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean startExam(int studentId, int examId) throws SQLException {
        String sql = "INSERT INTO report (student_id, exam_id, total_score, max_score, started_at, status) " +
                    "VALUES (?, ?, 0, 0, ?, 'in_progress')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Automatically submits an exam for a student.
     * Used when time expires or system needs to force-submit.
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @return true if auto-submission successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean autoSubmitExam(int studentId, int examId) throws SQLException {
        // First update the report status
        String updateSql = "UPDATE report SET status = 'timeout', submitted_at = ? " +
                         "WHERE student_id = ? AND exam_id = ? AND status = 'in_progress'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, studentId);
            stmt.setInt(3, examId);
            
            if (stmt.executeUpdate() > 0) {
                // Then grade the exam
                return gradeExam(studentId, examId);
            }
        }
        return false;
    }
    
    /**
     * Calculates the average score for an exam.
     * 
     * @param examId The ID of the exam
     * @return Average score of all submitted attempts
     * @throws SQLException if a database error occurs
     */
    public double getAverageScore(int examId) throws SQLException {
        String sql = "SELECT AVG(CAST(total_score AS FLOAT) / max_score * 100) as avg_score " +
                    "FROM report WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_score");
                }
            }
        }
        return 0.0;
    }
    
    /**
     * Retrieves all reports for a specific exam.
     * 
     * @param examId The ID of the exam
     * @return List of Report objects
     * @throws SQLException if a database error occurs
     */
    public List<Report> getExamReports(int examId) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT r.*, u.username as student_name " +
                    "FROM report r " +
                    "INNER JOIN [user] u ON r.student_id = u.user_id " +
                    "WHERE r.exam_id = ? " +
                    "ORDER BY r.submitted_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setReportId(rs.getInt("report_id"));
                    report.setStudentId(rs.getInt("student_id"));
                    report.setExamId(rs.getInt("exam_id"));
                    report.setTotalScore(rs.getInt("total_score"));
                    report.setMaxScore(rs.getInt("max_score"));
                    report.setStartedAt(rs.getTimestamp("started_at").toLocalDateTime());
                    report.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                    report.setStatus(rs.getString("status"));
                    report.setStudentName(rs.getString("student_name"));
                    reports.add(report);
                }
            }
        }
        return reports;
    }
    
    /**
     * Deletes all questions associated with an exam.
     * 
     * @param examId The ID of the exam
     * @return true if deletion successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteQuestions(int examId) throws SQLException {
        String sql = "DELETE FROM question WHERE exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Gets the total number of attempts for an exam.
     * 
     * @param examId The ID of the exam
     * @return Total number of attempts
     * @throws SQLException if a database error occurs
     */
    public int getAttemptCount(int examId) throws SQLException {
        return getAttemptedCount(examId);
    }
} 