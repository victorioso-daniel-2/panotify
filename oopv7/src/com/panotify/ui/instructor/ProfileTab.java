package com.panotify.ui.instructor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

import com.panotify.model.Course;
import com.panotify.model.Instructor;
import com.panotify.service.CourseService;
import com.panotify.service.UserService;
import com.panotify.ui.UIUtils;

/**
 * Tab for managing instructor profile information
 * Provides interface for viewing and editing profile details,
 * changing password, and managing account settings
 */
public class ProfileTab implements InstructorTab {
    private Instructor instructor;
    private UserService userService;
    private CourseService courseService;
    private InstructorDashboard dashboard;
    private VBox content;
    
    /**
     * Creates a new ProfileTab with the necessary services and data
     * 
     * @param instructor the logged-in instructor user
     * @param userService service for user-related operations
     * @param courseService service for course-related operations
     * @param dashboard reference to the parent dashboard
     */
    public ProfileTab(Instructor instructor, UserService userService, CourseService courseService, 
                      InstructorDashboard dashboard) {
        this.instructor = instructor;
        this.userService = userService;
        this.courseService = courseService;
        this.dashboard = dashboard;
        this.content = new VBox();
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.CENTER);
    }
    
    /**
     * Gets the content pane for this tab
     * 
     * @return the VBox containing this tab's content
     */
    @Override
    public VBox getContent() {
        return content;
    }
    
    /**
     * Called when this tab is navigated to
     * Refreshes the content and hides the sidebar
     */
    @Override
    public void onNavigatedTo() {
        // Clear existing content
        content.getChildren().clear();
        
        // Hide the sidebar when viewing profile
        dashboard.hideSidebar();
        
        // Add the profile content
        content.getChildren().add(createProfileView());
    }
    
    /**
     * Creates the profile view UI
     * Sets up the layout with user information and action buttons
     * 
     * @return a VBox containing the profile view elements
     */
    private VBox createProfileView() {
        // Main container
        VBox container = new VBox(20);
        container.setMaxWidth(600);
        container.setAlignment(Pos.CENTER);
        
        // Profile card with semi-transparent background, darker border and shadow
        VBox profileCard = new VBox(20);
        profileCard.setMaxWidth(550);
        profileCard.setPadding(new Insets(30));
        profileCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #333333;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );
        profileCard.setAlignment(Pos.TOP_CENTER);
        
        // Title
        Label titleLabel = new Label("Instructor Profile");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        
        // Profile avatar with initials
        StackPane avatarPane = new StackPane();
        avatarPane.setMinSize(100, 100);
        avatarPane.setMaxSize(100, 100);
        
        Circle avatarCircle = new Circle(50);
        avatarCircle.setFill(Color.valueOf("#00d355"));  // Green color for instructor
        
        String initials = "";
        if (instructor.getFirstName() != null && !instructor.getFirstName().isEmpty()) {
            initials += instructor.getFirstName().charAt(0);
        }
        if (instructor.getLastName() != null && !instructor.getLastName().isEmpty()) {
            initials += instructor.getLastName().charAt(0);
        }
        
        Label initialsLabel = new Label(initials.toUpperCase());
        initialsLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        avatarPane.getChildren().addAll(avatarCircle, initialsLabel);
        
        // Profile information table
        GridPane infoGrid = new GridPane();
        infoGrid.setAlignment(Pos.CENTER);
        infoGrid.setHgap(20);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(20, 0, 20, 0));
        
        // Column styling - center aligned
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);
        col1.setHalignment(javafx.geometry.HPos.RIGHT);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(200);
        col2.setHalignment(javafx.geometry.HPos.LEFT);
        
        infoGrid.getColumnConstraints().addAll(col1, col2);
        
        // Add fields
        addProfileField(infoGrid, 0, "Full Name", instructor.getFirstName() + " " + instructor.getLastName());
        addProfileField(infoGrid, 1, "Username", instructor.getUsername());
        addProfileField(infoGrid, 2, "Email", instructor.getEmail());
        addProfileField(infoGrid, 3, "Phone", instructor.getPhoneNumber());
        addProfileField(infoGrid, 4, "Institution", instructor.getInstitution());
        addProfileField(infoGrid, 5, "Department", instructor.getDepartment());
        
        // Action buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");
        editProfileBtn.setOnAction(e -> showEditProfileDialog());
        
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");
        changePasswordBtn.setOnAction(e -> showChangePasswordDialog());
        
        buttonBox.getChildren().addAll(editProfileBtn, changePasswordBtn);
        
        // Add components to profile card
        profileCard.getChildren().addAll(titleLabel, avatarPane, infoGrid, buttonBox);
        
        // Center the profile card vertically and horizontally
        StackPane centerPane = new StackPane(profileCard);
        centerPane.setPadding(new Insets(20, 0, 0, 0)); // Reduced top padding to move it higher (from 50 to 20)
        
        container.getChildren().add(centerPane);
        
        return container;
    }
    
    /**
     * Adds a field to the profile information grid
     * 
     * @param grid the GridPane to add the field to
     * @param row the row index for the field
     * @param label the label text for the field
     * @param value the value text for the field
     */
    private void addProfileField(GridPane grid, int row, String label, String value) {
        Label fieldLabel = new Label(label + ":");
        fieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label fieldValue = new Label(value != null ? value : "Not provided");
        fieldValue.setStyle("-fx-font-size: 14px;");
        
        grid.add(fieldLabel, 0, row);
        grid.add(fieldValue, 1, row);
    }
    
    /**
     * Shows a dialog for changing the user's password
     * Validates current password and ensures new password confirmation matches
     */
    private void showChangePasswordDialog() {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Change Password");
            dialogStage.setMinWidth(400);
            
            // Set the icon
            try {
                dialogStage.getIcons().add(new Image("file:src/com/images/icon.png"));
            } catch (Exception e) {
                System.err.println("Could not load icon: " + e.getMessage());
            }
            
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white;");
            
            Label headerLabel = new Label("Change Password");
            headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            Label currentPasswordLabel = new Label("Current Password");
            currentPasswordLabel.setStyle("-fx-font-weight: bold;");
            PasswordField currentPasswordField = new PasswordField();
            
            Label newPasswordLabel = new Label("New Password");
            newPasswordLabel.setStyle("-fx-font-weight: bold;");
            PasswordField newPasswordField = new PasswordField();
            
            Label confirmPasswordLabel = new Label("Confirm New Password");
            confirmPasswordLabel.setStyle("-fx-font-weight: bold;");
            PasswordField confirmPasswordField = new PasswordField();
            
            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(false);
            
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            cancelBtn.setOnAction(e -> dialogStage.close());
            
            Button saveBtn = new Button("Save Changes");
            saveBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
            saveBtn.setOnAction(e -> {
                String currentPassword = currentPasswordField.getText();
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                
                // Clear previous error message
                errorLabel.setVisible(false);
                
                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    errorLabel.setText("All fields are required");
                    errorLabel.setVisible(true);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    errorLabel.setText("New password and confirmation do not match");
                    errorLabel.setVisible(true);
                    return;
                }
                
                try {
                    // Verify current password with database
                    boolean passwordMatches = userService.verifyPassword(instructor.getUserId(), currentPassword);
                    
                    if (!passwordMatches) {
                        errorLabel.setText("Current password is incorrect");
                        errorLabel.setVisible(true);
                        return;
                    }
                    
                    // Update password in database
                    boolean success = userService.changePassword(instructor.getUserId(), newPassword);
                    if (success) {
                        // Update local instructor object
                        instructor.setPassword(newPassword);
                        UIUtils.showSuccessAlert("Success", "Password changed successfully");
                        dialogStage.close();
                    } else {
                        errorLabel.setText("Failed to update password. Please try again.");
                        errorLabel.setVisible(true);
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Error: " + ex.getMessage());
                    errorLabel.setVisible(true);
                    ex.printStackTrace();
                }
            });
            
            buttonBox.getChildren().addAll(cancelBtn, saveBtn);
            
            content.getChildren().addAll(
                headerLabel,
                currentPasswordLabel, currentPasswordField,
                newPasswordLabel, newPasswordField,
                confirmPasswordLabel, confirmPasswordField,
                errorLabel,
                buttonBox
            );
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not open change password dialog: " + e.getMessage());
        }
    }
    
    private void showEditProfileDialog() {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Edit Profile");
            dialogStage.setMinWidth(400);
            
            // Set the icon
            try {
                dialogStage.getIcons().add(new Image("file:src/com/images/icon.png"));
            } catch (Exception e) {
                System.err.println("Could not load icon: " + e.getMessage());
            }
            
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white;");
            
            Label headerLabel = new Label("Edit Profile");
            headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            Label firstNameLabel = new Label("First Name");
            firstNameLabel.setStyle("-fx-font-weight: bold;");
            TextField firstNameField = new TextField(instructor.getFirstName());
            
            Label lastNameLabel = new Label("Last Name");
            lastNameLabel.setStyle("-fx-font-weight: bold;");
            TextField lastNameField = new TextField(instructor.getLastName());
            
            Label emailLabel = new Label("Email");
            emailLabel.setStyle("-fx-font-weight: bold;");
            TextField emailField = new TextField(instructor.getEmail());
            
            Label phoneLabel = new Label("Phone");
            phoneLabel.setStyle("-fx-font-weight: bold;");
            TextField phoneField = new TextField(instructor.getPhoneNumber());
            
            Label institutionLabel = new Label("Institution");
            institutionLabel.setStyle("-fx-font-weight: bold;");
            TextField institutionField = new TextField(instructor.getInstitution());
            
            Label departmentLabel = new Label("Department");
            departmentLabel.setStyle("-fx-font-weight: bold;");
            TextField departmentField = new TextField(instructor.getDepartment());
            
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            cancelBtn.setOnAction(e -> dialogStage.close());
            
            Button saveBtn = new Button("Save Changes");
            saveBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
            saveBtn.setOnAction(e -> {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String institution = institutionField.getText().trim();
                String department = departmentField.getText().trim();
                
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    UIUtils.showErrorAlert("Error", "First name, last name, and email are required");
                    return;
                }
                
                try {
                    // Update instructor object with new values
                    Instructor updatedInstructor = new Instructor();
                    updatedInstructor.setUserId(instructor.getUserId());
                    updatedInstructor.setFirstName(firstName);
                    updatedInstructor.setLastName(lastName);
                    updatedInstructor.setEmail(email);
                    updatedInstructor.setPhoneNumber(phone);
                    updatedInstructor.setInstitution(institution);
                    updatedInstructor.setDepartment(department);
                    updatedInstructor.setUsername(instructor.getUsername());
                    updatedInstructor.setPassword(instructor.getPassword());
                    updatedInstructor.setAccountType(instructor.getAccountType());
                    
                    // Update in database
                    boolean success = userService.updateUser(updatedInstructor);
                    
                    if (success) {
                        // Update local instructor object
                        instructor.setFirstName(firstName);
                        instructor.setLastName(lastName);
                        instructor.setEmail(email);
                        instructor.setPhoneNumber(phone);
                        instructor.setInstitution(institution);
                        instructor.setDepartment(department);
                        
                        UIUtils.showSuccessAlert("Success", "Profile updated successfully");
                        dialogStage.close();
                        
                        // Refresh the profile view
                        onNavigatedTo();
                        
                        // Update the initials in the dashboard
                        dashboard.updateUserInitials();
                    } else {
                        UIUtils.showErrorAlert("Error", "Failed to update profile");
                    }
                } catch (Exception ex) {
                    UIUtils.showErrorAlert("Error", "An error occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            buttonBox.getChildren().addAll(cancelBtn, saveBtn);
            
            content.getChildren().addAll(
                headerLabel,
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                institutionLabel, institutionField,
                departmentLabel, departmentField,
                buttonBox
            );
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not open edit profile dialog: " + e.getMessage());
        }
    }
} 