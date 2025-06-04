package com.panotify.model;

import java.util.List;

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
    
    // Constructors
    public StudentReport() {}
    
    public StudentReport(int studentId, String studentName, String studentEmail) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }
    
    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getStudentEmail() {
        return studentEmail;
    }
    
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    public double getAveragePercentage() {
        return averagePercentage;
    }
    
    public void setAveragePercentage(double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }
    
    public int getExamsTaken() {
        return examsTaken;
    }
    
    public void setExamsTaken(int examsTaken) {
        this.examsTaken = examsTaken;
    }
    
    public int getExamsAvailable() {
        return examsAvailable;
    }
    
    public void setExamsAvailable(int examsAvailable) {
        this.examsAvailable = examsAvailable;
    }
    
    public List<ExamResult> getExamResults() {
        return examResults;
    }
    
    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }
    
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