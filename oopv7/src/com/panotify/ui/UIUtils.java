package com.panotify.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;

/**
 * Utility class providing common UI methods used across the application
 */
public class UIUtils {
    
    /**
     * Shows a success alert dialog
     * 
     * @param title the title of the alert
     * @param message the message to display
     */
    public static void showSuccessAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Shows an error alert dialog
     * 
     * @param title the title of the alert
     * @param message the message to display
     */
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows an info alert dialog
     * 
     * @param title the title of the alert
     * @param message the message to display
     */
    public static void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog and returns whether the user confirmed
     * 
     * @param title the title of the alert
     * @param message the message to display
     * @return true if the user confirmed, false otherwise
     */
    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Shows a confirmation dialog with custom button text
     * 
     * @param title the title of the alert
     * @param message the message to display
     * @param confirmText the text for the confirm button
     * @param cancelText the text for the cancel button
     * @return true if the user confirmed, false otherwise
     */
    public static boolean showConfirmationDialog(String title, String message, String confirmText, String cancelText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Set custom button types
        ButtonType confirmButtonType = new ButtonType(confirmText);
        ButtonType cancelButtonType = new ButtonType(cancelText);
        alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButtonType;
    }
    
    /**
     * Creates a styled button with custom background color
     * 
     * @param text the text to display on the button
     * @param color the background color as a hex string (e.g., "#00FF7F")
     * @return the styled button
     */
    public static Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 0);"
        );
        button.setPrefWidth(200);
        return button;
    }
    
    /**
     * Creates a menu button with hover effects
     * 
     * @param text the text to display on the button
     * @return the menu button
     */
    public static Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-padding: 10 20;"
        );
        button.setPrefWidth(200);
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: CENTER_LEFT;" +
                "-fx-padding: 10 20;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: CENTER_LEFT;" +
                "-fx-padding: 10 20;"
            )
        );
        
        return button;
    }
    
    /**
     * Shows a custom non-modal confirmation dialog that doesn't close parent dialogs
     * @param title The title of the dialog
     * @param message The message to display
     * @param confirmButtonText Text for the confirm button
     * @param cancelButtonText Text for the cancel button
     * @return true if confirmed, false otherwise
     */
    public static boolean showNonModalConfirmation(String title, String message, String confirmButtonText, String cancelButtonText) {
        // Create a new stage for our custom dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Set to APPLICATION_MODAL to block only this window
        
        // Create content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        // Add message
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setMaxWidth(400);
        
        // Add buttons
        Button confirmButton = new Button(confirmButtonText);
        confirmButton.setDefaultButton(true);
        confirmButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        
        Button cancelButton = new Button(cancelButtonText);
        cancelButton.setCancelButton(true);
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        
        content.getChildren().addAll(messageLabel, buttonBox);
        
        // Create scene
        Scene scene = new Scene(content);
        dialogStage.setScene(scene);
        
        // Set up result
        final boolean[] result = new boolean[1];
        result[0] = false; // Default to false (cancel)
        
        // Set up button actions
        confirmButton.setOnAction(e -> {
            result[0] = true;
            dialogStage.close();
        });
        
        cancelButton.setOnAction(e -> {
            result[0] = false;
            dialogStage.close();
        });
        
        // Show and wait
        dialogStage.showAndWait();
        
        return result[0];
    }
} 