package com.panotify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Use the correct path to load FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
            Scene scene = new Scene(root);
            
            // Set application title and icon
            primaryStage.setTitle("PaNotify! - Online Examination System");
            // Uncomment and set path to your icon if available
            // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/panotify/resources/images/icon.png")));
            
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}