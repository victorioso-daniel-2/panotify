package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.ArrayList;
import java.util.List;

import com.panotify.model.Student;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.service.UserService;
import com.panotify.ui.UIUtils;

/**
 * Main dashboard for students
 * Manages navigation between different tabs
 */
public class StudentDashboard {
    private Stage primaryStage;
    private Student student;
    private UserService userService;
    private CourseService courseService;
    private ExamService examService;
    
    private VBox sidebar;
    private StackPane contentStack;
    private HBox userInitialsContainer;
    
    // Tab components
    private CoursesTab coursesTab;
    private ExamsTab examsTab;
    private ResultsTab resultsTab;
    private ProfileTab profileTab;
    
    private Runnable logoutHandler;
    
    /**
     * Creates a new StudentDashboard with the necessary services and user data
     * 
     * @param primaryStage the JavaFX stage for displaying the dashboard
     * @param student the logged-in student user
     * @param userService service for user-related operations
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     */
    public StudentDashboard(Stage primaryStage, Student student, UserService userService, 
                          CourseService courseService, ExamService examService) {
        this.primaryStage = primaryStage;
        this.student = student;
        this.userService = userService;
        this.courseService = courseService;
        this.examService = examService;
        
        // Initialize tab components
        this.coursesTab = new CoursesTab(student, courseService, this);
        this.examsTab = new ExamsTab(student, courseService, examService, this);
        this.resultsTab = new ResultsTab(student, examService, this);
        this.profileTab = new ProfileTab(student, userService, this);
    }
    
