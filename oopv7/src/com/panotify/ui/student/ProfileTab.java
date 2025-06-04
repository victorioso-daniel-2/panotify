package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.panotify.model.Student;
import com.panotify.service.UserService;
import com.panotify.ui.UIUtils;

/**
 * Tab for managing student profile information
 * Provides interface for viewing and editing profile details,
 * changing password, and managing account settings
 */
public class ProfileTab implements StudentTab {
    private Student student;
    private UserService userService;
    private StudentDashboard dashboard;
    private VBox content;
    private Stage profileStage;
    
    /**
     * Creates a new ProfileTab with the necessary services and data
     * 
     * @param student the logged-in student user
     * @param userService service for user-related operations
     * @param dashboard reference to the parent dashboard
     */
    public ProfileTab(Student student, UserService userService, StudentDashboard dashboard) {
        this.student = student;
        this.userService = userService;
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
        content.getChildren().add(createProfileContent());
    }
    
    /**
     * Creates the profile content view
     * Sets up the UI with user information and action buttons
     * 
     * @return a VBox containing the profile view elements
     */
    private VBox createProfileContent() {
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
        Label titleLabel = new Label("Student Profile");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        
        // Profile avatar with initials
        StackPane avatarPane = new StackPane();
        avatarPane.setMinSize(100, 100);
        avatarPane.setMaxSize(100, 100);
        
        Circle avatarCircle = new Circle(50);
        avatarCircle.setFill(Color.valueOf("#00d355"));
        
        String initials = "";
        if (student.getFirstName() != null && !student.getFirstName().isEmpty()) {
            initials += student.getFirstName().charAt(0);
        }
        if (student.getLastName() != null && !student.getLastName().isEmpty()) {
            initials += student.getLastName().charAt(0);
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
        
        // Add fields (removed institution field)
        addProfileField(infoGrid, 0, "Full Name", student.getFirstName() + " " + student.getLastName());
        addProfileField(infoGrid, 1, "Username", student.getUsername());
        addProfileField(infoGrid, 2, "Email", student.getEmail());
        addProfileField(infoGrid, 3, "Phone", student.getPhoneNumber());
        
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
        centerPane.setPadding(new Insets(20, 0, 0, 0)); // Reduced top padding to move it higher (from 30 to 20)
        
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
     * Shows a dialog for editing profile information
     * Provides form fields for updating user details
     */
    private void showEditProfileDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your profile information");
        
        // Set the icon
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image("file:src/com/images/icon.png"));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + e.getMessage());
        }
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Create text fields
        TextField firstNameField = new TextField(student.getFirstName());
        TextField lastNameField = new TextField(student.getLastName());
        TextField emailField = new TextField(student.getEmail());
        TextField phoneField = new TextField(student.getPhoneNumber());
        
        // Add labels and fields to grid (removed institution field)
        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the first name field
        firstNameField.requestFocus();
        
        // Convert the result to a student object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Student updatedStudent = new Student();
                updatedStudent.setUserId(student.getUserId());
                updatedStudent.setFirstName(firstNameField.getText());
                updatedStudent.setLastName(lastNameField.getText());
                updatedStudent.setEmail(emailField.getText());
                updatedStudent.setPhoneNumber(phoneField.getText());
                updatedStudent.setUsername(student.getUsername());
                updatedStudent.setPassword(student.getPassword());
                updatedStudent.setAccountType(student.getAccountType());
                updatedStudent.setDepartment(student.getDepartment());
                return updatedStudent;
            }
            return null;
        });
        
        // Process the result
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(updatedStudent -> {
            try {
                boolean success = userService.updateUser(updatedStudent);
                if (success) {
                    // Update the local student object
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setPhoneNumber(updatedStudent.getPhoneNumber());
                    
                    UIUtils.showSuccessAlert("Success", "Profile updated successfully!");
                    
                    // Refresh the profile view
                    onNavigatedTo();
                    
                    // Update the initials in the dashboard
                    dashboard.updateUserInitials();
                } else {
                    UIUtils.showErrorAlert("Error", "Failed to update profile. Please try again.");
                }
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        });
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
                
                // Validate inputs
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
                    boolean passwordMatches = userService.verifyPassword(student.getUserId(), currentPassword);
                    
                    if (!passwordMatches) {
                        errorLabel.setText("Current password is incorrect");
                        errorLabel.setVisible(true);
                        return;
                    }
                    
                    // Update password in database
                    boolean success = userService.changePassword(student.getUserId(), newPassword);
                    if (success) {
                        // Update local student object
                        student.setPassword(newPassword);
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
            UIUtils.showErrorAlert("Error", "Failed to open change password dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 