package com.panotify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
<<<<<<< HEAD

import java.io.IOException;
=======
>>>>>>> branch 'main' of git@github.com:victorioso-daniel-2/panotify.git

public class Main extends Application {

    @Override
<<<<<<< HEAD
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("PaNotify! - Online Examination System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }
=======
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
        
        // Create the scene
        Scene scene = new Scene(root);
        
        // Set up the stage
        primaryStage.setTitle("PaNotify! - Online Examination System");
        primaryStage.setScene(scene);
        
        // Optional: Set application icon
        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/panotify/resources/images/app_icon.png")));
        
        // Set stage properties
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        
        // Show the stage
        primaryStage.show();
>>>>>>> branch 'main' of git@github.com:victorioso-daniel-2/panotify.git
    }

    public static void main(String[] args) {
        launch(args);
    }
}
