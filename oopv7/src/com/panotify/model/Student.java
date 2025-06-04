package com.panotify.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user in the PaNotify system.
 * Extends the base User class with student-specific attributes and behaviors,
 * such as enrolled courses and exam results.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Student extends User {
    /** List of courses the student is enrolled in */
    private List<CourseInfo> enrolledCourses;
    
    /** List of exam results for the student */
    private List<ExamResult> examResults;

    /**
     * Default constructor for Student.
     * Initializes empty lists for enrolled courses and exam results.
     */
    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        this.examResults = new ArrayList<>();
    }

    /**
     * Parameterized constructor to create a new student with essential information.
     * Sets the account type to "student".
     * 
     * @param firstName Student's first name
     * @param lastName Student's last name
     * @param username Unique username for login
     * @param email Student's email address
     * @param phoneNumber Student's contact phone number
     * @param password Student's password
     */
    public Student(String firstName, String lastName, String username, String email, String phoneNumber, String password) {
        super(firstName, lastName, username, email, phoneNumber, password, "student");
        this.enrolledCourses = new ArrayList<>();
        this.examResults = new ArrayList<>();
    }

    /**
     * Gets the list of courses the student is enrolled in.
     * 
     * @return List of enrolled courses
     */
    public List<CourseInfo> getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * Sets the list of courses the student is enrolled in.
     * 
     * @param enrolledCourses List of enrolled courses to set
     */
    public void setEnrolledCourses(List<CourseInfo> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * Gets the list of exam results for the student.
     * 
     * @return List of exam results
     */
    public List<ExamResult> getExamResults() {
        return examResults;
    }

    /**
     * Sets the list of exam results for the student.
     * 
     * @param examResults List of exam results to set
     */
    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }

    /**
     * Adds a course to the student's enrolled courses if not already enrolled.
     * 
     * @param course The course to enroll in
     */
    public void addEnrolledCourse(CourseInfo course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    /**
     * Removes a course from the student's enrolled courses.
     * 
     * @param course The course to remove
     */
    public void removeEnrolledCourse(CourseInfo course) {
        enrolledCourses.remove(course);
    }

    /**
     * Gets the student's phone number.
     * Convenience method that delegates to the parent class.
     * 
     * @return The phone number
     */
    public String getPhone() {
        return getPhoneNumber();
    }

    /**
     * Gets the student's major.
     * Convenience method that delegates to the parent class's department field.
     * 
     * @return The major
     */
    public String getMajor() {
        return getDepartment();
    }

    /**
     * Gets the student's ID as a string.
     * Converts the numeric user ID to a string.
     * 
     * @return The student ID as a string
     */
    public String getStudentId() {
        return String.valueOf(getUserId());
    }

    /**
     * Returns a string representation of the Student object.
     * 
     * @return A string containing the student's key information
     */
    @Override
    public String toString() {
        return "Student{" +
                "userId=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", enrolledCourses=" + enrolledCourses.size() +
                '}';
    }
} 