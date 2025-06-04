package com.panotify.ui.instructor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.ArrayList;
import java.util.List;

import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.service.UserService;
import com.panotify.model.Instructor;
import com.panotify.ui.UIUtils;

/**
 * Main dashboard interface for instructor users
 * Provides navigation and content area for various instructor functions
 * such as course management, exam creation, and viewing student results
 */
public class InstructorDashboard {
    private Stage primaryStage;
    private Instructor instructor;
    private UserService userService;
    private CourseService courseService;
    private ExamService examService;
    
    // UI components that need class-level access
    private VBox sidebar;
    private StackPane contentStack;
    private HBox userInitialsContainer;
    
    // Tab content components
    private CoursesTab coursesTab;
    private ExamTab examTab;
    private StudentResultsTab resultsTab;
    private ProfileTab profileTab;
    
    // Callback for handling logout action
    private Runnable logoutHandler;
    
    /**
     * Creates a new InstructorDashboard with the necessary services and user data
     * 
     * @param primaryStage the JavaFX stage for displaying the dashboard
     * @param instructor the logged-in instructor user
     * @param userService service for user-related operations
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     */
    public InstructorDashboard(Stage primaryStage, Instructor instructor, UserService userService, 
                              CourseService courseService, ExamService examService) {
        this.primaryStage = primaryStage;
        this.instructor = instructor;
        this.userService = userService;
        this.courseService = courseService;
        this.examService = examService;
        
        // Initialize tab components
        this.coursesTab = new CoursesTab(instructor, userService, courseService, examService, this);
        this.examTab = new ExamTab(instructor, courseService, examService, this);
        this.resultsTab = new StudentResultsTab(instructor, userService, courseService, examService, this);
        this.profileTab = new ProfileTab(instructor, userService, courseService, this);
    }
    
