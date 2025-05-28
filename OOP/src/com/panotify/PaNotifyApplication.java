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
import com.panotify.util.DatabaseUtil;
import com.panotify.model.*;
import com.panotify.service.*;
import java.sql.SQLException;

public class PaNotifyApplication extends Application {
    
    private Stage primaryStage;
    private UserService userService;
    private AuthenticationService authService;
    
    // Current logged in user
    private User currentUser;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
        this.authService = new AuthenticationService();
        
        primaryStage.setTitle("PaNotify - Secure Online Examination Platform");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        
        showWelcomeScreen();
        primaryStage.show();
    }
    
    // ===== Welcome Screen =====
    private void showWelcomeScreen() {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Header
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(50, 20, 30, 20));
        
        Label welcomeLabel = new Label("Welcome to PaNotify!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        welcomeLabel.setTextFill(Color.BLACK);
        
        Label subtitleLabel = new Label("Secure and efficient online examination platform");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitleLabel.setTextFill(Color.DARKGRAY);
        subtitleLabel.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-text-fill: white; " +
                             "-fx-padding: 10 20; -fx-background-radius: 25;");
        
        header.getChildren().addAll(welcomeLabel, subtitleLabel);
        
        // Center buttons
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        
        Button studentBtn = createStyledButton("Student", "#4CAF50");
        Button instructorBtn = createStyledButton("Instructor", "#FF9800");
        
        studentBtn.setOnAction(e -> showStudentLogin());
        instructorBtn.setOnAction(e -> showInstructorLogin());
        
        centerBox.getChildren().addAll(studentBtn, instructorBtn);
        
        // Registration section
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        
        Label registerLabel = new Label("Don't have an account?");
        registerLabel.setFont(Font.font("Arial", 16));
        
        Button registerBtn = new Button("Register now");
        registerBtn.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #666; " +
                           "-fx-font-size: 14px; -fx-padding: 8 16; -fx-background-radius: 20;");
        registerBtn.setOnAction(e -> showRegistrationOptions());
        
        bottomBox.getChildren().addAll(registerLabel, registerBtn);
        
        // Footer
        HBox footer = new HBox(30);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        
        Button helpBtn = new Button("Help");
        Button contactBtn = new Button("Contact");
        Button aboutBtn = new Button("About");
        
        styleFooterButton(helpBtn);
        styleFooterButton(contactBtn);
        styleFooterButton(aboutBtn);
        
        footer.getChildren().addAll(helpBtn, contactBtn, aboutBtn);
        
        root.setTop(header);
        root.setCenter(centerBox);
        root.setBottom(new VBox(bottomBox, footer));
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Student Login Screen =====
    private void showStudentLogin() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showWelcomeScreen());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Login form
        VBox loginBox = createLoginForm("Student Login", "Student");
        
        root.setTop(topBox);
        root.setCenter(loginBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Instructor Login Screen =====
    private void showInstructorLogin() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
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
    
    // ===== Generic Login Form =====
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
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.BLACK);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;");
        usernameField.setPrefWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;");
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
            
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }
            
            try {
                User user = authService.login(username, password, accountType);
                if (user != null) {
                    currentUser = user;
                    if (accountType.equals("Student")) {
                        showStudentProfile((Student) user);
                    } else {
                        showInstructorProfile((Instructor) user);
                    }
                } else {
                    showAlert("Login Failed", "Invalid username or password.");
                }
            } catch (SQLException ex) {
                showAlert("Database Error", "Could not connect to database: " + ex.getMessage());
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
    
    // ===== Registration Options Screen =====
    private void showRegistrationOptions() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
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
        
        Button studentBtn = createStyledButton("Register as Student", "#4CAF50");
        Button instructorBtn = createStyledButton("Register as Instructor", "#FF9800");
        
        studentBtn.setOnAction(e -> showRegistrationForm(true));
        instructorBtn.setOnAction(e -> showRegistrationForm(false));
        
        centerBox.getChildren().addAll(titleLabel, studentBtn, instructorBtn);
        
        root.setTop(topBox);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Registration Form =====
    private void showRegistrationForm(boolean isStudent) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> showRegistrationOptions());
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        
        // Registration form panel
        VBox regPanel = new VBox(15);
        regPanel.setAlignment(Pos.CENTER);
        regPanel.setPadding(new Insets(30));
        regPanel.setMaxWidth(450);
        regPanel.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        String title = isStudent ? "Register Student Account" : "Register Instructor Account";
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.BLACK);
        
        TextField fullNameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email Address");
        TextField phoneField = createStyledTextField("Phone Number");
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        
        // Additional fields for instructor
        TextField institutionField = null;
        TextField departmentField = null;
        
        if (!isStudent) {
            institutionField = createStyledTextField("Institution");
            departmentField = createStyledTextField("Department");
        }
        
        Button registerBtn = new Button("Register");
        registerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12 40; -fx-background-radius: 25;");
        registerBtn.setPrefWidth(350);
        
        // Add all fields to panel
        regPanel.getChildren().addAll(titleLabel, fullNameField, emailField, phoneField, 
                                     usernameField, passwordField, confirmPasswordField);
        
        if (!isStudent) {
            regPanel.getChildren().addAll(institutionField, departmentField);
        }
        
        regPanel.getChildren().add(registerBtn);
        
        // Footer
        HBox footerLinks = new HBox(30);
        footerLinks.setAlignment(Pos.CENTER);
        footerLinks.setPadding(new Insets(20));
        
        Button helpBtn = new Button("Help");
        Button aboutBtn = new Button("About");
        styleFooterButton(helpBtn);
        styleFooterButton(aboutBtn);
        
        footerLinks.getChildren().addAll(helpBtn, aboutBtn);
        regPanel.getChildren().add(footerLinks);
        
        // Register button action
        TextField finalInstitutionField = institutionField;
        TextField finalDepartmentField = departmentField;
        
        registerBtn.setOnAction(e -> {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            // Validation
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || 
                username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }
            
            if (!isStudent) {
                if (finalInstitutionField.getText().trim().isEmpty() || 
                    finalDepartmentField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please fill in all fields.");
                    return;
                }
            }
            
            if (!password.equals(confirmPassword)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }
            
            if (password.length() < 6) {
                showAlert("Error", "Password must be at least 6 characters long.");
                return;
            }
            
            try {
                // Check if username or email already exists
                if (DatabaseUtil.usernameExists(username)) {
                    showAlert("Error", "Username already exists. Please choose another.");
                    return;
                }
                
                if (DatabaseUtil.emailExists(email)) {
                    showAlert("Error", "Email already exists. Please use another email.");
                    return;
                }
                
                // Create user
                boolean success;
                if (isStudent) {
                    Student student = new Student(fullName, username, email, phone, password);
                    success = userService.registerStudent(student);
                } else {
                    String institution = finalInstitutionField.getText().trim();
                    String department = finalDepartmentField.getText().trim();
                    Instructor instructor = new Instructor(fullName, username, email, phone, password, institution, department);
                    success = userService.registerInstructor(instructor);
                }
                
                if (success) {
                    showAlert("Success", "Registration successful! You can now login.");
                    if (isStudent) {
                        showStudentLogin();
                    } else {
                        showInstructorLogin();
                    }
                } else {
                    showAlert("Error", "Registration failed. Please try again.");
                }
                
            } catch (SQLException ex) {
                showAlert("Database Error", "Could not connect to database: " + ex.getMessage());
            }
        });
        
        container.getChildren().add(regPanel);
        scrollPane.setContent(container);
        
        root.setTop(topBox);
        root.setCenter(scrollPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Student Profile Screen =====
    private void showStudentProfile(Student student) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button (logout)
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> {
            currentUser = null;
            showWelcomeScreen();
        });
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Profile panel
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        VBox profilePanel = new VBox(20);
        profilePanel.setAlignment(Pos.CENTER);
        profilePanel.setPadding(new Insets(40));
        profilePanel.setMaxWidth(450);
        profilePanel.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        Label titleLabel = new Label("Student Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.BLACK);
        
        // Profile picture placeholder
        Label avatarLabel = new Label("🐱");
        avatarLabel.setStyle("-fx-font-size: 60px; -fx-background-color: #F0F0F0; " +
                           "-fx-padding: 20; -fx-background-radius: 10;");
        
        // Profile information
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        
        infoBox.getChildren().addAll(
            createInfoRow("Full Name", student.getFullName()),
            createInfoRow("Username", student.getUsername()),
            createInfoRow("Email", student.getEmail()),
            createInfoRow("Phone", student.getPhoneNumber())
        );
        
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                               "-fx-padding: 10 30; -fx-background-radius: 25;");
        editProfileBtn.setPrefWidth(300);
        
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                                  "-fx-padding: 10 30; -fx-background-radius: 25;");
        changePasswordBtn.setPrefWidth(300);
        
        profilePanel.getChildren().addAll(titleLabel, avatarLabel, infoBox, editProfileBtn, changePasswordBtn);
        
        container.getChildren().add(profilePanel);
        
        root.setTop(topBox);
        root.setCenter(container);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Instructor Profile Screen =====
    private void showInstructorProfile(Instructor instructor) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98);");
        
        // Back button (logout)
        Button backBtn = new Button("←");
        backBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-text-fill: black;");
        backBtn.setOnAction(e -> {
            currentUser = null;
            showWelcomeScreen();
        });
        
        HBox topBox = new HBox(backBtn);
        topBox.setPadding(new Insets(20));
        
        // Profile panel
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        VBox profilePanel = new VBox(20);
        profilePanel.setAlignment(Pos.CENTER);
        profilePanel.setPadding(new Insets(40));
        profilePanel.setMaxWidth(450);
        profilePanel.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        
        Label titleLabel = new Label("Instructor Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.BLACK);
        
        // Profile picture placeholder
        Label avatarLabel = new Label("😸");
        avatarLabel.setStyle("-fx-font-size: 60px; -fx-background-color: #F0F0F0; " +
                           "-fx-padding: 20; -fx-background-radius: 10;");
        
        // Profile information
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        
        infoBox.getChildren().addAll(
            createInfoRow("Full Name", instructor.getFullName()),
            createInfoRow("Username", instructor.getUsername()),
            createInfoRow("Email", instructor.getEmail()),
            createInfoRow("Phone", instructor.getPhoneNumber()),
            createInfoRow("Institution", instructor.getInstitution()),
            createInfoRow("Department", instructor.getDepartment())
        );
        
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                               "-fx-padding: 10 30; -fx-background-radius: 25;");
        editProfileBtn.setPrefWidth(300);
        
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                                  "-fx-padding: 10 30; -fx-background-radius: 25;");
        changePasswordBtn.setPrefWidth(300);
        
        profilePanel.getChildren().addAll(titleLabel, avatarLabel, infoBox, editProfileBtn, changePasswordBtn);
        
        container.getChildren().add(profilePanel);
        
        root.setTop(topBox);
        root.setCenter(container);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // ===== Helper Methods =====
    
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                       "-fx-font-size: 18px; -fx-padding: 15 40; -fx-background-radius: 25; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");
        button.setPrefWidth(200);
        return button;
    }
    
    private void styleFooterButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #666; " +
                       "-fx-font-size: 14px; -fx-underline: false;");
    }
    
    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;");
        field.setPrefWidth(350);
        return field;
    }
    
    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5;");
        field.setPrefWidth(350);
        return field;
    }
    
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5));
        
        Label labelField = new Label(label);
        labelField.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        labelField.setPrefWidth(100);
        
        Label valueField = new Label(value);
        valueField.setFont(Font.font("Arial", 14));
        
        row.getChildren().addAll(labelField, valueField);
        return row;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}