package com.panotify.model;

/**
 * Represents a student's progress in a course within the PaNotify system.
 * Contains information about exam completion status and performance metrics.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class StudentProgress {
    /** Full name of the student */
    private String studentName;
    
    /** Number of exams the student has taken */
    private int examsTaken;
    
    /** Total number of exams available in the course */
    private int totalExams;
    
    /** Average score across all exams taken by the student */
    private double averageScore;

    /**
     * Parameterized constructor to create a new student progress record.
     * 
     * @param studentName Full name of the student
     * @param examsTaken Number of exams the student has taken
     * @param totalExams Total number of exams available in the course
     * @param averageScore Average score across all exams taken by the student
     */
    public StudentProgress(String studentName, int examsTaken, int totalExams, double averageScore) {
        this.studentName = studentName;
        this.examsTaken = examsTaken;
        this.totalExams = totalExams;
        this.averageScore = averageScore;
    }

    /**
     * Gets the student's name.
     * 
     * @return The student's name
     */
    public String getStudentName() {
        return studentName;
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
     * Gets the total number of exams available in the course.
     * 
     * @return The total number of exams
     */
    public int getTotalExams() {
        return totalExams;
    }

    /**
     * Gets the student's average score across all exams taken.
     * 
     * @return The average score
     */
    public double getAverageScore() {
        return averageScore;
    }

    /**
     * Sets the student's name.
     * 
     * @param studentName The student's name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
     * Sets the total number of exams available in the course.
     * 
     * @param totalExams The total number of exams to set
     */
    public void setTotalExams(int totalExams) {
        this.totalExams = totalExams;
    }

    /**
     * Sets the student's average score across all exams taken.
     * 
     * @param averageScore The average score to set
     */
    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
} 