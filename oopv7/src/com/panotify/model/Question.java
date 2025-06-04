package com.panotify.model;

/**
 * Represents a question in an exam within the PaNotify system.
 * Questions can be multiple choice or identification type, with options
 * and correct answers.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Question {
    /** Unique identifier for the question */
    private int questionId;
    
    /** ID of the exam this question belongs to */
    private int examId;
    
    /** The text of the question */
    private String questionText;
    
    /** Multiple options separated by | character */
    private String options;
    
    /** Index of the correct option (0-based) for multiple choice questions */
    private int correctOption;
    
    /** Point value of the question */
    private int points;

    /**
     * Default constructor for Question.
     */
    public Question() {}

    /**
     * Parameterized constructor to create a new question with essential information.
     * 
     * @param examId ID of the exam this question belongs to
     * @param questionText The text of the question
     * @param options Multiple options separated by | character
     * @param correctOption Index of the correct option (0-based)
     * @param points Point value of the question
     */
    public Question(int examId, String questionText, String options, int correctOption, int points) {
        this.examId = examId;
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
        this.points = points;
    }

    /**
     * Gets the question ID.
     * 
     * @return The question ID
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question ID.
     * 
     * @param questionId The question ID to set
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the exam ID.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }

    /**
     * Sets the exam ID.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }

    /**
     * Gets the text of the question.
     * 
     * @return The question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the text of the question.
     * 
     * @param questionText The question text to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Gets the options for the question.
     * For multiple choice questions, options are separated by | character.
     * For identification questions, this contains the answer.
     * 
     * @return The options string
     */
    public String getOptions() {
        return options;
    }

    /**
     * Sets the options for the question.
     * For multiple choice questions, options should be separated by | character.
     * For identification questions, this should contain the answer.
     * 
     * @param options The options string to set
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * Gets the index of the correct option for multiple choice questions.
     * 
     * @return The index of the correct option (0-based)
     */
    public int getCorrectOption() {
        return correctOption;
    }

    /**
     * Sets the index of the correct option for multiple choice questions.
     * 
     * @param correctOption The index of the correct option (0-based) to set
     */
    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    /**
     * Gets the point value of the question.
     * 
     * @return The points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the point value of the question.
     * 
     * @param points The points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
    
    /**
     * Gets the correct answer as a string.
     * For multiple choice questions, returns the text of the correct option.
     * For identification questions, returns the answer directly.
     * 
     * @return The correct answer as a string
     */
    public String getCorrectAnswer() {
        if (options == null || options.isEmpty()) {
            return "";
        }
        
        // If this is an identification question (no pipe separator in options)
        if (!options.contains("|")) {
            return options; // For identification questions, the answer is stored directly in options
        }
        
        // For multiple choice questions
        String[] optionsArray = options.split("\\|");
        if (correctOption >= 0 && correctOption < optionsArray.length) {
            return optionsArray[correctOption];
        }
        return "";
    }

    /**
     * Returns a string representation of the Question object.
     * 
     * @return A string containing the question's key information
     */
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", examId=" + examId +
                ", questionText='" + questionText + '\'' +
                ", options='" + options + '\'' +
                ", correctOption=" + correctOption +
                ", points=" + points +
                '}';
    }
} 