    /**
     * Builds and displays the dashboard UI
     * Creates the layout, navigation elements, and content areas
     */
    public void show() {
        // Create dashboard layout
        BorderPane dashboardLayout = new BorderPane();
        
        // Set background image
        try {
            BackgroundImage bgImage = new BackgroundImage(
                new Image("file:src/com/images/bg_2.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            dashboardLayout.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            dashboardLayout.setStyle("-fx-background-color: #f0f0f0;");
        }
        
        // Create top navigation bar
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: rgba(220, 220, 220, 0.9);");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // Create two separate labels for "Instructor" and "Dashboard"
        Label instructorLabel = new Label("Instructor");
        instructorLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: black; -fx-font-weight: bold;");
        
        Label dashboardLabel = new Label("Dashboard");
        dashboardLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #00d355; -fx-font-weight: bold;");
        
        // Create a container for the title labels to make them clickable together
        HBox titleContainer = new HBox(5);
        titleContainer.getChildren().addAll(instructorLabel, dashboardLabel);
        titleContainer.setCursor(javafx.scene.Cursor.HAND);
        
        // Add click handler to return to courses tab and show sidebar
        titleContainer.setOnMouseClicked(e -> {
            showSidebar();
            navigateToMyCourses();
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Create user initials with different colors
        String firstInitial = "";
        String secondInitial = "";
        
        if (instructor.getFirstName() != null && !instructor.getFirstName().isEmpty()) {
            firstInitial = instructor.getFirstName().substring(0, 1);
        }
        if (instructor.getLastName() != null && !instructor.getLastName().isEmpty()) {
            secondInitial = instructor.getLastName().substring(0, 1);
        }
        
        Text firstInitialText = new Text(firstInitial);
        firstInitialText.setStyle("-fx-font-size: 28px; -fx-fill: black; -fx-font-weight: bold;");
        
        Text secondInitialText = new Text(secondInitial);
        secondInitialText.setStyle("-fx-font-size: 28px; -fx-fill: #00d355; -fx-font-weight: bold;");
        
        TextFlow initialsFlow = new TextFlow(firstInitialText, secondInitialText);
        initialsFlow.setCursor(javafx.scene.Cursor.HAND);
        initialsFlow.setOnMouseClicked(e -> navigateToProfile());
        
        // Create user initials container
        userInitialsContainer = new HBox();
        userInitialsContainer.getChildren().add(initialsFlow);
        userInitialsContainer.setCursor(javafx.scene.Cursor.HAND);
        userInitialsContainer.setOnMouseClicked(e -> navigateToProfile());
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 16px; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> logout());
        
        topBar.getChildren().addAll(titleContainer, spacer, userInitialsContainer, logoutButton);
        dashboardLayout.setTop(topBar);
        
        // Create sidebar menu
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: rgba(0, 211, 85, 0.5); -fx-background-radius: 10;");
        sidebar.setPrefWidth(250);
        BorderPane.setMargin(sidebar, new Insets(30, 10, 10, 10));
        
        Button myCourses = UIUtils.createMenuButton("My Courses");
        myCourses.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        myCourses.setOnMouseEntered(e -> myCourses.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        myCourses.setOnMouseExited(e -> myCourses.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        Button createExam = UIUtils.createMenuButton("Create Exam");
        createExam.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        createExam.setOnMouseEntered(e -> createExam.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        createExam.setOnMouseExited(e -> createExam.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        Button studentResults = UIUtils.createMenuButton("Student Results");
        studentResults.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        studentResults.setOnMouseEntered(e -> studentResults.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        studentResults.setOnMouseExited(e -> studentResults.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        // Only add the three main tabs to sidebar (removed Profile tab)
        sidebar.getChildren().addAll(myCourses, createExam, studentResults);
        dashboardLayout.setLeft(sidebar);
        
        // Create content stack for switching between tabs
        contentStack = new StackPane();
        contentStack.getChildren().addAll(
            coursesTab.getContent(),
            examTab.getContent(),
            resultsTab.getContent(),
            profileTab.getContent()
        );
        
        // Set initial visibility - start with My Courses tab visible
        coursesTab.getContent().setVisible(true);
        examTab.getContent().setVisible(false);
        resultsTab.getContent().setVisible(false);
        profileTab.getContent().setVisible(false);
        
        // Ensure the courses tab is refreshed on startup
        coursesTab.onNavigatedTo();
        
        // Create a wrapper pane to center the content
        StackPane contentWrapper = new StackPane(contentStack);
        contentWrapper.setPadding(new Insets(20));
        BorderPane.setMargin(contentWrapper, new Insets(10, 10, 10, 0));
        dashboardLayout.setCenter(contentWrapper);
        
        // Create scene and set to stage
        Scene scene = new Scene(dashboardLayout, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PaNotify - Instructor Dashboard");
        primaryStage.show();

        // Add event handlers for menu buttons
        myCourses.setOnAction(e -> navigateToTab(coursesTab));
        createExam.setOnAction(e -> navigateToTab(examTab));
        studentResults.setOnAction(e -> navigateToTab(resultsTab));
    }
    
    /**
     * Navigates to a specific tab in the dashboard
     * Hides all other content and displays the selected tab
     * 
     * @param tab the tab to navigate to
     */
    private void navigateToTab(InstructorTab tab) {
        // Hide all content panes
        List<Node> nodes = new ArrayList<>(contentStack.getChildren());
        for (Node node : nodes) {
            node.setVisible(false);
        }
        
        // Show the selected tab's content
        tab.getContent().setVisible(true);
        tab.onNavigatedTo();
    }
    
    /**
     * Navigate directly to the My Courses tab
     * Used as a convenience method for direct navigation
     */
    public void navigateToMyCourses() {
        if (coursesTab != null) {
            navigateToTab(coursesTab);
        }
    }
    
    /**
     * Navigate directly to the Create Exam tab
     * Used as a convenience method for direct navigation
     */
    public void navigateToCreateExam() {
        navigateToTab(examTab);
    }
    
    /**
     * Navigate directly to the Student Results tab
     * Used as a convenience method for direct navigation
     */
    public void navigateToStudentResults() {
        navigateToTab(resultsTab);
    }
    
    /**
     * Navigate directly to the Profile tab
     * Used as a convenience method for direct navigation
     */
    public void navigateToProfile() {
        navigateToTab(profileTab);
    }
    
    /**
     * Handles the logout process
     * Calls the registered logout handler callback
     */
    private void logout() {
        Platform.runLater(() -> {
            if (logoutHandler != null) {
                logoutHandler.run();
            }
        });
    }
    
    /**
     * Sets a callback handler for logout action
     * 
     * @param handler the runnable to execute when logging out
     */
    public void setOnLogout(Runnable handler) {
        this.logoutHandler = handler;
    }
    
    /**
     * Gets the primary stage for this dashboard
     * 
     * @return the JavaFX stage for this dashboard
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Gets the current instructor user
     * 
     * @return the instructor object for the logged-in user
     */
    public Instructor getInstructor() {
        return instructor;
    }
    
    /**
     * Gets the user service instance
     * 
     * @return the user service for user-related operations
     */
    public UserService getUserService() {
        return userService;
    }
    
    /**
     * Gets the course service instance
     * 
     * @return the course service for course-related operations
     */
    public CourseService getCourseService() {
        return courseService;
    }
    
    /**
     * Gets the exam service instance
     * 
     * @return the exam service for exam-related operations
     */
    public ExamService getExamService() {
        return examService;
    }
    
    /**
     * Gets the exam tab instance
     * 
     * @return the exam tab component
     */
    public ExamTab getExamTab() {
        return examTab;
    }
    
    /**
     * Hides the sidebar menu
     * Used to provide more screen space for content when needed
     */
    public void hideSidebar() {
        if (sidebar != null && sidebar.getParent() != null) {
            BorderPane parent = (BorderPane) sidebar.getParent();
            parent.setLeft(null);
        }
    }
    
    /**
     * Shows the sidebar menu
     * Restores the sidebar after it has been hidden
     */
    public void showSidebar() {
        if (sidebar != null && sidebar.getParent() == null) {
            // Find the BorderPane
            Scene scene = primaryStage.getScene();
            if (scene != null && scene.getRoot() instanceof BorderPane) {
                BorderPane root = (BorderPane) scene.getRoot();
                root.setLeft(sidebar);
                BorderPane.setMargin(sidebar, new Insets(30, 10, 10, 10));
            }
        }
    }
    
    /**
     * Updates the user initials display in the UI
     * Called when user information is changed
     */
    public void updateUserInitials() {
        if (userInitialsContainer != null) {
            userInitialsContainer.getChildren().clear();
            
            String firstInitial = "";
            String secondInitial = "";
            
            if (instructor.getFirstName() != null && !instructor.getFirstName().isEmpty()) {
                firstInitial = instructor.getFirstName().substring(0, 1);
            }
            if (instructor.getLastName() != null && !instructor.getLastName().isEmpty()) {
                secondInitial = instructor.getLastName().substring(0, 1);
            }
            
            Text firstInitialText = new Text(firstInitial);
            firstInitialText.setStyle("-fx-font-size: 28px; -fx-fill: black; -fx-font-weight: bold;");
            
            Text secondInitialText = new Text(secondInitial);
            secondInitialText.setStyle("-fx-font-size: 28px; -fx-fill: #00d355; -fx-font-weight: bold;");
            
            TextFlow initialsFlow = new TextFlow(firstInitialText, secondInitialText);
            initialsFlow.setCursor(javafx.scene.Cursor.HAND);
            initialsFlow.setOnMouseClicked(e -> navigateToProfile());
            
            userInitialsContainer.getChildren().add(initialsFlow);
        }
    }
} 