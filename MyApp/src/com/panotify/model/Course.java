package com.panotify.controller;

import javafx.beans.property.*;

public class Course {
    private final IntegerProperty courseId;
    private final StringProperty courseName;
    private final StringProperty courseCode;

    public Course(int id, String name, String code) {
        this.courseId = new SimpleIntegerProperty(id);
        this.courseName = new SimpleStringProperty(name);
        this.courseCode = new SimpleStringProperty(code);
    }

    public int getCourseId() { return courseId.get(); }
    public IntegerProperty courseIdProperty() { return courseId; }

    public String getCourseName() { return courseName.get(); }
    public StringProperty courseNameProperty() { return courseName; }

    public String getCourseCode() { return courseCode.get(); }
    public StringProperty courseCodeProperty() { return courseCode; }
}
