package com.panotify.model;

public class Student extends User {
    private int studentId;
    
    public Student() {
        super();
        setAccountType("Student");
    }
    
    public Student(String fullName, String username, String email, String phoneNumber, String password) {
        super(fullName, username, email, phoneNumber, password, "Student");
    }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", " + super.toString() +
                '}';
    }
}
