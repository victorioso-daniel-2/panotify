package com.panotify.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a student user in the PaNotify system.
 * Extends the base User class with student-specific functionality.
 * Manages enrolled courses and exam results for the student.
 */
public class Student extends User {
    private List<CourseInfo> enrolledCourses;
    private List<ExamResult> examResults;
    
    /**
     * Default constructor for Student class.
     * Initializes an empty list of enrolled courses and sets account type to "student".
     */
    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        setAccountType("student");
    }
    
    /**
     * Constructs a new Student with the specified details.
     * Splits the full name into first and last name components.
     * 
     * @param fullName The student's full name
     * @param username The student's username
     * @param email The student's email address
     * @param phoneNumber The student's phone number
     * @param password The student's password
     */
    public Student(String fullName, String username, String email, String phoneNumber, String password) {
        super(
            fullName.split(" ", 2)[0], 
            fullName.split(" ", 2).length > 1 ? fullName.split(" ", 2)[1] : "",
            username,
            email,
            phoneNumber,
            password,
            "student"
        );
        this.enrolledCourses = new ArrayList<>();
    }
    
    /**
     * Gets the list of courses in which the student is enrolled.
     * 
     * @return List of CourseInfo objects representing enrolled courses
     */
    public List<CourseInfo> getEnrolledCourses() {
        return enrolledCourses;
    }
    
    /**
     * Sets the list of courses in which the student is enrolled.
     * 
     * @param enrolledCourses List of CourseInfo objects to set
     */
    public void setEnrolledCourses(List<CourseInfo> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
    
    /**
     * Gets the list of exam results for this student.
     * 
     * @return List of ExamResult objects representing exam performance
     */
    public List<ExamResult> getExamResults() {
        return examResults;
    }
    
    /**
     * Sets the list of exam results for this student.
     * 
     * @param examResults List of ExamResult objects to set
     */
    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }
    
    /**
     * Adds a course to the student's list of enrolled courses.
     * 
     * @param course The CourseInfo object to add
     */
    public void addEnrolledCourse(CourseInfo course) {
        this.enrolledCourses.add(course);
    }
    
    /**
     * Removes a course from the student's list of enrolled courses.
     * 
     * @param course The CourseInfo object to remove
     */
    public void removeEnrolledCourse(CourseInfo course) {
        this.enrolledCourses.remove(course);
    }
    
    /**
     * Returns a string representation of the Student object.
     * Includes user ID, full name, username, email, phone number,
     * institution, and department.
     * 
     * @return A string containing the student's details
     */
    @Override
    public String toString() {
        return "Student{" +
                "userId=" + getUserId() +
                ", fullName='" + getFullName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", institution='" + getInstitution() + '\'' +
                ", department='" + getDepartment() + '\'' +
                '}';
    }
}