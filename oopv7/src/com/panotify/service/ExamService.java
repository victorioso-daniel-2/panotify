package com.panotify.service;

import com.panotify.model.*;
import com.panotify.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for managing exams and related operations
 * Provides methods for creating, retrieving, updating, and deleting exams
 * as well as handling student exam submissions and grading
 */
public class ExamService {
    
    private Connection connection;
    
    /**
     * Creates a new ExamService instance and establishes a database connection
     */
    public ExamService() {
        try {
            this.connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new exam in the database
     * 
     * @param exam the exam object to create
     * @return true if creation was successful, false otherwise
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
     * Adds a question to an exam
     * 
     * @param question the question to add
     * @return the ID of the newly created question, or -1 if creation failed
     * @throws SQLException if a database error occurs
     */
    public int addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO question (exam_id, question_text, options, correct_option, points) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, question.getExamId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getOptions());
            stmt.setInt(4, question.getCorrectOption());
            stmt.setInt(5, question.getPoints());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int questionId = rs.getInt(1);
                        question.setQuestionId(questionId);
                        return questionId;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Retrieves all exams for a specific course
     * 
     * @param courseId the ID of the course to get exams for
     * @return a list of exams for the course
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
     * Retrieves all questions for a specific exam
     * 
     * @param examId the ID of the exam to get questions for
     * @return a list of questions for the exam
     * @throws SQLException if a database error occurs
     */
    public List<Question> getExamQuestions(int examId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE exam_id = ? ORDER BY question_id";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestionId(rs.getInt("question_id"));
                    question.setExamId(rs.getInt("exam_id"));
                    question.setQuestionText(rs.getString("question_text"));
                    question.setOptions(rs.getString("options"));
                    question.setCorrectOption(rs.getInt("correct_option"));
                    question.setPoints(rs.getInt("points"));
                    questions.add(question);
                }
            }
        }
        return questions;
    }
    
    /**
     * Submits a student's answer to a question
     * 
     * @param answer the student answer to submit
     * @return true if submission was successful, false otherwise
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
     * Grades an exam submission for a student
     * Calculates the score based on correct answers and updates the report
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if grading was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean gradeExam(int studentId, int examId) throws SQLException {
        // Get all questions for the exam
        List<Question> questions = getExamQuestions(examId);
        int totalPoints = 0;
        int earnedPoints = 0;
        
        // For each question, check the student's answer
        for (Question question : questions) {
            totalPoints += question.getPoints();
            String sql = "SELECT answer_text, is_correct FROM student_answer WHERE student_id = ? AND question_id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, question.getQuestionId());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String studentAnswer = rs.getString("answer_text");
                        boolean isCorrect = false;
                        
                        // For multiple choice questions
                        if (question.getOptions() != null && question.getOptions().contains("|")) {
                            try {
                                int selectedOption = Integer.parseInt(studentAnswer);
                                isCorrect = (selectedOption == question.getCorrectOption());
                            } catch (NumberFormatException e) {
                                // Invalid option format
                                isCorrect = false;
                            }
                        } else {
                            // For identification questions - compare case-insensitively
                            String correctAnswer = question.getCorrectAnswer().trim();
                            isCorrect = studentAnswer.trim().equalsIgnoreCase(correctAnswer);
                        }
                        
                        // Update is_correct flag if needed
                        if (isCorrect != rs.getBoolean("is_correct")) {
                            updateAnswerCorrectness(studentId, question.getQuestionId(), isCorrect);
                        }
                        
                        if (isCorrect) {
                            earnedPoints += question.getPoints();
                        }
                    }
                }
            }
        }
        
        // Create or update exam result
        return createExamResult(studentId, examId, earnedPoints, totalPoints);
    }
    
    /**
     * Checks if a student has attempted an exam
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the student has attempted the exam, false otherwise
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
     * Gets a student's report for a specific exam
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return the student's report or null if not found
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
     * Gets the number of students who have attempted an exam
     * 
     * @param examId the ID of the exam
     * @return the number of students who have attempted the exam
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
     * Gets a student's answers for a specific exam
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return a list of the student's answers
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
     * Gets an exam by its ID
     * 
     * @param examId the ID of the exam
     * @return the exam or null if not found
     */
    public Exam getExamById(int examId) {
        try {
            String query = "SELECT * FROM exam WHERE exam_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, examId);
            
            ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                Exam exam = new Exam();
                    exam.setExamId(rs.getInt("exam_id"));
                exam.setTitle(rs.getString("exam_title"));
                exam.setCourseId(rs.getInt("course_id"));
                
                Timestamp deadline = rs.getTimestamp("deadline");
                if (deadline != null) {
                    exam.setDeadline(deadline.toLocalDateTime());
                }
                
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setPublished(rs.getBoolean("published"));
                return exam;
            }
        } catch (SQLException e) {
            System.err.println("Error getting exam by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Gets all exams for a student from all enrolled courses
     * 
     * @param studentId the ID of the student
     * @return a list of exams
     */
    public List<Exam> getExamsForStudent(int studentId) {
        List<Exam> exams = new ArrayList<>();
        try {
            String query = "SELECT e.* FROM exam e " +
                          "JOIN course c ON e.course_id = c.course_id " +
                          "JOIN user_course uc ON c.course_id = uc.course_id " +
                          "WHERE uc.user_id = ? AND e.published = 1";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Exam exam = new Exam();
                exam.setExamId(rs.getInt("exam_id"));
                exam.setTitle(rs.getString("exam_title"));
                exam.setCourseId(rs.getInt("course_id"));
                exam.setInstructorId(rs.getInt("instructor_id"));
                    
                    Timestamp deadline = rs.getTimestamp("deadline");
                    if (deadline != null) {
                        exam.setDeadline(deadline.toLocalDateTime());
                    }
                
                exam.setDurationMinutes(rs.getInt("duration_minutes"));
                exam.setPublished(rs.getBoolean("published"));
                exams.add(exam);
            }
        } catch (SQLException e) {
            System.err.println("Error getting exams for student: " + e.getMessage());
        }
        return exams;
    }
    
    /**
     * Checks if a student has completed an exam
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the student has completed the exam, false otherwise
     */
    public boolean hasStudentCompletedExam(int studentId, int examId) {
        try {
            String query = "SELECT status FROM report WHERE student_id = ? AND exam_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, examId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                return "completed".equals(status);
            }
        } catch (SQLException e) {
            System.err.println("Error checking if student completed exam: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Gets the results for a student for a specific course
     * 
     * @param studentId the ID of the student
     * @param courseId the ID of the course
     * @return a list of exam results
     */
    public List<ExamResult> getResultsForStudentByCourse(int studentId, int courseId) {
        List<ExamResult> results = new ArrayList<>();
        try {
            String query = "SELECT r.*, e.exam_title FROM report r " +
                          "JOIN exam e ON r.exam_id = e.exam_id " +
                          "WHERE r.student_id = ? AND e.course_id = ? AND r.status = 'completed'";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultId(rs.getInt("report_id"));
                result.setExamId(rs.getInt("exam_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setExamTitle(rs.getString("exam_title"));
                result.setTotalScore(rs.getInt("total_score"));
                result.setMaxScore(rs.getInt("max_score"));
                
                Timestamp submittedAt = rs.getTimestamp("submitted_at");
                if (submittedAt != null) {
                    result.setSubmittedAt(submittedAt.toLocalDateTime());
                }
                
                result.setCompleted(true);
                result.setStatus("completed");
                
                results.add(result);
            }
        } catch (SQLException e) {
            System.err.println("Error getting results for student by course: " + e.getMessage());
        }
        return results;
    }

    /**
     * Deletes an exam and all related records (questions, student answers, reports)
     * 
     * @param examId the ID of the exam to delete
     * @return true if deletion was successful, false otherwise
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
     * Updates an existing exam's details
     * 
     * @param exam the exam object with updated values
     * @return true if update was successful, false otherwise
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
     * Checks if an exam's time has expired for a specific student
     * 
     * @param examId the ID of the exam
     * @param studentId the ID of the student
     * @return true if the exam time has expired, false otherwise
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
     * Auto-submits expired exams for all students
     * Identifies exams that have exceeded their duration or passed deadline
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
     * Gets all published exams for a specific course
     * 
     * @param courseId the ID of the course
     * @return a list of published exams for the course
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
     * Publishes an exam, making it available to students
     * 
     * @param examId the ID of the exam to publish
     * @return true if publishing was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean publishExam(int examId) throws SQLException {
        return setExamPublishStatus(examId, true);
    }

    /**
     * Unpublishes an exam, making it unavailable to students
     * 
     * @param examId the ID of the exam to unpublish
     * @return true if unpublishing was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean unpublishExam(int examId) throws SQLException {
        return setExamPublishStatus(examId, false);
    }

    /**
     * Sets the publish status of an exam
     * 
     * @param examId the ID of the exam
     * @param published the publish status to set
     * @return true if the operation was successful, false otherwise
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
     * Starts an exam for a student
     * Creates an initial report entry with "in_progress" status
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the operation was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean startExam(int studentId, int examId) throws SQLException {
        String sql = "INSERT INTO report (student_id, exam_id, total_score, max_score, started_at, submitted_at, status) " +
                    "VALUES (?, ?, 0, 0, ?, ?, 'in_progress')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(3, now);
            stmt.setTimestamp(4, now); // Set submitted_at to the same time as started_at initially
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Auto-submits an exam when time expires
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the operation was successful, false otherwise
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
     * Gets the average score for an exam
     * 
     * @param examId the ID of the exam
     * @return the average score as a percentage
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
     * Gets all reports for an exam
     * 
     * @param examId the ID of the exam
     * @return a list of reports for the exam
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
     * Deletes all questions for an exam
     * 
     * @param examId the ID of the exam
     * @return true if the operation was successful, false otherwise
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
     * Gets the number of attempts for an exam
     * 
     * @param examId the ID of the exam
     * @return the number of attempts
     * @throws SQLException if a database error occurs
     */
    public int getAttemptCount(int examId) throws SQLException {
        return getAttemptedCount(examId);
    }

    /**
     * Gets all exam results for a student
     * 
     * @param studentId the ID of the student
     * @return a list of exam results
     */
    public List<ExamResult> getStudentResults(int studentId) {
        List<ExamResult> results = new ArrayList<>();
        try {
            String query = "SELECT er.*, e.exam_title FROM report er " +
                          "JOIN exam e ON er.exam_id = e.exam_id " +
                          "WHERE er.student_id = ? AND er.status = 'completed'";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultId(rs.getInt("report_id"));
                result.setExamId(rs.getInt("exam_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setExamTitle(rs.getString("exam_title"));
                result.setTotalScore(rs.getInt("total_score"));
                result.setMaxScore(rs.getInt("max_score"));
                
                Timestamp submittedAt = rs.getTimestamp("submitted_at");
                if (submittedAt != null) {
                    result.setSubmittedAt(submittedAt.toLocalDateTime());
                }
                
                result.setCompleted(true);
                result.setStatus("completed");
                
                results.add(result);
            }
        } catch (SQLException e) {
            System.err.println("Error getting student results: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }
    
    /**
     * Gets detailed question results for a student's exam attempt
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return a list of question results
     */
    public List<QuestionResult> getQuestionResults(int studentId, int examId) {
        List<QuestionResult> results = new ArrayList<>();
        try {
            // Get all questions for this exam
            List<Question> questions = getExamQuestions(examId);
            
            // Get the student answers for these questions
            String query = "SELECT sa.question_id, sa.answer_text, sa.is_correct FROM student_answer sa " +
                           "JOIN question q ON sa.question_id = q.question_id " +
                           "WHERE sa.student_id = ? AND q.exam_id = ?";
            
            Map<Integer, StudentAnswer> answerMap = new HashMap<>();
            
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, examId);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int questionId = rs.getInt("question_id");
                    String answerText = rs.getString("answer_text");
                    boolean isCorrect = rs.getBoolean("is_correct");
                    
                    StudentAnswer answer = new StudentAnswer(studentId, questionId, answerText);
                    answer.setCorrect(isCorrect);
                    answerMap.put(questionId, answer);
                }
            }
            
            // Create QuestionResult objects
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                StudentAnswer answer = answerMap.get(question.getQuestionId());
                
                // Skip questions that weren't answered
                if (answer == null) continue;
                
                String correctAnswer;
                if (question.getOptions() != null && !question.getOptions().isEmpty()) {
                    // For multiple choice, get the text of the correct option
                    String[] options = question.getOptions().split("\\|");
                    if (question.getCorrectOption() >= 0 && question.getCorrectOption() < options.length) {
                        correctAnswer = options[question.getCorrectOption()];
                    } else {
                        correctAnswer = "";
                    }
                } else {
                    // For identification questions
                    correctAnswer = question.getCorrectAnswer();
                }
                
                QuestionResult result = new QuestionResult(
                    0, // No question_result_id since we're not using that table
                    0, // No result_id
                    question.getQuestionId(),
                    i + 1, // Question number (1-based)
                    question.getQuestionText(),
                    answer.getAnswerText(),
                    correctAnswer,
                    answer.isCorrect()
                );
                
                results.add(result);
            }
        } catch (SQLException e) {
            System.err.println("Error getting question results: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Submits an exam for a student
     * Marks the exam as completed in the database
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the submission was successful, false otherwise
     */
    public boolean submitExam(int studentId, int examId) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // First check if there's an existing report entry
            String checkQuery = "SELECT report_id, status FROM report WHERE student_id = ? AND exam_id = ?";
            
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, studentId);
                checkStatement.setInt(2, examId);
                
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        // A report already exists, update it
                        String status = rs.getString("status");
                        
                        // If the exam is already completed, don't update
                        if ("completed".equals(status)) {
                            return true;
                        }
                        
                        // Update the existing report
                        String updateQuery = "UPDATE report SET status = 'completed', submitted_at = ? WHERE student_id = ? AND exam_id = ?";
                        
                        try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                            updateStatement.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                            updateStatement.setInt(2, studentId);
                            updateStatement.setInt(3, examId);
                            
                            return updateStatement.executeUpdate() > 0;
                        }
                    } else {
                        // No report exists, create a new one
                        String insertQuery = "INSERT INTO report (student_id, exam_id, total_score, max_score, started_at, submitted_at, status) " +
                                           "VALUES (?, ?, 0, 0, ?, ?, 'completed')";
                        
                        try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, studentId);
                            insertStatement.setInt(2, examId);
                            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                            insertStatement.setTimestamp(3, now);
                            insertStatement.setTimestamp(4, now);
                            
                            return insertStatement.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error submitting exam: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Checks if a student has submitted an exam (whether completed or in progress)
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return true if the student has submitted the exam, false otherwise
     */
    public boolean hasStudentSubmittedExam(int studentId, int examId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT status FROM report WHERE student_id = ? AND exam_id = ?")) {
            statement.setInt(1, studentId);
            statement.setInt(2, examId);
            
            try (ResultSet rs = statement.executeQuery()) {
                // If any record exists, the student has submitted the exam
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking if student submitted exam: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Gets the submission status of an exam for a student
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return the status of the exam (in_progress, completed, timeout) or null if not submitted
     */
    public String getExamSubmissionStatus(int studentId, int examId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT status FROM report WHERE student_id = ? AND exam_id = ?")) {
            statement.setInt(1, studentId);
            statement.setInt(2, examId);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting exam submission status: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing question
     * 
     * @param question the question object with updated values
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE question SET question_text = ?, options = ?, correct_option = ?, points = ? WHERE question_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getOptions());
            stmt.setInt(3, question.getCorrectOption());
            stmt.setInt(4, question.getPoints());
            stmt.setInt(5, question.getQuestionId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a question
     * 
     * @param questionId the ID of the question to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM question WHERE question_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Gets an exam result for a specific student and exam
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @return the exam result or null if not found
     */
    public ExamResult getExamResult(int studentId, int examId) {
        try {
            String query = "SELECT r.*, e.exam_title FROM report r " +
                           "JOIN exam e ON r.exam_id = e.exam_id " +
                           "WHERE r.student_id = ? AND r.exam_id = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            statement.setInt(2, examId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ExamResult result = new ExamResult();
                result.setResultId(rs.getInt("report_id"));
                result.setExamId(examId);
                result.setStudentId(studentId);
                result.setExamTitle(rs.getString("exam_title"));
                result.setTotalScore(rs.getInt("total_score"));
                result.setMaxScore(rs.getInt("max_score"));
                
                Timestamp submittedAt = rs.getTimestamp("submitted_at");
                if (submittedAt != null) {
                    result.setSubmittedAt(submittedAt.toLocalDateTime());
                }
                
                String status = rs.getString("status");
                result.setCompleted("completed".equals(status));
                result.setStatus(status);
                
                return result;
            }
        } catch (SQLException e) {
            System.err.println("Error getting exam result: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if any student has submitted answers for an exam
     * 
     * @param examId the ID of the exam
     * @return true if any student has submitted answers for the exam, false otherwise
     */
    public boolean hasAnySubmissions(int examId) {
        try {
            String query = "SELECT COUNT(*) as submission_count FROM report WHERE exam_id = ? AND status = 'completed'";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, examId);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("submission_count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking for exam submissions: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates the is_correct flag for a student answer
     * 
     * @param studentId the ID of the student
     * @param questionId the ID of the question
     * @param isCorrect whether the answer is correct
     * @throws SQLException if a database error occurs
     */
    private void updateAnswerCorrectness(int studentId, int questionId, boolean isCorrect) throws SQLException {
        String sql = "UPDATE student_answer SET is_correct = ? WHERE student_id = ? AND question_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isCorrect);
            stmt.setInt(2, studentId);
            stmt.setInt(3, questionId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Creates or updates an exam result
     * 
     * @param studentId the ID of the student
     * @param examId the ID of the exam
     * @param earnedPoints the points earned by the student
     * @param totalPoints the total possible points for the exam
     * @return true if the operation was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    private boolean createExamResult(int studentId, int examId, int earnedPoints, int totalPoints) throws SQLException {
        // First check if a report already exists
        String checkSql = "SELECT report_id FROM report WHERE student_id = ? AND exam_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, studentId);
            checkStmt.setInt(2, examId);
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Update existing report
                    String sql = "UPDATE report SET total_score = ?, max_score = ?, submitted_at = ?, status = 'completed' " +
                                "WHERE student_id = ? AND exam_id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, earnedPoints);
                        stmt.setInt(2, totalPoints);
                        stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        stmt.setInt(4, studentId);
                        stmt.setInt(5, examId);
                        int updated = stmt.executeUpdate();
                        return updated > 0;
                    }
                } else {
                    // Create new report
                    String sql = "INSERT INTO report (student_id, exam_id, total_score, max_score, status, submitted_at) " +
                                "VALUES (?, ?, ?, ?, 'completed', ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, studentId);
                        stmt.setInt(2, examId);
                        stmt.setInt(3, earnedPoints);
                        stmt.setInt(4, totalPoints);
                        stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                        int inserted = stmt.executeUpdate();
                        return inserted > 0;
                    }
                }
            }
        }
    }
} 