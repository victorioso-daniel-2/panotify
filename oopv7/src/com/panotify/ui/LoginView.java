package com.panotify.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.panotify.model.User;
import com.panotify.model.Student;
import com.panotify.model.Instructor;
import com.panotify.service.AuthenticationService;
import com.panotify.service.UserService;

import java.time.LocalDateTime;
import java.sql.SQLException;

/**
 * View class for handling login and registration functionality
 * Provides UI for user authentication and account creation
 */
public class LoginView {
    private Stage stage;
    private AuthenticationService authService;
    private Scene previousScene;
    private LoginCallback callback;
    private UserService userService;

    /**
     * Callback interface for handling successful login events
     */
    public interface LoginCallback {
        /**
         * Called when a user successfully logs in
         * 
         * @param user the authenticated user
         */
        void onLoginSuccess(User user);
    }

    /**
     * Creates a new LoginView with the necessary dependencies
     * 
     * @param stage the primary stage for displaying the view
     * @param authService service for authentication operations
     * @param previousScene the scene to return to when canceling login
     * @param callback callback for handling successful login
     */
    public LoginView(Stage stage, AuthenticationService authService, Scene previousScene, LoginCallback callback) {
        this.stage = stage;
        this.authService = authService;
        this.previousScene = previousScene;
        this.callback = callback;
        this.userService = new UserService();
    }

    /**
     * Shows the login view for a specific account type
     * Uses default dimensions for the login window
     * 
     * @param accountType the type of account (Student or Instructor)
     */
    public void show(String accountType) {
        show(accountType, 800, 600);
    }

