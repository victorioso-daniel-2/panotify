package com.panotify.model;

import java.util.List;

/**
 * Represents a comprehensive report of a student's performance in a course.
 * Contains scores, exam statistics, and detailed exam results.
 */
public class StudentReport {
    private int studentId;
    private String studentName;
    private String studentEmail;
    private int courseId;
    private String courseName;
    private int totalScore;
    private int maxScore;
    private double averagePercentage;
    private int examsTaken;
    private int examsAvailable;
    private List<ExamResult> examResults;
    
    /**
     * Default constructor for StudentReport class.
     */
    public StudentReport() {}
    
    /**
     * Constructs a new StudentReport with the specified details.
     * 
     * @param studentId The ID of the student
     * @param studentName The name of the student
     * @param studentEmail The email of the student
     */
    public StudentReport(int studentId, String studentName, String studentEmail) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }
    
    /**
     * Gets the student ID for this report.
     * 
     * @return The student ID
     */
    public int getStudentId() {
        return studentId;
    }
    
    /**
     * Sets the student ID for this report.
     * 
     * @param studentId The student ID to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    /**
     * Gets the name of the student.
     * 
     * @return The student name
     */
    public String getStudentName() {
        return studentName;
    }
    
    /**
     * Sets the name of the student.
     * 
     * @param studentName The student name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    /**
     * Gets the email of the student.
     * 
     * @return The student email
     */
    public String getStudentEmail() {
        return studentEmail;
    }
    
    /**
     * Sets the email of the student.
     * 
     * @param studentEmail The student email to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    
    /**
     * Gets the course ID for this report.
     * 
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }
    
    /**
     * Sets the course ID for this report.
     * 
     * @param courseId The course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    /**
     * Gets the course name for this report.
     * 
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }
    
    /**
     * Sets the course name for this report.
     * 
     * @param courseName The course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * Gets the total score achieved by the student.
     * 
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Sets the total score achieved by the student.
     * 
     * @param totalScore The total score to set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    /**
     * Gets the maximum possible score for the student.
     * 
     * @return The maximum score
     */
    public int getMaxScore() {
        return maxScore;
    }
    
    /**
     * Sets the maximum possible score for the student.
     * 
     * @param maxScore The maximum score to set
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    /**
     * Gets the average percentage score for the student.
     * 
     * @return The average percentage
     */
    public double getAveragePercentage() {
        return averagePercentage;
    }
    
    /**
     * Sets the average percentage score for the student.
     * 
     * @param averagePercentage The average percentage to set
     */
    public void setAveragePercentage(double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }
    
    /**
     * Gets the number of exams taken by the student.
     * 
     * @return The number of exams taken
     */
    public int getExamsTaken() {
        return examsTaken;
    }
    
    /**
     * Sets the number of exams taken by the student.
     * 
     * @param examsTaken The number of exams taken to set
     */
    public void setExamsTaken(int examsTaken) {
        this.examsTaken = examsTaken;
    }
    
    /**
     * Gets the number of exams available in the course.
     * 
     * @return The number of exams available
     */
    public int getExamsAvailable() {
        return examsAvailable;
    }
    
    /**
     * Sets the number of exams available in the course.
     * 
     * @param examsAvailable The number of exams available to set
     */
    public void setExamsAvailable(int examsAvailable) {
        this.examsAvailable = examsAvailable;
    }
    
    /**
     * Gets the list of detailed exam results for the student.
     * 
     * @return List of ExamResult objects
     */
    public List<ExamResult> getExamResults() {
        return examResults;
    }
    
    /**
     * Sets the list of detailed exam results for the student.
     * 
     * @param examResults List of ExamResult objects to set
     */
    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }
    
    /**
     * Returns a string representation of the StudentReport object.
     * 
     * @return A string containing student and report details
     */
    @Override
    public String toString() {
        return "StudentReport{" +
                "studentName='" + studentName + '\'' +
                ", totalScore=" + totalScore +
                ", maxScore=" + maxScore +
                ", averagePercentage=" + averagePercentage +
                ", examsTaken=" + examsTaken +
                '}';
    }
}
