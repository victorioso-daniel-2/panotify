package com.panotify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}