    /**
     * Display the dashboard
     * Creates and sets up the UI layout with sidebar navigation and content area
     */
    public void show() {
        // Create the main layout
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
        
        // Create two separate labels for "Student" and "Dashboard"
        Label studentLabel = new Label("Student");
        studentLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: black; -fx-font-weight: bold;");
        
        Label dashboardLabel = new Label("Dashboard");
        dashboardLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #00d355; -fx-font-weight: bold;");
        
        // Create a container for the title labels to make them clickable together
        HBox titleContainer = new HBox(5);
        titleContainer.getChildren().addAll(studentLabel, dashboardLabel);
        titleContainer.setCursor(javafx.scene.Cursor.HAND);
        
        // Add click handler to return to courses tab and show sidebar
        titleContainer.setOnMouseClicked(e -> {
            showSidebar();
            navigateToCoursesTab();
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Create user initials with different colors
        String firstInitial = "";
        String secondInitial = "";
        
        if (student.getFirstName() != null && !student.getFirstName().isEmpty()) {
            firstInitial = student.getFirstName().substring(0, 1);
        }
        if (student.getLastName() != null && !student.getLastName().isEmpty()) {
            secondInitial = student.getLastName().substring(0, 1);
        }
        
        Text firstInitialText = new Text(firstInitial);
        firstInitialText.setStyle("-fx-font-size: 28px; -fx-fill: black; -fx-font-weight: bold;");
        
        Text secondInitialText = new Text(secondInitial);
        secondInitialText.setStyle("-fx-font-size: 28px; -fx-fill: #00d355; -fx-font-weight: bold;");
        
        TextFlow initialsFlow = new TextFlow(firstInitialText, secondInitialText);
        initialsFlow.setCursor(javafx.scene.Cursor.HAND);
        initialsFlow.setOnMouseClicked(e -> navigateToTab(profileTab));
        
        // Create user initials container
        userInitialsContainer = new HBox();
        userInitialsContainer.getChildren().add(initialsFlow);
        userInitialsContainer.setCursor(javafx.scene.Cursor.HAND);
        userInitialsContainer.setOnMouseClicked(e -> navigateToTab(profileTab));
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 16px; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> logout());
        
        topBar.getChildren().addAll(titleContainer, spacer, userInitialsContainer, logoutBtn);
        dashboardLayout.setTop(topBar);
        
        // Create sidebar with navigation buttons
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: rgba(0, 211, 85, 0.5); -fx-background-radius: 10;");
        sidebar.setPrefWidth(250);
        BorderPane.setMargin(sidebar, new Insets(30, 10, 10, 10));
        
        Button coursesBtn = UIUtils.createMenuButton("My Courses");
        coursesBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        coursesBtn.setOnMouseEntered(e -> coursesBtn.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        coursesBtn.setOnMouseExited(e -> coursesBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        Button examsBtn = UIUtils.createMenuButton("My Exams");
        examsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        examsBtn.setOnMouseEntered(e -> examsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        examsBtn.setOnMouseExited(e -> examsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        Button resultsBtn = UIUtils.createMenuButton("My Results");
        resultsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        resultsBtn.setOnMouseEntered(e -> resultsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        resultsBtn.setOnMouseExited(e -> resultsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left; -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 5; -fx-border-width: 1; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));
        
        // Add buttons to sidebar
        sidebar.getChildren().addAll(coursesBtn, examsBtn, resultsBtn);
        dashboardLayout.setLeft(sidebar);
        
        // Create content stack for switching between tabs
        contentStack = new StackPane();
        contentStack.getChildren().addAll(
            coursesTab.getContent(),
            examsTab.getContent(),
            resultsTab.getContent(),
            profileTab.getContent()
        );
        
        // Set initial visibility - start with My Courses tab visible
        coursesTab.getContent().setVisible(true);
        examsTab.getContent().setVisible(false);
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
        primaryStage.setTitle("PaNotify - Student Dashboard");
        primaryStage.show();
        
        // Add event handlers for menu buttons
        coursesBtn.setOnAction(e -> navigateToTab(coursesTab));
        examsBtn.setOnAction(e -> navigateToTab(examsTab));
        resultsBtn.setOnAction(e -> navigateToTab(resultsTab));
    }
    
    /**
     * Navigates to a specific tab by updating tab visibility
     * Makes the selected tab visible and hides all other tabs
     * 
     * @param tab the tab to navigate to
     */
    private void navigateToTab(StudentTab tab) {
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
     * Navigates to the Courses tab
     * Shortcut method used by navigation buttons
     */
    public void navigateToMyCourses() {
        navigateToTab(coursesTab);
    }
    
    /**
     * Navigates to the Exams tab
     * Shortcut method used by navigation buttons
     */
    public void navigateToMyExams() {
        navigateToTab(examsTab);
    }
    
    /**
     * Navigates to the Results tab
     * Shortcut method used by navigation buttons
     */
    public void navigateToMyResults() {
        navigateToTab(resultsTab);
    }
    
    /**
     * Navigates to the Profile tab
     * Shortcut method used by navigation buttons and user avatar
     */
    public void navigateToProfile() {
        navigateToTab(profileTab);
    }
    
    /**
     * Navigates to the Courses tab and ensures sidebar is shown
     * Used by the dashboard title/logo click handler
     */
    public void navigateToCoursesTab() {
        if (coursesTab != null) {
            navigateToTab(coursesTab);
        }
    }
    
    /**
     * Handles user logout
     * Calls the registered logout handler if available
     */
    private void logout() {
        if (logoutHandler != null) {
            logoutHandler.run();
        }
    }
    
    /**
     * Sets the handler to be called on user logout
     * 
     * @param handler the runnable to execute on logout
     */
    public void setOnLogout(Runnable handler) {
        this.logoutHandler = handler;
    }
    
    /**
     * Gets the primary stage for this dashboard
     * 
     * @return the primary JavaFX stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Gets the current student user
     * 
     * @return the student user object
     */
    public Student getStudent() {
        return student;
    }
    
    /**
     * Gets the user service instance
     * 
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }
    
    /**
     * Gets the course service instance
     * 
     * @return the course service
     */
    public CourseService getCourseService() {
        return courseService;
    }
    
    /**
     * Gets the exam service instance
     * 
     * @return the exam service
     */
    public ExamService getExamService() {
        return examService;
    }
    
    /**
     * Gets the results tab instance
     * 
     * @return the results tab
     */
    public ResultsTab getResultsTab() {
        return resultsTab;
    }
    
    /**
     * Hides the sidebar navigation
     * Used when displaying full-width content like the profile
     */
    public void hideSidebar() {
        if (sidebar != null) {
            sidebar.setVisible(false);
            sidebar.setManaged(false);
        }
    }
    
    /**
     * Shows the sidebar navigation
     * Restores sidebar visibility after it has been hidden
     */
    public void showSidebar() {
        if (sidebar != null) {
            sidebar.setVisible(true);
            sidebar.setManaged(true);
        }
    }
    
    /**
     * Updates the user initials displayed in the top bar
     * Called after profile changes to reflect updated name
     */
    public void updateUserInitials() {
        if (userInitialsContainer != null && student != null) {
            userInitialsContainer.getChildren().clear();
            
            String firstInitial = "";
            String secondInitial = "";
            
            if (student.getFirstName() != null && !student.getFirstName().isEmpty()) {
                firstInitial = student.getFirstName().substring(0, 1);
            }
            if (student.getLastName() != null && !student.getLastName().isEmpty()) {
                secondInitial = student.getLastName().substring(0, 1);
            }
            
            Text firstInitialText = new Text(firstInitial);
            firstInitialText.setStyle("-fx-font-size: 28px; -fx-fill: black; -fx-font-weight: bold;");
            
            Text secondInitialText = new Text(secondInitial);
            secondInitialText.setStyle("-fx-font-size: 28px; -fx-fill: #00d355; -fx-font-weight: bold;");
            
            TextFlow initialsFlow = new TextFlow(firstInitialText, secondInitialText);
            initialsFlow.setCursor(javafx.scene.Cursor.HAND);
            initialsFlow.setOnMouseClicked(e -> navigateToTab(profileTab));
            
            userInitialsContainer.getChildren().add(initialsFlow);
            userInitialsContainer.setCursor(javafx.scene.Cursor.HAND);
            userInitialsContainer.setOnMouseClicked(e -> navigateToTab(profileTab));
        }
    }
} 