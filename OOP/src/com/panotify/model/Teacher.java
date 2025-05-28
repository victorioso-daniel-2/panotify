package com.panotify.model;

public class Teacher extends User {
    private int instructorId;
    private String institution;
    private String department;
    
    public Teacher() {
        super();
        setAccountType("Instructor");
    }
    
    public Teacher(String fullName, String username, String email, String phoneNumber, 
                     String password, String institution, String department) {
        super(fullName, username, email, phoneNumber, password, "Instructor");
        this.institution = institution;
        this.department = department;
    }
    
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId=" + instructorId +
                ", institution='" + institution + '\'' +
                ", department='" + department + '\'' +
                ", " + super.toString() +
                '}';
    }
}