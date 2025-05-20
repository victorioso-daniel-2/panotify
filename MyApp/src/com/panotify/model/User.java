package com.panotify.model;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String phone;
    private String accountType;
    private String institution;
    private String department;
    
    // Constructor with ID (for retrieving from database)
    public User(int id, String fullName, String email, String username, String password, 
                String phone, String accountType, String institution, String department) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.accountType = accountType;
        this.institution = institution;
        this.department = department;
    }
    
    // Constructor without ID (for new user registration)
    public User(String fullName, String email, String username, String password, 
                String phone, String accountType, String institution, String department) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.accountType = accountType;
        this.institution = institution;
        this.department = department;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getInstitution() {
        return institution;
    }
    
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "user_id=" + id +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", username='" + username + '\'' +
               ", accountType='" + accountType + '\'' +
               ", institution='" + institution + '\'' +
               ", department='" + department + '\'' +
               '}';
    }
}