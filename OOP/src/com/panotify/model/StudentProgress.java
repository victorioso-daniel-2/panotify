package com.panotify.model;

/**
 * Represents a student's progress in a course.
 * Contains statistics about exams taken, total exams, and average score.
 */
public class StudentProgress {
    private String studentName;
    private int examsTaken;
    private int totalExams;
    private double averageScore;

    /**
     * Constructs a new StudentProgress with the specified details.
     *
     * @param studentName The name of the student
     * @param examsTaken The number of exams taken
     * @param totalExams The total number of exams available
     * @param averageScore The average score as a percentage
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
     * @return The student name
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
     * Gets the average score as a percentage.
     *
     * @return The average score
     */
    public double getAverageScore() {
        return averageScore;
    }

    /**
     * Sets the student's name.
     *
     * @param studentName The student name to set
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
     * Sets the average score as a percentage.
     *
     * @param averageScore The average score to set
     */
    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
} 