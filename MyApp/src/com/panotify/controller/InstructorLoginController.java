package com.panotify.controller;

import com.panotify.dao.UserDAO;
import com.panotify.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InstructorLoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;
    
    @FXML
    private void handleForgotPassword(ActionEvent event) {
    }

    @FXML
    private void handleRegister(ActionEvent event) {
    }

    @FXML
    private void handleHelp(ActionEvent event) {
    }

    @FXML
    private void handleAbout(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
    }

    /**
     * Handles login button click event.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both username and password.");
            return;
        }

        try {
            User user = userDAO.authenticateUser(username, password);

            if (user != null && "Instructor".equalsIgnoreCase(user.getAccountType())) {
                showAlert("Login Successful", "Welcome, " + user.getFullName() + "!");
                navigateToInstructorDashboard(event, user);
            } else {
                showAlert("Login Failed", "Invalid instructor credentials.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    /**
     * Navigates to the instructor dashboard after successful login.
     */
    private void navigateToInstructorDashboard(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/panotify/view/InstructorDashboard.fxml"));
            Parent root = loader.load();

            // If your InstructorDashboardController requires user data, uncomment below:
            // InstructorDashboardController controller = loader.getController();
            // controller.initData(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Instructor Dashboard - PaNotify!");
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load dashboard: " + e.getMessage());
        }
    }

    /**
     * Handles the back button to return to the landing page.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("PaNotify! - Online Examination System");
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not return to landing page: " + e.getMessage());
        }
    }

    /**
     * Utility method to show alert dialogs.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}