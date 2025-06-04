package com.panotify;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.panotify.service.AuthenticationService;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.service.UserService;
import com.panotify.model.User;
import com.panotify.model.Student;
import com.panotify.model.Instructor;
import com.panotify.ui.LoginView;
import com.panotify.ui.student.StudentDashboard;
import com.panotify.ui.instructor.InstructorDashboard;
import com.panotify.ui.UIUtils;

/**
 * Main application class for PaNotify - an educational application for creating and managing online examinations.
 * This class serves as the entry point and manages the primary application flow, including:
 * <ul>
 *   <li>Application initialization</li>
 *   <li>Welcome screen display</li>
 *   <li>User authentication (student and instructor)</li>
 *   <li>Dashboard navigation</li>
 * </ul>
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Main extends Application {
    /** The primary stage for the application UI */
    private Stage primaryStage;
    
    /** Service for user-related operations */
    private UserService userService;
    
    /** Service for authentication operations */
    private AuthenticationService authService;
    
    /** Service for course-related operations */
    private CourseService courseService;
    
    /** Service for exam-related operations */
    private ExamService examService;
    
    /** Currently authenticated user */
    private User currentUser;

    /**
     * Initializes the application, sets up services, and displays the welcome screen.
     * 
     * @param primaryStage The primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.userService = new UserService();
            this.authService = new AuthenticationService();
            this.courseService = new CourseService();
            this.examService = new ExamService();
            
            try {
                Image icon = new Image("file:src/com/images/icon.png");
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Error loading application icon: " + e.getMessage());
            }
            
            showWelcomeScreen();
            
            primaryStage.setTitle("PaNotify");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the welcome screen with options for student and instructor login.
     * Sets up the UI layout, background, and navigation buttons.
     */
    private void showWelcomeScreen() {
        VBox mainLayout = new VBox();
        mainLayout.setAlignment(Pos.BOTTOM_LEFT);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setSpacing(10);

        try {
            BackgroundImage bgImage = new BackgroundImage(
                new Image("file:src/com/images/bg_page.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            mainLayout.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            mainLayout.setStyle("-fx-background-color: #f0f0f0;");
        }

        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT);
        buttonContainer.setPadding(new Insets(60, 0, 160, 150));

        Button studentLoginBtn = UIUtils.createStyledButton("Student", "#00FF7F");
        studentLoginBtn.setStyle(studentLoginBtn.getStyle() + "-fx-font-size: 24px; -fx-font-weight: 500; -fx-min-width: 350px; -fx-text-fill: #474344;");
        
        Button instructorLoginBtn = UIUtils.createStyledButton("Instructor", "#FFD700");
        instructorLoginBtn.setStyle(instructorLoginBtn.getStyle() + "-fx-font-size: 24px; -fx-font-weight: 500; -fx-min-width: 350px; -fx-text-fill: #474344;");
        
        buttonContainer.getChildren().addAll(studentLoginBtn, instructorLoginBtn);
        
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(buttonContainer);
        StackPane.setAlignment(buttonContainer, Pos.BOTTOM_LEFT);

        mainLayout.getChildren().add(stackPane);
        
        Scene scene = new Scene(mainLayout, 1200, 700);

        studentLoginBtn.setOnAction(e -> showStudentLogin(scene));
        instructorLoginBtn.setOnAction(e -> showInstructorLogin(scene));

        primaryStage.setScene(scene);
    }

    /**
     * Displays the student login screen.
     * 
     * @param previousScene The previous scene to return to if needed
     */
    private void showStudentLogin(Scene previousScene) {
        LoginView loginView = new LoginView(primaryStage, authService, previousScene, user -> {
            if (user instanceof Student) {
                this.currentUser = user;
                UIUtils.showSuccessAlert("Login Successful", "Welcome, " + user.getFirstName() + "!");
                showStudentDashboard((Student) user);
            }
        });
        loginView.show("Student", 1200, 700);
    }

    /**
     * Displays the instructor login screen.
     * 
     * @param previousScene The previous scene to return to if needed
     */
    private void showInstructorLogin(Scene previousScene) {
        LoginView loginView = new LoginView(primaryStage, authService, previousScene, user -> {
            if (user instanceof Instructor) {
                this.currentUser = user;
                UIUtils.showSuccessAlert("Login Successful", "Welcome, " + user.getFirstName() + "!");
                showInstructorDashboard((Instructor) user);
            }
        });
        loginView.show("Instructor", 1200, 700);
    }

    /**
     * Displays the student dashboard after successful authentication.
     * 
     * @param student The authenticated student
     */
    private void showStudentDashboard(Student student) {
        StudentDashboard dashboard = new StudentDashboard(
            primaryStage, student, userService, courseService, examService);
        dashboard.setOnLogout(() -> showWelcomeScreen());
        dashboard.show();
    }

    /**
     * Displays the instructor dashboard after successful authentication.
     * 
     * @param instructor The authenticated instructor
     */
    private void showInstructorDashboard(Instructor instructor) {
        InstructorDashboard dashboard = new InstructorDashboard(primaryStage, instructor, userService, courseService, examService);
        dashboard.setOnLogout(() -> showWelcomeScreen());
        dashboard.show();
    }

    /**
     * Application entry point.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
} 