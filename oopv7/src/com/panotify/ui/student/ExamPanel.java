package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.panotify.model.Exam;
import com.panotify.model.Question;
import com.panotify.model.StudentAnswer;
import com.panotify.service.ExamService;
import com.panotify.ui.UIUtils;

/**
 * Panel for displaying and taking an exam in the main content area
 * Provides interface for viewing exam questions, selecting answers,
 * and submitting completed exams
 */
public class ExamPanel {
    private ExamsTab parentTab;
    private Exam exam;
    private List<Question> questions;
    private ExamService examService;
    private int studentId;
    private Map<Integer, Object> answers = new HashMap<>();
    
    /**
     * Creates a new ExamPanel with the necessary data for taking an exam
     * 
     * @param parentTab the parent ExamsTab that created this panel
     * @param exam the exam to be taken
     * @param examService service for exam-related operations
     * @param studentId the ID of the student taking the exam
     */
    public ExamPanel(ExamsTab parentTab, Exam exam, ExamService examService, int studentId) {
        this.parentTab = parentTab;
        this.exam = exam;
        this.examService = examService;
        this.studentId = studentId;
    }
    
    /**
     * Creates and returns the exam content UI
     * Sets up the UI with exam information, questions, and submit button
     * 
     * @return the VBox containing the exam panel
     */
    public VBox createContent() {
        try {
            // Get the questions for this exam
            questions = examService.getExamQuestions(exam.getExamId());
            
            if (questions == null || questions.isEmpty()) {
                UIUtils.showErrorAlert("Error", "This exam has no questions. Please contact your instructor.");
                return createErrorPanel("No questions available for this exam.");
            }
            
            // Start the exam in the database
            boolean examStarted = examService.startExam(studentId, exam.getExamId());
            if (!examStarted) {
                UIUtils.showErrorAlert("Error", "Failed to record exam start. Please try again.");
                return createErrorPanel("Failed to start exam. Please try again.");
            }
            
            // Create main container for the exam
            VBox examContainer = new VBox(20);
            examContainer.setPadding(new Insets(20));
            examContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            
            // Header with exam title and time
            HBox headerBox = new HBox(15);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            
            Label titleLabel = new Label("Exam: " + exam.getTitle());
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            Label timeLabel = new Label("Time: " + exam.getDurationMinutes() + " minutes");
            timeLabel.setStyle("-fx-font-size: 16px;");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Button backButton = new Button("Back to Exams");
            backButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            backButton.setOnAction(e -> {
                boolean confirmExit = UIUtils.showConfirmationDialog(
                    "Exit Exam", 
                    "Are you sure you want to exit this exam? Your answers will not be saved.",
                    "Exit Exam", "Continue Exam"
                );
                
                if (confirmExit) {
                    parentTab.onNavigatedTo(); // Return to the exams list
                }
            });
            
            headerBox.getChildren().addAll(titleLabel, spacer, timeLabel, backButton);
            
            // Instructions
            Label instructionsLabel = new Label("Answer all questions and click 'Submit Exam' when done.");
            instructionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            instructionsLabel.setWrapText(true);
            
            // Scrollable container for questions
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            
            VBox questionsBox = new VBox(20);
            questionsBox.setPadding(new Insets(20));
            
            // Create a panel for each question
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                
                VBox questionBox = new VBox(10);
                questionBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 5; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
                
                Label questionLabel = new Label((i + 1) + ". " + question.getQuestionText());
                questionLabel.setWrapText(true);
                questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                
                questionBox.getChildren().add(questionLabel);
                
                // Add points info
                Label pointsLabel = new Label("(" + question.getPoints() + " points)");
                pointsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6c757d;");
                questionBox.getChildren().add(pointsLabel);
                
                // Process options
                if (question.getOptions() != null && question.getOptions().contains("|")) {
                    // Multiple choice question
                    ToggleGroup optionsGroup = new ToggleGroup();
                    String[] options = question.getOptions().split("\\|");
                    
                    for (int j = 0; j < options.length; j++) {
                        RadioButton option = new RadioButton(options[j]);
                        option.setToggleGroup(optionsGroup);
                        option.setWrapText(true);
                        option.setUserData(j); // Store option index
                        questionBox.getChildren().add(option);
                        
                        // Add change listener to store answer
                        final int optionIndex = j;
                        option.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                answers.put(question.getQuestionId(), optionIndex);
                            }
                        });
                    }
                } else {
                    // Identification question
                    Label answerInstructionLabel = new Label("Type your answer below:");
                    answerInstructionLabel.setStyle("-fx-font-style: italic;");
                    
                    TextField answerField = new TextField();
                    answerField.setPromptText("Enter your answer here");
                    answerField.setPrefWidth(600);
                    
                    // Add listener to store answer
                    answerField.textProperty().addListener((obs, oldVal, newVal) -> {
                        answers.put(question.getQuestionId(), newVal.trim());
                    });
                    
                    questionBox.getChildren().addAll(answerInstructionLabel, answerField);
                }
                
                questionsBox.getChildren().add(questionBox);
            }
            
            scrollPane.setContent(questionsBox);
            scrollPane.setPrefHeight(400);
            
            // Submit button area
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setPadding(new Insets(10, 0, 0, 0));
            
            Button submitButton = new Button("Submit Exam");
            submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px;");
            submitButton.setPrefWidth(150);
            submitButton.setPrefHeight(40);
            
            submitButton.setOnAction(e -> {
                // Show confirmation dialog
                String message = "Are you sure you want to submit this exam?\n" +
                                "Once submitted, you cannot make changes.\n\n" +
                                "Exam: " + exam.getTitle() + "\n" +
                                "Questions Answered: " + answers.size() + "/" + questions.size();
                
                boolean confirm = UIUtils.showConfirmationDialog(
                    "Confirm Submission",
                    message,
                    "Submit Exam",
                    "Review Answers"
                );
                
                if (confirm) {
                    submitExam();
                }
                // If not confirmed, just stay on the exam page
            });
            
            buttonBox.getChildren().add(submitButton);
            
            // Add all components to the exam container
            examContainer.getChildren().addAll(headerBox, instructionsLabel, scrollPane, buttonBox);
            
            return examContainer;
            
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not start exam: " + e.getMessage());
            e.printStackTrace();
            return createErrorPanel("An error occurred while loading the exam.");
        }
    }
    
    /**
     * Creates an error panel when the exam cannot be loaded or started
     * Displays an error message and provides a back button
     * 
     * @param message the error message to display
     * @return a VBox containing the error panel
     */
    private VBox createErrorPanel(String message) {
        VBox errorBox = new VBox(20);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(50));
        errorBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #dc3545;");
        
        Button backButton = new Button("Back to Exams");
        backButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        backButton.setOnAction(e -> parentTab.onNavigatedTo());
        
        errorBox.getChildren().addAll(errorLabel, backButton);
        
        return errorBox;
    }
    
    /**
     * Handles the exam submission process
     * Gathers answers, submits them to the server, and navigates to results
     */
    private void submitExam() {
        try {
            // Convert answers to the expected format
            Map<Integer, String> formattedAnswers = new HashMap<>();
            for (Map.Entry<Integer, Object> entry : answers.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    formattedAnswers.put(entry.getKey(), entry.getValue().toString());
                } else if (entry.getValue() instanceof String) {
                    formattedAnswers.put(entry.getKey(), (String) entry.getValue());
                }
            }
            
            // Submit each answer
            boolean allAnswersSubmitted = true;
            for (Question question : questions) {
                String answer = formattedAnswers.getOrDefault(question.getQuestionId(), "");
                
                // Check if answer is empty
                if (answer.isEmpty()) {
                    allAnswersSubmitted = false;
                    continue;
                }
                
                // Determine if the answer is correct
                boolean isCorrect = false;
                if (question.getOptions() != null && question.getOptions().contains("|")) {
                    // Multiple choice
                    try {
                        int selectedOption = Integer.parseInt(answer);
                        isCorrect = (selectedOption == question.getCorrectOption());
                    } catch (NumberFormatException e) {
                        // Invalid answer format
                        isCorrect = false;
                    }
                } else {
                    // Identification - case insensitive match
                    String correctAnswer = question.getCorrectAnswer().trim();
                    String studentAnswer = answer.trim();
                    isCorrect = studentAnswer.equalsIgnoreCase(correctAnswer);
                }
                
                // Create and submit the answer
                StudentAnswer studentAnswer = new StudentAnswer(
                    studentId,
                    question.getQuestionId(),
                    answer
                );
                studentAnswer.setCorrect(isCorrect);
                studentAnswer.setSubmittedAt(LocalDateTime.now());
                
                boolean answerSubmitted = examService.submitAnswer(studentAnswer);
                if (!answerSubmitted) {
                    allAnswersSubmitted = false;
                }
            }
            
            // Complete the exam
            boolean examCompleted = examService.submitExam(studentId, exam.getExamId());
            
            if (examCompleted && allAnswersSubmitted) {
                // Grade the exam
                boolean graded = examService.gradeExam(studentId, exam.getExamId());
                
                if (graded) {
                    UIUtils.showSuccessAlert("Exam Submitted", "Your exam has been submitted and graded successfully!");
                } else {
                    UIUtils.showSuccessAlert("Exam Submitted", "Your exam has been submitted successfully, but there was an issue with grading.");
                }
            } else {
                UIUtils.showErrorAlert("Error", "There was an issue submitting some of your answers. Please try again.");
            }
            
            // Return to the exams list
            parentTab.onNavigatedTo();
            
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not submit exam: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 