    /**
     * Shows the login view for a specific account type with custom dimensions
     * 
     * @param accountType the type of account (Student or Instructor)
     * @param width the width of the login window
     * @param height the height of the login window
     */
    public void show(String accountType, int width, int height) {
        VBox loginLayout = new VBox(10);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));

        // Set background
        try {
        BackgroundImage bgImage = new BackgroundImage(
            new Image("file:src/com/images/bg_2.jpg"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, true)
        );
        loginLayout.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            loginLayout.setStyle("-fx-background-color: #f0f0f0;");
        }

        // Create login form
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");
        Button loginButton = createStyledButton("Login", "#00FF7F");
        Button registerButton = createStyledButton("Register", "#1E90FF");
        Button backButton = createStyledButton("Back", "#FFD700");

        // Style form container
        VBox formContainer = new VBox(10);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(20));
        formContainer.setMaxWidth(300);
        formContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");
        
        Label titleLabel = new Label(accountType + " Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        formContainer.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, registerButton, backButton);
        loginLayout.getChildren().add(formContainer);

        // Handle login
        loginButton.setOnAction(e -> {
            try {
                User user = authService.login(usernameField.getText(), passwordField.getText(), accountType);
                if (user != null) {
                    callback.onLoginSuccess(user);
                } else {
                    showAlert("Login Failed", "Invalid username or password");
                }
            } catch (Exception ex) {
                showAlert("Error", "An error occurred during login: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Handle register button
        registerButton.setOnAction(e -> {
            showRegistrationForm(accountType, width, height);
        });

        // Handle back button
        backButton.setOnAction(e -> stage.setScene(previousScene));

        Scene scene = new Scene(loginLayout, width, height);
        stage.setScene(scene);
    }
    
    /**
     * Shows the registration form for a specific account type
     * Uses default dimensions for the registration window
     * 
     * @param accountType the type of account (Student or Instructor)
     */
    private void showRegistrationForm(String accountType) {
        showRegistrationForm(accountType, 800, 600);
    }
    
    /**
     * Shows the registration form for a specific account type with custom dimensions
     * 
     * @param accountType the type of account (Student or Instructor)
     * @param width the width of the registration window
     * @param height the height of the registration window
     */
    private void showRegistrationForm(String accountType, int width, int height) {
        VBox registrationLayout = new VBox(10);
        registrationLayout.setAlignment(Pos.CENTER);
        registrationLayout.setPadding(new Insets(20));

        // Set background
        try {
            BackgroundImage bgImage = new BackgroundImage(
                new Image("file:src/com/images/bg_2.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
            );
            registrationLayout.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            registrationLayout.setStyle("-fx-background-color: #f0f0f0;");
        }

        // Create registration form
        TextField firstNameField = createStyledTextField("First Name");
        TextField lastNameField = createStyledTextField("Last Name");
        TextField usernameField = createStyledTextField("Username");
        TextField emailField = createStyledTextField("Email");
        TextField phoneField = createStyledTextField("Phone Number (09XXXXXXXXX)");
        PasswordField passwordField = createStyledPasswordField("Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        
        // Label for password match feedback
        Label passwordMatchLabel = new Label("");
        passwordMatchLabel.setStyle("-fx-font-size: 12px;");
        
        // Label for phone number validation
        Label phoneValidationLabel = new Label("");
        phoneValidationLabel.setStyle("-fx-font-size: 12px;");
        
        TextField institutionField = null;
        TextField departmentField = null;
        
        if ("Instructor".equals(accountType)) {
            institutionField = createStyledTextField("Institution");
            departmentField = createStyledTextField("Department");
        }
        
        Button registerButton = createStyledButton("Register", "#00FF7F");
        Button backButton = createStyledButton("Back", "#FFD700");

        // Style form container
        VBox formContainer = new VBox(10);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(20));
        formContainer.setMaxWidth(400);
        formContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");
        
        Label titleLabel = new Label(accountType + " Registration");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        formContainer.getChildren().add(titleLabel);
        formContainer.getChildren().addAll(firstNameField, lastNameField, usernameField, emailField, phoneField, phoneValidationLabel, passwordField, confirmPasswordField, passwordMatchLabel);
        
        if ("Instructor".equals(accountType)) {
            formContainer.getChildren().addAll(institutionField, departmentField);
        }
        
        formContainer.getChildren().addAll(registerButton, backButton);
        registrationLayout.getChildren().add(formContainer);
        
        // Real-time password match validation
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (confirmPasswordField.getText().isEmpty()) {
                passwordMatchLabel.setText("");
            } else if (passwordField.getText().equals(confirmPasswordField.getText())) {
                passwordMatchLabel.setText("Passwords match");
                passwordMatchLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
            } else {
                passwordMatchLabel.setText("Passwords do not match");
                passwordMatchLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!confirmPasswordField.getText().isEmpty()) {
                if (passwordField.getText().equals(confirmPasswordField.getText())) {
                    passwordMatchLabel.setText("Passwords match");
                    passwordMatchLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                } else {
                    passwordMatchLabel.setText("Passwords do not match");
                    passwordMatchLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                }
            }
        });
        
        // Phone number validation (11 digits starting with 09)
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                phoneValidationLabel.setText("");
            } else if (!newValue.matches("09\\d{9}")) {
                phoneValidationLabel.setText("Invalid phone number format. Must be 11 digits starting with 09");
                phoneValidationLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            } else {
                phoneValidationLabel.setText("Valid phone number");
                phoneValidationLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
            }
        });

        // Handle registration
        TextField finalInstitutionField = institutionField;
        TextField finalDepartmentField = departmentField;
        
        registerButton.setOnAction(e -> {
            try {
                // Validate fields
                boolean hasError = false;
                StringBuilder errorMessage = new StringBuilder("Please correct the following errors:\n");
                
                if (firstNameField.getText().isEmpty()) {
                    errorMessage.append("- First name is required\n");
                    hasError = true;
                }
                
                if (lastNameField.getText().isEmpty()) {
                    errorMessage.append("- Last name is required\n");
                    hasError = true;
                }
                
                if (usernameField.getText().isEmpty()) {
                    errorMessage.append("- Username is required\n");
                    hasError = true;
                }
                
                if (emailField.getText().isEmpty()) {
                    errorMessage.append("- Email is required\n");
                    hasError = true;
                }
                
                if (phoneField.getText().isEmpty()) {
                    errorMessage.append("- Phone number is required\n");
                    hasError = true;
                } else if (!phoneField.getText().matches("09\\d{9}")) {
                    errorMessage.append("- Phone number must be 11 digits starting with 09\n");
                    hasError = true;
                }
                
                if (passwordField.getText().isEmpty()) {
                    errorMessage.append("- Password is required\n");
                    hasError = true;
                }
                
                if (confirmPasswordField.getText().isEmpty()) {
                    errorMessage.append("- Confirm password is required\n");
                    hasError = true;
                }
                
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    errorMessage.append("- Passwords do not match\n");
                    hasError = true;
                }
                
                if ("Instructor".equals(accountType)) {
                    if (finalInstitutionField.getText().isEmpty()) {
                        errorMessage.append("- Institution is required\n");
                        hasError = true;
                    }
                    
                    if (finalDepartmentField.getText().isEmpty()) {
                        errorMessage.append("- Department is required\n");
                        hasError = true;
                    }
                }
                
                if (hasError) {
                    showAlert("Registration Error", errorMessage.toString());
                    return;
                }
                
                // Create user object based on account type
                boolean success;
                if ("Student".equals(accountType)) {
                    Student student = new Student();
                    student.setFirstName(firstNameField.getText());
                    student.setLastName(lastNameField.getText());
                    student.setUsername(usernameField.getText());
                    student.setEmail(emailField.getText());
                    student.setPhoneNumber(phoneField.getText());
                    student.setPassword(passwordField.getText());
                    student.setAccountType("Student");
                    student.setCreatedAt(LocalDateTime.now());
                    success = userService.registerStudent(student);
                } else {
                    Instructor instructor = new Instructor();
                    instructor.setFirstName(firstNameField.getText());
                    instructor.setLastName(lastNameField.getText());
                    instructor.setUsername(usernameField.getText());
                    instructor.setEmail(emailField.getText());
                    instructor.setPhoneNumber(phoneField.getText());
                    instructor.setPassword(passwordField.getText());
                    instructor.setAccountType("Instructor");
                    instructor.setInstitution(finalInstitutionField.getText());
                    instructor.setDepartment(finalDepartmentField.getText());
                    instructor.setCreatedAt(LocalDateTime.now());
                    success = userService.registerInstructor(instructor);
                }
                
                if (success) {
                    showMessage("Registration Successful", "Your " + accountType.toLowerCase() + " account has been successfully created.\nYou can now login with your credentials.", Alert.AlertType.INFORMATION);
                    // Go back to login screen
                    show(accountType, width, height);
                } else {
                    showAlert("Registration Failed", "Could not register user. Username or email may already exist.");
                }
            } catch (SQLException ex) {
                showAlert("Error", "An error occurred during registration: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Handle back button
        backButton.setOnAction(e -> show(accountType, width, height));

        Scene scene = new Scene(registrationLayout, width, height);
        stage.setScene(scene);
    }

    /**
     * Creates a styled text field with the specified prompt text
     * 
     * @param promptText the prompt text to display when field is empty
     * @return a styled TextField
     */
    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 15;"
        );
        
        // Make placeholder text only disappear when there is text
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                textField.setPromptText(promptText);
            }
        });
        
        return textField;
    }

    /**
     * Creates a styled password field with the specified prompt text
     * 
     * @param promptText the prompt text to display when field is empty
     * @return a styled PasswordField
     */
    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setStyle(
            "-fx-background-radius: 20;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 15;"
        );
        
        // Make placeholder text only disappear when there is text
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                passwordField.setPromptText(promptText);
            }
        });
        
        return passwordField;
    }

    /**
     * Creates a styled button with the specified text and color
     * 
     * @param text the text to display on the button
     * @param color the background color as a hex string
     * @return a styled Button
     */
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 0);"
        );
        button.setPrefWidth(200);
        return button;
    }

    /**
     * Shows an error alert with the specified title and message
     * 
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showAlert(String title, String message) {
        showMessage(title, message, Alert.AlertType.ERROR);
    }
    
    /**
     * Shows a message dialog with the specified title, message, and alert type
     * 
     * @param title the title of the alert
     * @param message the message to display
     * @param alertType the type of alert to show
     */
    private void showMessage(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 