package com.panotify;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import com.panotify.util.DatabaseUtil;
import com.panotify.model.*;
import com.panotify.service.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * PaNotify - A Secure Online Examination Platform
 * 
 * This is the main application class that handles the JavaFX UI and user interactions.
 * It provides functionality for student and instructor management, course management,
 * exam creation, taking exams, and viewing results.
 * 
 * @author Your Name
 * @version 1.0
 */
public class PaNotifyApplication extends Application {
    
    private Stage primaryStage;
    private UserService userService;
    private AuthenticationService authService;
    private CourseService courseService;
    private ExamService examService;
    
    // Current logged in user
    private User currentUser;
    
    // UI Components
    private TableView<Report> resultsTable;
    private TabPane tabPane;  // Add tabPane as a class field
    
    /**
     * Initializes and starts the JavaFX application.
     * Sets up the primary stage, initializes services, and shows the welcome screen.
     * 
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
        this.authService = new AuthenticationService();
        this.courseService = new CourseService();
        this.examService = new ExamService();
        
        primaryStage.setTitle("PaNotify - Secure Online Examination Platform");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);  // Reduced height
        primaryStage.setResizable(false);
        
        showWelcomeScreen();
        primaryStage.show();
    }
    
    /**
     * Shows the welcome screen with options for student and instructor login.
     * This is the initial screen of the application.
     */
    private void showWelcomeScreen() {
        BorderPane root = new BorderPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);" +
            "-fx-padding: 20;"
        );
        
        // Header
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(40, 20, 30, 20));
        header.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15;");
        
        Label welcomeLabel = new Label("Welcome To PaNotify!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        welcomeLabel.setStyle("-fx-text-fill: #2196F3;");
        
        Label subtitleLabel = new Label("Secure And Efficient Online Examination Platform");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        subtitleLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;"
        );
        
        header.getChildren().addAll(welcomeLabel, subtitleLabel);
        
        // Center content
        VBox centerBox = new VBox(25);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        centerBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.9);" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        
        // Main action buttons
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button studentBtn = createStyledButton("Student", "#4CAF50");
        Button instructorBtn = createStyledButton("Instructor", "#FF9800");
        
        studentBtn.setOnAction(e -> showStudentLogin());
        instructorBtn.setOnAction(e -> showInstructorLogin());
        
        buttonBox.getChildren().addAll(
            new Label("Choose Your Role"),
            studentBtn,
            instructorBtn
        );
        
        // Registration section
        VBox registerBox = new VBox(10);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPadding(new Insets(20));
        registerBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.7);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 10;"
        );
        
        Label registerLabel = new Label("New to PaNotify?");
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        registerLabel.setStyle("-fx-text-fill: #333333;");
        
        Button registerBtn = new Button("Create Account");
        registerBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 16;" +
            "-fx-border-color: #2196F3;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;"
        );
        registerBtn.setOnAction(e -> showRegistrationOptions());
        
        registerBox.getChildren().addAll(registerLabel, registerBtn);
        
        centerBox.getChildren().addAll(buttonBox, registerBox);
        
        // Footer
        HBox footer = new HBox(30);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        footer.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 10;");
        
        Button helpBtn = new Button("Help");
        Button contactBtn = new Button("Contact");
        Button aboutBtn = new Button("About");
        
        styleFooterButton(helpBtn);
        styleFooterButton(contactBtn);
        styleFooterButton(aboutBtn);
        
        footer.getChildren().addAll(helpBtn, contactBtn, aboutBtn);
        
        root.setTop(header);
        root.setCenter(centerBox);
        root.setBottom(footer);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Shows the student login screen with username and password fields.
     * Provides options for registration and password recovery.
     */
    private void showStudentLogin() {
        BorderPane root = new BorderPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);" +
            "-fx-padding: 20;"
        );
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> showWelcomeScreen());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(10));
        
        // Login form
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(40));
        loginBox.setMaxWidth(500);
        loginBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        
        Label titleLabel = new Label("Student Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2196F3;");
        
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");
        
        Button loginBtn = createStyledButton("Login", "#4CAF50");
        loginBtn.setPrefWidth(300);
        
        // Add login handler here...
        
        VBox registerBox = new VBox(5);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPadding(new Insets(20, 0, 0, 0));
        
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #666666;");
        
        Button registerBtn = new Button("Register Now");
        registerBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 16;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;"
        );
        registerBtn.setOnAction(e -> showRegistrationForm(true));
        
        registerBox.getChildren().addAll(noAccountLabel, registerBtn);
        
        loginBox.getChildren().addAll(
            titleLabel,
            usernameField,
            passwordField,
            loginBtn,
            registerBox
        );
        
        root.setTop(topBox);
        root.setCenter(loginBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Shows the instructor login screen with username and password fields.
     * Provides options for registration and password recovery.
     */
    private void showInstructorLogin() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-font-size: 28px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showWelcomeScreen());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Login form
        VBox loginBox = createLoginForm("Instructor Login", "Instructor");
        
        root.setTop(topBox);
        root.setCenter(loginBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Creates a generic login form that can be used for both student and instructor login.
     * 
     * @param title The title to display on the login form
     * @param accountType The type of account ("Student" or "Instructor")
     * @return VBox containing the login form elements
     */
    private VBox createLoginForm(String title, String accountType) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        // Login form panel
        VBox loginPanel = new VBox(20);
        loginPanel.setAlignment(Pos.CENTER);
        loginPanel.setPadding(new Insets(40));
        loginPanel.setMaxWidth(400);
        loginPanel.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 5;");
        usernameField.setPrefWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 5;");
        passwordField.setPrefWidth(300);
        
        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.setStyle("-fx-text-fill: #2196F3;");
        
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                         "-fx-padding: 12 40; -fx-background-radius: 25;");
        loginBtn.setPrefWidth(300);
        
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            System.out.println("Debug - Login attempt:");
            System.out.println("Username: " + username);
            System.out.println("Account type: " + accountType);
            
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }
            
            try {
                System.out.println("Debug - Calling authService.login()");
                User user = authService.login(username, password, accountType);
                System.out.println("Debug - Login result: " + (user != null ? "success" : "failed"));
                
                if (user != null) {
                    currentUser = user;
                    System.out.println("Debug - User type: " + user.getAccountType());
                    if (accountType.equalsIgnoreCase("Student") && user instanceof Student) {
                        System.out.println("Debug - Showing student profile");
                        showStudentLanding((Student) user);
                    } else if (accountType.equalsIgnoreCase("Instructor") && user instanceof Instructor) {
                        System.out.println("Debug - Showing instructor profile");
                        showInstructorLanding((Instructor) user);
                    } else {
                        showAlert("Error", "Invalid account type for this login.");
                    }
                } else {
                    showAlert("Login Failed", "Invalid username or password.");
                }
            } catch (SQLException ex) {
                System.out.println("Debug - SQL Exception: " + ex.getMessage());
                ex.printStackTrace();
                showAlert("Database Error", "Could not connect to database: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Debug - Unexpected error: " + ex.getMessage());
                ex.printStackTrace();
                showAlert("Error", "An unexpected error occurred: " + ex.getMessage());
            }
        });
        
        Label noAccountLabel = new Label("Don't you have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #666;");
        
        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setStyle("-fx-text-fill: #2196F3;");
        registerLink.setOnAction(e -> showRegistrationForm(accountType.equals("Student")));
        
        VBox registerBox = new VBox(5, noAccountLabel, registerLink);
        registerBox.setAlignment(Pos.CENTER);
        
        HBox footerLinks = new HBox(30);
        footerLinks.setAlignment(Pos.CENTER);
        
        Button helpBtn = new Button("Help");
        Button aboutBtn = new Button("About");
        styleFooterButton(helpBtn);
        styleFooterButton(aboutBtn);
        
        footerLinks.getChildren().addAll(helpBtn, aboutBtn);
        
        loginPanel.getChildren().addAll(titleLabel, usernameField, passwordField, 
                                       forgotLink, loginBtn, registerBox, footerLinks);
        
        container.getChildren().add(loginPanel);
        return container;
    }
    
    /**
     * Shows the registration options screen where users can choose to register as
     * either a student or an instructor.
     */
    private void showRegistrationOptions() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-font-size: 28px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showWelcomeScreen());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Registration options
        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(50));
        
        Label titleLabel = new Label("Register Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);
        
        Button studentBtn = createStyledButton("Student", "#4CAF50");
        Button instructorBtn = createStyledButton("Instructor", "#FF9800");
        
        studentBtn.setOnAction(e -> showRegistrationForm(true));
        instructorBtn.setOnAction(e -> showRegistrationForm(false));
        
        centerBox.getChildren().addAll(titleLabel, studentBtn, instructorBtn);
        
        root.setTop(topBox);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Shows the registration form for either student or instructor registration.
     * 
     * @param isStudent true if registering as a student, false if registering as an instructor
     */
    private void showRegistrationForm(boolean isStudent) {
        BorderPane root = new BorderPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);" +
            "-fx-padding: 20;"
        );
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> showRegistrationOptions());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(10));
        
        // Main content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        container.setSpacing(30);
        
        // Registration form panel
        VBox regPanel = new VBox(20);
        regPanel.setAlignment(Pos.CENTER);
        regPanel.setPadding(new Insets(40));
        regPanel.setMaxWidth(600);
        regPanel.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        
        String title = isStudent ? "Student Account" : "Instructor Account";
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2196F3;");
        
        // Form fields container
        VBox formFields = new VBox(15);
        formFields.setAlignment(Pos.CENTER);
        
        // Basic info section
        Label basicInfoLabel = new Label("Basic Information");
        basicInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        basicInfoLabel.setStyle("-fx-text-fill: #333333;");
        
        TextField fullNameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email Address");
        TextField phoneField = createStyledTextField("Phone Number");
        
        VBox basicInfoBox = new VBox(10);
        basicInfoBox.getChildren().addAll(basicInfoLabel, fullNameField, emailField, phoneField);
        
        // Account info section
        Label accountInfoLabel = new Label("Account Information");
        accountInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        accountInfoLabel.setStyle("-fx-text-fill: #333333;");
        
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        
        VBox accountInfoBox = new VBox(10);
        accountInfoBox.getChildren().addAll(accountInfoLabel, usernameField, passwordField, confirmPasswordField);
        
        formFields.getChildren().addAll(basicInfoBox, new Separator(), accountInfoBox);
        
        // Additional fields for instructor
        if (!isStudent) {
            Label institutionInfoLabel = new Label("Institution Information");
            institutionInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            institutionInfoLabel.setStyle("-fx-text-fill: #333333;");
            
            TextField institutionField = createStyledTextField("Institution");
            TextField departmentField = createStyledTextField("Department");
            
            VBox institutionInfoBox = new VBox(10);
            institutionInfoBox.getChildren().addAll(
                new Separator(),
                institutionInfoLabel,
                institutionField,
                departmentField
            );
            
            formFields.getChildren().add(institutionInfoBox);
        }
        
        // Register button
        Button registerBtn = createStyledButton("Create Account", "#4CAF50");
        registerBtn.setPrefWidth(300);
        
        // Add register button handler here...
        
        regPanel.getChildren().addAll(titleLabel, formFields, registerBtn);
        
        container.getChildren().add(regPanel);
        scrollPane.setContent(container);
        
        root.setTop(topBox);
        root.setCenter(scrollPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Shows the student landing page after successful login.
     * Displays enrolled courses, available exams, and results.
     * 
     * @param student The logged-in student object
     */
    private void showStudentLanding(Student student) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Top navigation bar
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: rgba(255,255,255,0.9);");
        
        Label welcomeLabel = new Label("Welcome, " + student.getFullName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        Button profileBtn = new Button("Profile");
        Button logoutBtn = new Button("Logout");
        styleNavButton(profileBtn);
        styleNavButton(logoutBtn);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(welcomeLabel, spacer, profileBtn, logoutBtn);
        
        // Center content
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Courses tab
        Tab coursesTab = new Tab("My Courses");
        VBox coursesContent = new VBox(20);
        coursesContent.setPadding(new Insets(20));
        
        // Course enrollment section
        VBox enrollmentBox = new VBox(10);
        enrollmentBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label enrollLabel = new Label("Enroll In A Course");
        enrollLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        HBox enrollInputBox = new HBox(10);
        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Enter course code");
        Button enrollBtn = new Button("Enroll");
        enrollBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        enrollInputBox.getChildren().addAll(courseCodeField, enrollBtn);
        enrollmentBox.getChildren().addAll(enrollLabel, enrollInputBox);
        
        // Enrolled courses list
        VBox courseListBox = new VBox(10);
        courseListBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label coursesLabel = new Label("My Courses");
        coursesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        ListView<Course> courseListView = new ListView<>();
        courseListView.setPrefHeight(300);
        
        courseListBox.getChildren().addAll(coursesLabel, courseListView);
        
        coursesContent.getChildren().addAll(enrollmentBox, courseListBox);
        coursesTab.setContent(coursesContent);
        
        tabPane.getTabs().add(coursesTab);
        
        // Event handlers
        profileBtn.setOnAction(e -> showStudentProfile(student));
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showWelcomeScreen();
        });
        
        enrollBtn.setOnAction(e -> {
            String courseCode = courseCodeField.getText().trim();
            if (!courseCode.isEmpty()) {
                try {
                    if (courseService.enrollStudent(student.getUserId(), courseCode)) {
                        showAlert("Success", "Successfully enrolled in the course!");
                        loadStudentCourses(courseListView, student.getUserId());
                    } else {
                        showAlert("Error", "Invalid course code or already enrolled.");
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Could not enroll in course: " + ex.getMessage());
                }
            }
        });
        
        // Load initial data
        try {
            loadStudentCourses(courseListView, student.getUserId());
        } catch (SQLException ex) {
            showAlert("Error", "Could not load data: " + ex.getMessage());
        }
        
        root.setTop(topBar);
        root.setCenter(tabPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    /**
     * Shows the instructor landing page after successful login.
     * Displays created courses, exams, and student results.
     * 
     * @param instructor The logged-in instructor object
     */
    private void showInstructorLanding(Instructor instructor) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Top navigation bar
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: rgba(255,255,255,0.9);");
        
        Label welcomeLabel = new Label("Welcome, " + instructor.getFullName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        Button profileBtn = new Button("Profile");
        Button logoutBtn = new Button("Logout");
        styleNavButton(profileBtn);
        styleNavButton(logoutBtn);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(welcomeLabel, spacer, profileBtn, logoutBtn);
        
        // Center content
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Courses tab
        Tab coursesTab = new Tab("My Courses");
        VBox coursesContent = new VBox(20);
        coursesContent.setPadding(new Insets(20));
        
        // Create course section
        VBox createCourseBox = new VBox(10);
        createCourseBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label createCourseLabel = new Label("Create New Course");
        createCourseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Course name");
        
        Button createCourseBtn = new Button("Create Course");
        createCourseBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        createCourseBox.getChildren().addAll(createCourseLabel, courseNameField, createCourseBtn);
        
        // Course list
        VBox courseListBox = new VBox(10);
        courseListBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label coursesLabel = new Label("My Courses");
        coursesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        ListView<Course> courseListView = new ListView<>();
        courseListView.setPrefHeight(300);
        
        courseListBox.getChildren().addAll(coursesLabel, courseListView);
        
        coursesContent.getChildren().addAll(createCourseBox, courseListBox);
        coursesTab.setContent(coursesContent);
        
        tabPane.getTabs().add(coursesTab);
        
        // Event handlers
        profileBtn.setOnAction(e -> showInstructorProfile(instructor));
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showWelcomeScreen();
        });
        
        createCourseBtn.setOnAction(e -> {
            String courseName = courseNameField.getText().trim();
            if (!courseName.isEmpty()) {
                try {
                    Course newCourse = new Course(courseName, generateCourseCode(), instructor.getUserId());
                    if (courseService.createCourse(newCourse)) {
                        showAlert("Success", "Course created successfully!\nCourse Code: " + newCourse.getCourseCode());
                        courseNameField.clear();
                        loadInstructorCourses(courseListView, instructor.getUserId());
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Could not create course: " + ex.getMessage());
                }
            }
        });

        // Add double-click handler for courses
        courseListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
                if (selectedCourse != null) {
                    try {
                        showInstructorCourseView(selectedCourse, courseListView, instructor.getUserId());
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not load course view: " + ex.getMessage());
                    }
                }
            }
        });
        
        // Load initial data
        try {
            loadInstructorCourses(courseListView, instructor.getUserId());
        } catch (SQLException ex) {
            showAlert("Error", "Could not load data: " + ex.getMessage());
        }
        
        root.setTop(topBar);
        root.setCenter(tabPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // Helper methods for loading data
    private void loadStudentCourses(ListView<Course> listView, int studentId) throws SQLException {
        List<Course> courses = courseService.getCoursesByStudent(studentId);
        listView.getItems().clear();
        listView.getItems().addAll(courses);
        
        listView.setStyle("-fx-font-size: 16px;"); // Increased base font size
        
        listView.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-font-size: 16px;");
                } else {
                    setText(String.format("%s (Code: %s)\nInstructor: %s", 
                        item.getCourseName(), 
                        item.getCourseCode(),
                        item.getInstructorName()));
                    setStyle("-fx-font-size: 16px;");
                }
            }
        });

        // Add click handler to show exams for selected course
        listView.setOnMouseClicked(event -> {
            Course selectedCourse = listView.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && selectedCourse != null) {
                try {
                    showCourseView(selectedCourse, listView, studentId);
                } catch (SQLException ex) {
                    showAlert("Error", "Could not load course view: " + ex.getMessage());
                }
            }
        });
    }

    private void showCourseView(Course selectedCourse, ListView<Course> listView, int studentId) throws SQLException {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        VBox contentBox = (VBox) currentTab.getContent();
        contentBox.getChildren().clear();

        // Add back button and course title in a HBox
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: #333333;");
        backButton.setOnAction(e -> handleBackButtonClick(listView, studentId));

        Label courseTitle = new Label(selectedCourse.getCourseName());
        courseTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        Label courseCode = new Label("Code: " + selectedCourse.getCourseCode());
        courseCode.setStyle("-fx-font-size: 20px; -fx-text-fill: #666;");

        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(courseTitle, courseCode);

        headerBox.getChildren().addAll(backButton, titleBox);

        // Create TabPane for Exams and Results
        TabPane courseTabPane = new TabPane();
        courseTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Exams tab
        Tab examsTab = new Tab("Exams");
        VBox examsBox = new VBox(10);
        examsBox.setPadding(new Insets(20));

        ListView<Exam> examListView = new ListView<>();
        examListView.setPrefHeight(400);

        // Load exams for this course
        loadExamsWithProgress(examListView, selectedCourse.getCourseId());

        // Add double-click handler to take exam
        examListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Exam selectedExam = examListView.getSelectionModel().getSelectedItem();
                if (selectedExam != null) {
                    try {
                        Report report = examService.getStudentReport(currentUser.getUserId(), selectedExam.getExamId());
                        if (report != null) {
                            // Show exam results if already taken
                            showExamResultsDialog(report);
                        } else {
                            // Take exam if not attempted yet
                            showTakeExamDialog(selectedExam);
                            // Refresh the exam list to update colors and scores
                            loadExamsWithProgress(examListView, selectedCourse.getCourseId());
                        }
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not load exam: " + ex.getMessage());
                    }
                }
            }
        });

        examsBox.getChildren().add(examListView);
        examsTab.setContent(examsBox);

        courseTabPane.getTabs().add(examsTab);
        contentBox.getChildren().addAll(headerBox, courseTabPane);
    }

    private void loadExamsWithProgress(ListView<Exam> examListView, int courseId) throws SQLException {
        List<Exam> exams = examService.getExamsByCourse(courseId);
        examListView.getItems().clear();
        examListView.getItems().addAll(exams);

        examListView.setStyle("-fx-font-size: 16px;"); // Increased base font size

        examListView.setCellFactory(lv -> new ListCell<Exam>() {
            @Override
            protected void updateItem(Exam exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setText(null);
                    setStyle("-fx-font-size: 16px;");
                } else {
                    try {
                        Report report = examService.getStudentReport(currentUser.getUserId(), exam.getExamId());
                        LocalDateTime deadline = exam.getDeadline();
                        boolean isExpired = deadline != null && deadline.isBefore(LocalDateTime.now());
                        
                        StringBuilder displayText = new StringBuilder(exam.getTitle());
                        if (deadline != null) {
                            displayText.append("\nDeadline: ")
                                     .append(deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
                        }
                        
                        if (report != null) {
                            // Exam has been taken - show score
                            displayText.append(String.format("\nScore: %d/%d - %.1f%%", 
                                report.getTotalScore(),
                                report.getMaxScore(),
                                (report.getTotalScore() * 100.0 / report.getMaxScore())));
                            setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
                        } else if (isExpired) {
                            // Exam expired
                            displayText.append("\nExpired");
                            setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                        } else {
                            // Exam not taken yet
                            setStyle("-fx-font-size: 16px; -fx-text-fill: #FF8C00;"); // Dark orange for not attempted
                        }
                        
                        setText(displayText.toString());
                    } catch (SQLException ex) {
                        setText(exam.getTitle());
                        setStyle("-fx-font-size: 16px;");
                    }
                }
            }
        });

        // Add double-click handler to show exam details or take exam
        examListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Exam selectedExam = examListView.getSelectionModel().getSelectedItem();
                if (selectedExam != null) {
                    try {
                        Report report = examService.getStudentReport(currentUser.getUserId(), selectedExam.getExamId());
                        if (report != null) {
                            // Show exam results if already taken
                            showExamResultsDialog(report);
                        } else {
                            // Take exam if not attempted yet
                            showTakeExamDialog(selectedExam);
                            // Refresh the exam list to update colors and scores
                            loadExamsWithProgress(examListView, courseId);
                        }
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not load exam details: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void loadStudentExams(ListView<Exam> listView, int studentId) throws SQLException {
        // Get student's courses
        List<Course> courses = courseService.getCoursesByStudent(studentId);
        List<Exam> allExams = new ArrayList<>();
        
        // Get exams for each course
        for (Course courseObj : courses) {
            allExams.addAll(examService.getExamsByCourse(courseObj.getCourseId()));
        }
        
        listView.getItems().clear();
        listView.getItems().addAll(allExams);
        listView.setCellFactory(lv -> new ListCell<Exam>() {
            @Override
            protected void updateItem(Exam item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
        
        // Add double-click handler to take exam
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Exam selectedExam = listView.getSelectionModel().getSelectedItem();
                if (selectedExam != null) {
                    try {
                        showTakeExamDialog(selectedExam);
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not load exam: " + ex.getMessage());
                    }
                }
            }
        });
    }
    
    private void loadStudentResults(TableView<Report> tableView, int studentId) throws SQLException {
        String sql = "SELECT r.*, e.exam_title, c.course_name " +
                    "FROM report r " +
                    "INNER JOIN exam e ON r.exam_id = e.exam_id " +
                    "INNER JOIN course c ON e.course_id = c.course_id " +
                    "WHERE r.student_id = ? " +
                    "ORDER BY r.submitted_at DESC";  // Add ordering to show most recent first
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                tableView.getItems().clear();
                
                while (rs.next()) {
                    Report report = new Report();
                    report.setReportId(rs.getInt("report_id"));
                    report.setStudentId(rs.getInt("student_id"));
                    report.setExamId(rs.getInt("exam_id"));
                    report.setTotalScore(rs.getInt("total_score"));
                    report.setMaxScore(rs.getInt("max_score"));
                    report.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                    
                    // Set additional display information
                    report.setExamTitle(rs.getString("exam_title"));
                    report.setCourseName(rs.getString("course_name"));
                    
                    tableView.getItems().add(report);
                }
            }
        }
    }
    
    private void loadInstructorCourses(ListView<Course> listView, int instructorId) throws SQLException {
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        listView.getItems().clear();
        listView.getItems().addAll(courses);
        listView.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCourseName() + " (Code: " + item.getCourseCode() + ")");
                }
            }
        });
    }
    
    private void loadInstructorCourses(ComboBox<Course> comboBox, int instructorId) throws SQLException {
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        comboBox.getItems().clear();
        comboBox.getItems().addAll(courses);
        comboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course object) {
                return object == null ? "" : object.getCourseName();
            }
            
            @Override
            public Course fromString(String string) {
                return null;
            }
        });
    }
    
    private void loadCourseReports(TableView<Report> tableView, int courseId) {
        try {
            String sql = "SELECT r.*, e.exam_title, u.username as student_name " +
                        "FROM report r " +
                        "INNER JOIN exam e ON r.exam_id = e.exam_id " +
                        "INNER JOIN [user] u ON r.student_id = u.user_id " +
                        "WHERE e.course_id = ?";
            
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, courseId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    tableView.getItems().clear();
                    
                    while (rs.next()) {
                        Report report = new Report();
                        report.setReportId(rs.getInt("report_id"));
                        report.setStudentId(rs.getInt("student_id"));
                        report.setExamId(rs.getInt("exam_id"));
                        report.setTotalScore(rs.getInt("total_score"));
                        report.setMaxScore(rs.getInt("max_score"));
                        report.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                        
                        // Set additional display information
                        report.setExamTitle(rs.getString("exam_title"));
                        report.setStudentName(rs.getString("student_name"));
                        
                        tableView.getItems().add(report);
                    }
                }
            }
        } catch (SQLException ex) {
            showAlert("Error", "Could not load course reports: " + ex.getMessage());
        }
    }
    
    private String generateCourseCode() {
        // Generate a random 6-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }
    
    private void showCreateExamDialog(Course courseObj, String examTitle) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Exam For " + courseObj.getCourseName());
        dialog.setHeaderText("Create exam for " + courseObj.getCourseName());
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogPane.setPrefSize(800, 600);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Add deadline picker
        Label deadlineLabel = new Label("Exam Deadline:");
        deadlineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        
        ComboBox<String> hourPicker = new ComboBox<>();
        ComboBox<String> minutePicker = new ComboBox<>();
        
        // Populate time pickers
        for (int i = 0; i < 24; i++) {
            hourPicker.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutePicker.getItems().add(String.format("%02d", i));
        }
        
        hourPicker.setValue("23");
        minutePicker.setValue("59");
        
        HBox timeBox = new HBox(10);
        timeBox.getChildren().addAll(
            new Label("Time:"),
            hourPicker,
            new Label(":"),
            minutePicker
        );

        // Add duration picker
        Label durationLabel = new Label("Exam Duration:");
        durationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        ComboBox<Integer> durationPicker = new ComboBox<>();
        durationPicker.getItems().addAll(30, 60, 90, 120, 150, 180); // 30 mins to 3 hours
        durationPicker.setValue(60); // Default to 1 hour
        
        HBox durationBox = new HBox(10);
        durationBox.getChildren().addAll(
            durationLabel,
            durationPicker,
            new Label("minutes")
        );

        // Add publish checkbox
        CheckBox publishCheckBox = new CheckBox("Publish exam immediately");
        publishCheckBox.setSelected(false);
        
        // Questions list
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        
        VBox questionsBox = new VBox(10);
        questionsBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
        questionsBox.setPrefWidth(750);
        
        List<VBox> questionPanes = new ArrayList<>();
        
        Button addQuestionBtn = new Button("Add Question");
        addQuestionBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        addQuestionBtn.setOnAction(e -> {
            VBox questionPane = createQuestionPane(questionPanes.size() + 1);
            questionPanes.add(questionPane);
            questionsBox.getChildren().add(questionPane);
        });
        
        scrollPane.setContent(questionsBox);
        scrollPane.setPrefViewportHeight(400);
        
        content.getChildren().addAll(
            new Label("Exam Title: " + examTitle),
            deadlineLabel,
            datePicker,
            timeBox,
            durationBox,
            publishCheckBox,
            new Label("Questions:"),
            scrollPane,
            addQuestionBtn
        );
        
        dialogPane.setContent(content);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    // Create exam
                    Exam exam = new Exam(examTitle, courseObj.getCourseId(), currentUser.getUserId());
                    
                    // Set deadline
                    LocalDateTime deadline = LocalDateTime.of(
                        datePicker.getValue(),
                        LocalTime.of(
                            Integer.parseInt(hourPicker.getValue()),
                            Integer.parseInt(minutePicker.getValue())
                        )
                    );
                    exam.setDeadline(deadline);
                    exam.setDurationMinutes(durationPicker.getValue());
                    exam.setPublished(publishCheckBox.isSelected());
                    
                    if (examService.createExam(exam)) {
                        // Add questions
                        for (VBox questionPane : questionPanes) {
                            TextArea questionArea = (TextArea) questionPane.lookup("#questionText");
                            TextField answerField = (TextField) questionPane.lookup("#correctAnswer");
                            TextField pointsField = (TextField) questionPane.lookup("#points");
                            
                            String questionText = questionArea.getText().trim();
                            String correctAnswer = answerField.getText().trim();
                            int points = Integer.parseInt(pointsField.getText().trim());
                            
                            Question question = new Question(exam.getExamId(), questionText, correctAnswer, points);
                            examService.addQuestion(question);
                        }
                        showAlert("Success", "Exam created successfully!");
                        return ButtonType.OK;
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Could not create exam: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private VBox createQuestionPane(int questionNumber) {
        VBox pane = new VBox(10);
        pane.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
        pane.setPrefWidth(730); // Set preferred width for question pane
        
        Label numberLabel = new Label("Question " + questionNumber);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        TextArea questionArea = new TextArea();
        questionArea.setId("questionText");
        questionArea.setPromptText("Enter question text");
        questionArea.setPrefRowCount(4); // Increased row count
        questionArea.setPrefWidth(700); // Set preferred width
        questionArea.setWrapText(true); // Enable text wrapping
        
        TextField answerField = new TextField();
        answerField.setId("correctAnswer");
        answerField.setPromptText("Correct answer");
        answerField.setPrefWidth(700); // Set preferred width
        
        HBox pointsBox = new HBox(10);
        pointsBox.setAlignment(Pos.CENTER_LEFT);
        
        Label pointsLabel = new Label("Points:");
        TextField pointsField = new TextField("1");
        pointsField.setId("points");
        pointsField.setPrefWidth(60);
        
        pointsBox.getChildren().addAll(pointsLabel, pointsField);
        
        pane.getChildren().addAll(
            numberLabel,
            new Label("Question:"),
            questionArea,
            new Label("Correct Answer:"),
            answerField,
            pointsBox
        );
        
        return pane;
    }
    
    private void showTakeExamDialog(Exam exam) throws SQLException {
        // Check if exam is published
        if (!exam.isPublished()) {
            showAlert("Error", "This exam is not yet published.");
            return;
        }

        // Check if student has already taken this exam
        Report report = examService.getStudentReport(currentUser.getUserId(), exam.getExamId());
        if (report != null) {
            showExamResultsDialog(report);
            return;
        }

        // Check if exam has expired
        if (examService.isExamTimeExpired(exam.getExamId(), currentUser.getUserId())) {
            showAlert("Error", "This exam has expired.");
            return;
        }

        // Get questions for this exam
        List<Question> questions = examService.getQuestionsByExam(exam.getExamId());
        if (questions.isEmpty()) {
            showAlert("Error", "This exam has no questions.");
            return;
        }

        // Create dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Take Exam: " + exam.getExamTitle());
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);

        // Create dialog content
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(800);
        dialogPane.setPrefHeight(600);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        // Add timer label
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Create timeline for countdown
        Timeline timeline = new Timeline();
        AtomicInteger secondsRemaining = new AtomicInteger(exam.getDurationMinutes() * 60);
        
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining.decrementAndGet();
            int minutes = secondsRemaining.get() / 60;
            int seconds = secondsRemaining.get() % 60;
            timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
            
            if (secondsRemaining.get() <= 0) {
                timeline.stop();
                // Auto submit
                try {
                    submitExam(dialog, exam, questions, getAnswerFields());
                } catch (SQLException ex) {
                    showAlert("Error", "Could not submit exam: " + ex.getMessage());
                }
            }
        });
        
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Add deadline info if exists
        if (exam.getDeadline() != null) {
            Label deadlineLabel = new Label("Deadline: " + 
                exam.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            deadlineLabel.setStyle("-fx-font-size: 14px;");
            mainContent.getChildren().add(deadlineLabel);
        }

        mainContent.getChildren().add(timerLabel);

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        List<TextArea> answerFields = new ArrayList<>();
        setAnswerFields(answerFields); // Store reference to answer fields

        // Add questions and answer fields
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            
            Label questionLabel = new Label((i + 1) + ". " + question.getQuestionText());
            questionLabel.setWrapText(true);
            questionLabel.setStyle("-fx-font-weight: bold;");

            TextArea answerField = new TextArea();
            answerField.setPromptText("Enter your answer here");
            answerField.setPrefRowCount(3);
            answerField.setWrapText(true);
            answerField.setPrefWidth(700);
            
            answerFields.add(answerField);
            
            content.getChildren().addAll(questionLabel, answerField);
        }

        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        mainContent.getChildren().add(scrollPane);
        dialogPane.setContent(mainContent);

        // Handle dialog close request
        dialog.setOnCloseRequest(event -> {
            timeline.stop();
        });

        dialog.setResultConverter(dialogButton -> {
            timeline.stop();
            if (dialogButton == ButtonType.FINISH) {
                try {
                    submitExam(dialog, exam, questions, answerFields);
                } catch (SQLException ex) {
                    showAlert("Error", "Could not submit exam: " + ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private List<TextArea> answerFieldsList;

    private void setAnswerFields(List<TextArea> fields) {
        this.answerFieldsList = fields;
    }

    private List<TextArea> getAnswerFields() {
        return this.answerFieldsList;
    }

    private void submitExam(Dialog<Void> dialog, Exam exam, List<Question> questions, List<TextArea> answerFields) throws SQLException {
        // Validate that currentUser is a Student
        if (!(currentUser instanceof Student)) {
            showAlert("Error", "Invalid user type. Only students can take exams.");
            return;
        }

        int studentId = ((Student)currentUser).getUserId();
        if (studentId <= 0) {
            showAlert("Error", "Invalid student ID.");
            return;
        }

        // Check if student has already taken this exam
        if (examService.hasAttemptedExam(studentId, exam.getExamId())) {
            showAlert("Error", "You have already taken this exam.");
            return;
        }

        // Submit answers
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String answer = answerFields.get(i).getText().trim();

            StudentAnswer studentAnswer = new StudentAnswer(
                studentId,
                question.getQuestionId(),
                answer
            );

            // Set is_correct flag
            studentAnswer.setCorrect(answer.equals(question.getCorrectAnswer()));
            examService.submitAnswer(studentAnswer);
        }

        // Grade exam and create report
        examService.gradeExam(studentId, exam.getExamId());

        // Refresh results table if it exists
        if (resultsTable != null) {
            loadStudentResults(resultsTable, studentId);
        }

        showAlert("Success", "Exam submitted successfully!");
        dialog.close();
    }
    
    private void styleNavButton(Button button) {
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 8 15;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: rgba(0,0,0,0.1);" +
                "-fx-text-fill: #333333;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 8 15;" +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #333333;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 8 15;" +
                "-fx-cursor: hand;"
            )
        );
    }
    
    // ===== Helper Methods =====
    
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 12 30;" +
            "-fx-background-radius: 5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);" +
            "-fx-cursor: hand;"
        );
        button.setPrefWidth(200);
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: derive(" + color + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 12 30;" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 12 30;" +
                "-fx-background-radius: 5;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);" +
                "-fx-cursor: hand;"
            )
        );
        
        return button;
    }
    
    private void styleFooterButton(Button button) {
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 5 10;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: rgba(0,0,0,0.1);" +
                "-fx-text-fill: #666666;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #666666;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;"
            )
        );
    }
    
    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 15;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-color: white;"
        );
        field.setPrefWidth(350);
        
        // Add focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10 15;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #2196F3;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-color: white;" +
                    "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.1), 5, 0, 0, 0);"
                );
            } else {
                field.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10 15;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #E0E0E0;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-color: white;"
                );
            }
        });
        
        return field;
    }
    
    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 15;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-color: white;"
        );
        field.setPrefWidth(350);
        
        // Add focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10 15;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #2196F3;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-color: white;" +
                    "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.1), 5, 0, 0, 0);"
                );
            } else {
                field.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10 15;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #E0E0E0;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-color: white;"
                );
            }
        });
        
        return field;
    }
    
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8));
        row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 5;");
        
        Label labelField = new Label(label);
        labelField.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        labelField.setStyle("-fx-text-fill: #333333;");
        labelField.setPrefWidth(120);
        
        Label valueField = new Label(value);
        valueField.setFont(Font.font("Arial", 14));
        valueField.setStyle("-fx-text-fill: #666666;");
        valueField.setWrapText(true);
        
        row.getChildren().addAll(labelField, valueField);
        return row;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 20;" +
            "-fx-background-radius: 5;"
        );
        
        // Style the buttons
        dialogPane.getButtonTypes().forEach(buttonType -> {
            Button button = (Button) dialogPane.lookupButton(buttonType);
            button.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 8 20;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            );
        });
        
        alert.showAndWait();
    }
    
    private void showStudentProfile(Student student) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-font-size: 28px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showStudentLanding(student));
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Profile content
        VBox profileBox = new VBox(15);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPadding(new Insets(30));
        profileBox.setMaxWidth(600);
        profileBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        Label titleLabel = new Label("Student Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);
        
        // Profile information
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(20));
        
        infoBox.getChildren().addAll(
            createInfoRow("Full Name:", student.getFullName()),
            createInfoRow("Username:", student.getUsername()),
            createInfoRow("Email:", student.getEmail()),
            createInfoRow("Phone:", student.getPhoneNumber())
        );
        
        // Edit profile button
        Button editBtn = new Button("Edit Profile");
        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                         "-fx-padding: 10 20; -fx-background-radius: 5;");
        editBtn.setOnAction(e -> showEditProfileDialog(student));
        
        // Change password button
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; " +
                                  "-fx-padding: 10 20; -fx-background-radius: 5;");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog(student));
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editBtn, changePasswordBtn);
        
        profileBox.getChildren().addAll(titleLabel, infoBox, buttonBox);
        
        root.setTop(topBox);
        root.setCenter(profileBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    private void showInstructorProfile(Instructor instructor) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-font-size: 28px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showInstructorLanding(instructor));
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Profile content
        VBox profileBox = new VBox(15);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPadding(new Insets(30));
        profileBox.setMaxWidth(600);
        profileBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        Label titleLabel = new Label("Instructor Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);
        
        // Profile information
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(20));
        
        infoBox.getChildren().addAll(
            createInfoRow("Full Name:", instructor.getFullName()),
            createInfoRow("Username:", instructor.getUsername()),
            createInfoRow("Email:", instructor.getEmail()),
            createInfoRow("Phone:", instructor.getPhoneNumber()),
            createInfoRow("Institution:", instructor.getInstitution()),
            createInfoRow("Department:", instructor.getDepartment())
        );
        
        // Edit profile button
        Button editBtn = new Button("Edit Profile");
        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                         "-fx-padding: 10 20; -fx-background-radius: 5;");
        editBtn.setOnAction(e -> showEditInstructorProfileDialog(instructor));
        
        // Change password button
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; " +
                                  "-fx-padding: 10 20; -fx-background-radius: 5;");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog(instructor));
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editBtn, changePasswordBtn);
        
        profileBox.getChildren().addAll(titleLabel, infoBox, buttonBox);
        
        root.setTop(topBox);
        root.setCenter(profileBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    private void showEditInstructorProfileDialog(Instructor instructor) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText(null);
        
        // Create the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Create form fields
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        TextField fullNameField = createStyledTextField("Full Name");
        fullNameField.setText(instructor.getFullName());
        
        TextField emailField = createStyledTextField("Email");
        emailField.setText(instructor.getEmail());
        
        TextField phoneField = createStyledTextField("Phone Number");
        phoneField.setText(instructor.getPhoneNumber());
        
        TextField institutionField = createStyledTextField("Institution");
        institutionField.setText(instructor.getInstitution());
        
        TextField departmentField = createStyledTextField("Department");
        departmentField.setText(instructor.getDepartment());
        
        content.getChildren().addAll(
            new Label("Full Name:"), fullNameField,
            new Label("Email:"), emailField,
            new Label("Phone:"), phoneField,
            new Label("Institution:"), institutionField,
            new Label("Department:"), departmentField
        );
        
        dialogPane.setContent(content);
        
        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    instructor.setFullName(fullNameField.getText().trim());
                    instructor.setEmail(emailField.getText().trim());
                    instructor.setPhoneNumber(phoneField.getText().trim());
                    instructor.setInstitution(institutionField.getText().trim());
                    instructor.setDepartment(departmentField.getText().trim());
                    
                    if (userService.updateUser(instructor)) {
                        showAlert("Success", "Profile updated successfully!");
                        showInstructorProfile(instructor);
                    } else {
                        showAlert("Error", "Could not update profile.");
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Database error: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }

    private void showEditProfileDialog(Student student) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText(null);
        
        // Create the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Create form fields
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        TextField fullNameField = createStyledTextField("Full Name");
        fullNameField.setText(student.getFullName());
        
        TextField emailField = createStyledTextField("Email");
        emailField.setText(student.getEmail());
        
        TextField phoneField = createStyledTextField("Phone Number");
        phoneField.setText(student.getPhoneNumber());
        
        content.getChildren().addAll(
            new Label("Full Name:"), fullNameField,
            new Label("Email:"), emailField,
            new Label("Phone:"), phoneField
        );
        
        dialogPane.setContent(content);
        
        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    student.setFullName(fullNameField.getText().trim());
                    student.setEmail(emailField.getText().trim());
                    student.setPhoneNumber(phoneField.getText().trim());
                    
                    if (userService.updateUser(student)) {
                        showAlert("Success", "Profile updated successfully!");
                        showStudentProfile(student);
                    } else {
                        showAlert("Error", "Could not update profile.");
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Database error: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }

    private void showChangePasswordDialog(User user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText(null);
        
        // Create the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Create form fields
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        PasswordField currentPasswordField = createStyledPasswordField("Current Password");
        PasswordField newPasswordField = createStyledPasswordField("New Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm New Password");
        
        content.getChildren().addAll(
            new Label("Current Password:"), currentPasswordField,
            new Label("New Password:"), newPasswordField,
            new Label("Confirm New Password:"), confirmPasswordField
        );
        
        dialogPane.setContent(content);
        
        // Handle the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String currentPassword = currentPasswordField.getText();
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                
                if (!newPassword.equals(confirmPassword)) {
                    showAlert("Error", "New passwords do not match.");
                    return null;
                }
                
                try {
                    if (authService.changePassword(user.getUserId(), currentPassword, newPassword)) {
                        showAlert("Success", "Password changed successfully!");
                    } else {
                        showAlert("Error", "Current password is incorrect.");
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Database error: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void showInstructorCourseView(Course selectedCourse, ListView<Course> listView, int instructorId) throws SQLException {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        VBox contentBox = (VBox) currentTab.getContent();
        contentBox.getChildren().clear();

        // Add back button and course title in a HBox
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: #333333;");
        backButton.setOnAction(e -> handleBackButtonClick(listView, instructorId));

        Label courseTitle = new Label(selectedCourse.getCourseName());
        courseTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        Label courseCode = new Label("Code: " + selectedCourse.getCourseCode());
        courseCode.setStyle("-fx-font-size: 20px; -fx-text-fill: #666;");

        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(courseTitle, courseCode);

        headerBox.getChildren().addAll(backButton, titleBox);

        // Create TabPane for Exams
        TabPane courseTabPane = new TabPane();
        courseTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Exams tab
        Tab examsTab = new Tab("Exams");
        VBox examsBox = new VBox(10);
        examsBox.setPadding(new Insets(20));

        // Create exam section
        VBox createExamBox = new VBox(10);
        createExamBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label createExamLabel = new Label("Create New Exam");
        createExamLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        TextField examTitleField = new TextField();
        examTitleField.setPromptText("Exam title");
        
        Button createExamBtn = new Button("Create Exam");
        createExamBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        ListView<Exam> examListView = new ListView<>();
        examListView.setPrefHeight(400);

        // Load exams for instructor view
        loadInstructorExams(examListView, selectedCourse.getCourseId());
        
        createExamBtn.setOnAction(e -> {
            String examTitle = examTitleField.getText().trim();
            if (!examTitle.isEmpty()) {
                showCreateExamDialog(selectedCourse, examTitle);
                examTitleField.clear();
                try {
                    loadInstructorExams(examListView, selectedCourse.getCourseId());
                } catch (SQLException ex) {
                    showAlert("Error", "Could not refresh exams: " + ex.getMessage());
                }
            }
        });
        
        createExamBox.getChildren().addAll(createExamLabel, examTitleField, createExamBtn);
        examsBox.getChildren().addAll(createExamBox, examListView);
        examsTab.setContent(examsBox);

        courseTabPane.getTabs().add(examsTab);
        contentBox.getChildren().addAll(headerBox, courseTabPane);
    }

    private void handleBackButtonClick(ListView<Course> listView, int studentId) {
        try {
            loadStudentCourses(listView, studentId);
            Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
            VBox contentBox = (VBox) currentTab.getContent();
            contentBox.getChildren().clear();
            
            // Course enrollment section
            VBox enrollmentBox = new VBox(10);
            enrollmentBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
            
            Label enrollLabel = new Label("Enroll In A Course");
            enrollLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            
            HBox enrollInputBox = new HBox(10);
            TextField courseCodeField = new TextField();
            courseCodeField.setPromptText("Enter course code");
            Button enrollBtn = new Button("Enroll");
            enrollBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            
            enrollInputBox.getChildren().addAll(courseCodeField, enrollBtn);
            enrollmentBox.getChildren().addAll(enrollLabel, enrollInputBox);
            
            // Enrolled courses list
            VBox courseListBox = new VBox(10);
            courseListBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
            
            Label coursesLabel = new Label("My Courses");
            coursesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            
            courseListBox.getChildren().addAll(coursesLabel, listView);
            
            contentBox.getChildren().addAll(enrollmentBox, courseListBox);
        } catch (SQLException ex) {
            showAlert("Error", "Could not load courses: " + ex.getMessage());
        }
    }
    
    private void loadStudentProgress(TableView<StudentProgress> table, int courseId) {
        try {
            List<StudentProgress> progress = courseService.getStudentProgress(courseId);
            table.getItems().clear();
            table.getItems().addAll(progress);
        } catch (SQLException ex) {
            showAlert("Error", "Could not load student progress: " + ex.getMessage());
        }
    }
    
    private void showExamResultsDialog(Report report) throws SQLException {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Exam Results");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(600);
        dialogPane.setPrefHeight(400);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Get exam details
        Exam exam = examService.getExam(report.getExamId());
        
        // Header information
        Label titleLabel = new Label("Exam: " + exam.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Label scoreLabel = new Label(String.format("Score: %d/%d (%.1f%%)", 
            report.getTotalScore(), 
            report.getMaxScore(),
            (report.getTotalScore() * 100.0 / report.getMaxScore())));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label dateLabel = new Label("Submitted: " + report.getSubmittedAt().toString());
        dateLabel.setFont(Font.font("Arial", 14));

        // Get student answers and questions
        List<Question> questions = examService.getQuestionsByExam(exam.getExamId());
        List<StudentAnswer> answers = examService.getStudentAnswers(currentUser.getUserId(), exam.getExamId());

        // Create a scrollable area for questions and answers
        ScrollPane scrollPane = new ScrollPane();
        VBox questionsBox = new VBox(15);
        questionsBox.setPadding(new Insets(10));
        
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            StudentAnswer answer = answers.stream()
                .filter(a -> a.getQuestionId() == question.getQuestionId())
                .findFirst()
                .orElse(null);

            VBox questionBox = new VBox(5);
            questionBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

            Label questionLabel = new Label((i + 1) + ". " + question.getQuestionText());
            questionLabel.setWrapText(true);
            questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Label yourAnswerLabel = new Label("Your Answer: " + (answer != null ? answer.getAnswerText() : "No answer"));
            yourAnswerLabel.setWrapText(true);

            Label correctAnswerLabel = new Label("Correct Answer: " + question.getCorrectAnswer());
            correctAnswerLabel.setWrapText(true);

            Label pointsLabel = new Label(String.format("Points: %d/%d", 
                (answer != null && answer.isCorrect() ? question.getPoints() : 0),
                question.getPoints()));

            // Set color based on correctness
            if (answer != null) {
                questionBox.setStyle(questionBox.getStyle() + 
                    (answer.isCorrect() ? "; -fx-background-color: #E8F5E9;" : "; -fx-background-color: #FFEBEE;"));
            }

            questionBox.getChildren().addAll(questionLabel, yourAnswerLabel, correctAnswerLabel, pointsLabel);
            questionsBox.getChildren().add(questionBox);
        }

        scrollPane.setContent(questionsBox);
        scrollPane.setFitToWidth(true);

        content.getChildren().addAll(titleLabel, scoreLabel, dateLabel, new Separator(), scrollPane);
        dialogPane.setContent(content);

        dialog.showAndWait();
    }
    
    private void showEditExamDialog(Exam exam) {
        if (exam.isPublished()) {
            // Show dialog to unpublish first
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Exam Published");
            alert.setHeaderText("This exam is currently published");
            alert.setContentText("You need to unpublish the exam before editing. Would you like to unpublish it now?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        if (examService.unpublishExam(exam.getExamId())) {
                            exam.setPublished(false);
                            showEditExamDialog(exam); // Recursive call after unpublishing
                        }
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not unpublish exam: " + ex.getMessage());
                    }
                }
            });
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Exam: " + exam.getTitle());
        dialog.setHeaderText(null);
        
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OTHER);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL, deleteButtonType);
        Button deleteButton = (Button) dialogPane.lookupButton(deleteButtonType);
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        dialogPane.setPrefSize(800, 600);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Title field
        TextField titleField = new TextField(exam.getTitle());
        titleField.setPromptText("Exam title");
        
        // Deadline picker
        Label deadlineLabel = new Label("Exam Deadline:");
        deadlineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        LocalDateTime currentDeadline = exam.getDeadline();
        DatePicker datePicker = new DatePicker(currentDeadline != null ? currentDeadline.toLocalDate() : LocalDate.now());
        
        ComboBox<String> hourPicker = new ComboBox<>();
        ComboBox<String> minutePicker = new ComboBox<>();
        
        // Populate time pickers
        for (int i = 0; i < 24; i++) {
            hourPicker.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutePicker.getItems().add(String.format("%02d", i));
        }
        
        hourPicker.setValue(currentDeadline != null ? String.format("%02d", currentDeadline.getHour()) : "23");
        minutePicker.setValue(currentDeadline != null ? String.format("%02d", currentDeadline.getMinute()) : "59");
        
        HBox timeBox = new HBox(10);
        timeBox.getChildren().addAll(
            new Label("Time:"),
            hourPicker,
            new Label(":"),
            minutePicker
        );

        // Duration picker
        Label durationLabel = new Label("Exam Duration:");
        durationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        ComboBox<Integer> durationPicker = new ComboBox<>();
        durationPicker.getItems().addAll(30, 60, 90, 120, 150, 180); // 30 mins to 3 hours
        durationPicker.setValue(exam.getDurationMinutes());
        
        HBox durationBox = new HBox(10);
        durationBox.getChildren().addAll(
            durationLabel,
            durationPicker,
            new Label("minutes")
        );

        // Action buttons for publishing
        HBox actionBox = new HBox(10);
        Button publishButton = new Button(exam.isPublished() ? "Unpublish" : "Publish");
        publishButton.setStyle(exam.isPublished() ? 
            "-fx-background-color: #ff9800; -fx-text-fill: white;" :  // Orange for unpublish
            "-fx-background-color: #4CAF50; -fx-text-fill: white;"    // Green for publish
        );
        
        publishButton.setOnAction(e -> {
            try {
                if (exam.isPublished()) {
                    if (examService.unpublishExam(exam.getExamId())) {
                        exam.setPublished(false);
                        publishButton.setText("Publish");
                        publishButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        showAlert("Success", "Exam unpublished successfully!");
                    }
                } else {
                    if (examService.publishExam(exam.getExamId())) {
                        exam.setPublished(true);
                        publishButton.setText("Unpublish");
                        publishButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white;");
                        showAlert("Success", "Exam published successfully!");
                    }
                }
            } catch (SQLException ex) {
                showAlert("Error", "Could not change exam publish status: " + ex.getMessage());
            }
        });
        
        actionBox.getChildren().add(publishButton);

        // Questions list
        ScrollPane questionsScrollPane = new ScrollPane();
        questionsScrollPane.setFitToWidth(true);
        
        VBox questionsBox = new VBox(10);
        questionsBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
        questionsBox.setPrefWidth(750);
        
        List<VBox> questionPanes = new ArrayList<>();
        
        // Load existing questions
        try {
            List<Question> questions = examService.getQuestionsByExam(exam.getExamId());
            for (Question question : questions) {
                VBox questionPane = createQuestionPane(questionPanes.size() + 1);
                
                // Find and set the question text
                TextArea questionArea = (TextArea) questionPane.lookup("#questionText");
                questionArea.setText(question.getQuestionText());
                
                // Find and set the correct answer
                TextField answerField = (TextField) questionPane.lookup("#correctAnswer");
                answerField.setText(question.getCorrectAnswer());
                
                // Find and set the points
                TextField pointsField = (TextField) questionPane.lookup("#points");
                pointsField.setText(String.valueOf(question.getPoints()));
                
                // Add delete question button
                Button deleteQuestionBtn = new Button("Delete Question");
                deleteQuestionBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                deleteQuestionBtn.setOnAction(e -> {
                    questionPanes.remove(questionPane);
                    questionsBox.getChildren().remove(questionPane);
                    // Renumber remaining questions
                    for (int i = 0; i < questionPanes.size(); i++) {
                        VBox pane = questionPanes.get(i);
                        Label numberLabel = (Label) pane.getChildren().get(0);
                        numberLabel.setText("Question " + (i + 1));
                    }
                });
                
                questionPane.getChildren().add(deleteQuestionBtn);
                questionPanes.add(questionPane);
                questionsBox.getChildren().add(questionPane);
            }
        } catch (SQLException ex) {
            showAlert("Error", "Could not load questions: " + ex.getMessage());
        }
        
        Button addQuestionBtn = new Button("Add Question");
        addQuestionBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        addQuestionBtn.setOnAction(e -> {
            VBox questionPane = createQuestionPane(questionPanes.size() + 1);
            
            // Add delete question button
            Button deleteQuestionBtn = new Button("Delete Question");
            deleteQuestionBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
            deleteQuestionBtn.setOnAction(event -> {
                questionPanes.remove(questionPane);
                questionsBox.getChildren().remove(questionPane);
                // Renumber remaining questions
                for (int i = 0; i < questionPanes.size(); i++) {
                    VBox pane = questionPanes.get(i);
                    Label numberLabel = (Label) pane.getChildren().get(0);
                    numberLabel.setText("Question " + (i + 1));
                }
            });
            
            questionPane.getChildren().add(deleteQuestionBtn);
            questionPanes.add(questionPane);
            questionsBox.getChildren().add(questionPane);
        });
        
        questionsScrollPane.setContent(questionsBox);
        questionsScrollPane.setPrefViewportHeight(300);
        
        content.getChildren().addAll(
            new Label("Exam Title:"),
            titleField,
            deadlineLabel,
            datePicker,
            timeBox,
            durationBox,
            actionBox,
            new Label("Questions:"),
            questionsScrollPane,
            addQuestionBtn
        );
        
        dialogPane.setContent(content);
        
        // Handle delete button action
        deleteButton.setOnAction(e -> {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Delete Exam");
            confirmDelete.setHeaderText("Are you sure you want to delete this exam?");
            confirmDelete.setContentText("This action cannot be undone.");
            
            confirmDelete.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        if (examService.deleteExam(exam.getExamId())) {
                            showAlert("Success", "Exam deleted successfully!");
                            dialog.close();
                        }
                    } catch (SQLException ex) {
                        showAlert("Error", "Could not delete exam: " + ex.getMessage());
                    }
                }
            });
        });
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    // Update exam details
                    exam.setTitle(titleField.getText().trim());
                    
                    // Set deadline
                    LocalDateTime deadline = LocalDateTime.of(
                        datePicker.getValue(),
                        LocalTime.of(
                            Integer.parseInt(hourPicker.getValue()),
                            Integer.parseInt(minutePicker.getValue())
                        )
                    );
                    exam.setDeadline(deadline);
                    exam.setDurationMinutes(durationPicker.getValue());
                    
                    if (examService.updateExam(exam)) {
                        // Update questions
                        examService.deleteQuestions(exam.getExamId()); // Remove old questions
                        
                        for (VBox questionPane : questionPanes) {
                            TextArea questionArea = (TextArea) questionPane.lookup("#questionText");
                            TextField answerField = (TextField) questionPane.lookup("#correctAnswer");
                            TextField pointsField = (TextField) questionPane.lookup("#points");
                            
                            String questionText = questionArea.getText().trim();
                            String correctAnswer = answerField.getText().trim();
                            int points = Integer.parseInt(pointsField.getText().trim());
                            
                            Question question = new Question(exam.getExamId(), questionText, correctAnswer, points);
                            examService.addQuestion(question);
                        }
                        
                        showAlert("Success", "Exam updated successfully!");
                        return ButtonType.OK;
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Could not update exam: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }

    private void loadInstructorExams(ListView<Exam> examListView, int courseId) throws SQLException {
        List<Exam> exams = examService.getExamsByCourse(courseId);
        examListView.getItems().clear();
        examListView.getItems().addAll(exams);

        examListView.setCellFactory(lv -> new ListCell<Exam>() {
            @Override
            protected void updateItem(Exam exam, boolean empty) {
                super.updateItem(exam, empty);
                if (empty || exam == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-font-size: 16px;");
                } else {
                    try {
                        // Get exam statistics
                        int totalStudents = courseService.getEnrolledStudentCount(exam.getCourseId());
                        int attemptedCount = examService.getAttemptedCount(exam.getExamId());
                        double avgScore = examService.getAverageScore(exam.getExamId());
                        
                        StringBuilder displayText = new StringBuilder();
                        displayText.append(exam.getTitle())
                                 .append("\nStatus: ").append(exam.isPublished() ? "Published" : "Draft")
                                 .append("\nAttempts: ").append(attemptedCount).append("/").append(totalStudents)
                                 .append("\nAvg Score: ").append(String.format("%.1f%%", avgScore));
                        
                        if (exam.getDeadline() != null) {
                            displayText.append("\nDeadline: ")
                                     .append(exam.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
                        }
                        
                        // Create a container for the cell content
                        VBox container = new VBox(5);
                        container.setPadding(new Insets(5));
                        
                        // Add text
                        Label textLabel = new Label(displayText.toString());
                        textLabel.setWrapText(true);
                        
                        // Add edit button
                        Button editButton = new Button("Edit");
                        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        editButton.setOnAction(e -> {
                            showEditExamDialog(exam);
                            try {
                                loadInstructorExams(examListView, courseId);
                            } catch (SQLException ex) {
                                showAlert("Error", "Could not refresh exams: " + ex.getMessage());
                            }
                        });
                        
                        // Add results button
                        Button resultsButton = new Button("Results");
                        resultsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-margin-left: 10;");
                        resultsButton.setOnAction(e -> {
                            try {
                                showExamResultsForInstructor(exam);
                            } catch (SQLException ex) {
                                showAlert("Error", "Could not load exam results: " + ex.getMessage());
                            }
                        });
                        
                        // Create HBox for buttons
                        HBox buttonBox = new HBox(10);
                        buttonBox.getChildren().addAll(editButton, resultsButton);
                        
                        container.getChildren().addAll(textLabel, buttonBox);
                        setGraphic(container);
                        setText(null);
                        
                        // Set color based on publish status
                        if (exam.isPublished()) {
                            setStyle("-fx-font-size: 16px; -fx-text-fill: #2E7D32;"); // Dark green for published
                        } else {
                            setStyle("-fx-font-size: 16px; -fx-text-fill: #F57C00;"); // Orange for draft
                        }
                    } catch (SQLException ex) {
                        setText(exam.getTitle());
                        setStyle("-fx-font-size: 16px;");
                    }
                }
            }
        });
    }

    private void showExamResultsForInstructor(Exam exam) throws SQLException {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Exam Results: " + exam.getTitle());
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefWidth(800);
        dialogPane.setPrefHeight(600);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Header information
        int totalStudents = courseService.getEnrolledStudentCount(exam.getCourseId());
        int attemptedCount = examService.getAttemptedCount(exam.getExamId());
        double avgScore = examService.getAverageScore(exam.getExamId());

        Label summaryLabel = new Label(String.format("Summary: %d/%d students attempted, Average Score: %.1f%%",
            attemptedCount, totalStudents, avgScore));
        summaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Create table for results
        TableView<StudentExamResult> resultsTable = new TableView<>();
        resultsTable.setPrefHeight(500);

        // Define columns
        TableColumn<StudentExamResult, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentName()));
        nameCol.setPrefWidth(200);

        TableColumn<StudentExamResult, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(100);

        TableColumn<StudentExamResult, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getScore()));
        scoreCol.setPrefWidth(100);

        TableColumn<StudentExamResult, String> submittedCol = new TableColumn<>("Submitted At");
        submittedCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubmittedAt()));
        submittedCol.setPrefWidth(200);

        resultsTable.getColumns().addAll(nameCol, statusCol, scoreCol, submittedCol);

        // Get all enrolled students
        List<StudentExamResult> results = new ArrayList<>();
        String sql = "SELECT u.username as student_name, r.total_score, r.max_score, r.submitted_at " +
                    "FROM [user] u " +
                    "INNER JOIN [user_course] uc ON u.user_id = uc.user_id " +
                    "LEFT JOIN report r ON r.student_id = u.user_id AND r.exam_id = ? " +
                    "WHERE uc.course_id = ? AND u.account_type = 'Student'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exam.getExamId());
            stmt.setInt(2, exam.getCourseId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    Integer totalScore = rs.getObject("total_score", Integer.class);
                    Integer maxScore = rs.getObject("max_score", Integer.class);
                    java.sql.Timestamp submittedAt = rs.getTimestamp("submitted_at");

                    StudentExamResult result = new StudentExamResult();
                    result.setStudentName(studentName);

                    if (totalScore == null || maxScore == null) {
                        result.setStatus("Not Attempted");
                        result.setScore("N/A");
                        result.setSubmittedAt("N/A");
                    } else {
                        result.setStatus("Completed");
                        result.setScore(String.format("%d/%d (%.1f%%)", 
                            totalScore, maxScore, (totalScore * 100.0 / maxScore)));
                        result.setSubmittedAt(submittedAt.toLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
                    }

                    results.add(result);
                }
            }
        }

        resultsTable.getItems().addAll(results);

        content.getChildren().addAll(summaryLabel, resultsTable);
        dialogPane.setContent(content);

        dialog.showAndWait();
    }

    // Helper class for exam results
    private static class StudentExamResult {
        private String studentName;
        private String status;
        private String score;
        private String submittedAt;

        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getScore() { return score; }
        public void setScore(String score) { this.score = score; }
        public String getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
    }
    
    /**
     * The main entry point for the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}